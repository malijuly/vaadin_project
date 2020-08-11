package com.nextbuy.demo.utils.jpa;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.Slf4JLogger;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class P6SpyAppender extends Slf4JLogger {
    // https://en.wikipedia.org/wiki/ANSI_escape_code
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BRIGHT_MAGENTA = "\u001B[95m";

    private final Logger log;
    private String previousBatchSql;

    @Setter
    private static boolean enabled = true;

    public P6SpyAppender() {
        log = LoggerFactory.getLogger("SQL");
    }

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        if (!enabled) return;

        // Pomiń puste logi (commit)
        if (category == Category.COMMIT) return;

        // Pomiń  logi zmiany schematu
        if (sql.indexOf("set schema") == 0)
            return;

        if (category == Category.ROLLBACK && (sql == null || sql.isEmpty()))
            return;

        // Pomiń zduplikowane logi 'batch' (insert, update, delete)
        if (category == Category.BATCH) {
            if (Objects.equals(previousBatchSql, sql))
                return;

            previousBatchSql = sql;
        }

        String msg = fixAliases(sql);
        msg = ANSI_BRIGHT_MAGENTA + msg + ANSI_RESET;

        if (Category.ERROR.equals(category)) {
            log.error(msg);
        } else if (Category.WARN.equals(category)) {
            log.warn(msg);
        } else if (Category.DEBUG.equals(category)) {
            log.debug(msg);
        } else {
            log.info(msg);
        }
    }

    enum QueryStep {
        SELECT, FROM, JOIN, WHERE
    }

    /**
     * Funkcja usuwa aliasy kolumn oraz
     * usuwa (dla zapytań z jednej tabeli) / aktualizuje (dla zapytań z wielu tabel) aliasy tabel.
     * <p>
     * Działa tylko dla hibernate-owych zapytań "SELECT"!
     */
    String fixAliases(String sql) {
        if (!sql.startsWith("select"))
            return sql;

        final String[] items = sql.split(" ");

        // Zbierz wszystkie aliasy tablic ------------------------------------------------------------------------------
        Map<String, String> hibernateTableAliases = new HashMap<>(); // <tableAlias, tableName>

        for (int i = 0; i < items.length; i++) {
            final String item = items[i];

            if ("from".equals(item) || "join".equals(item)) {
                final String tableName = i < items.length - 1 ? items[i + 1] : null;
                final String tableAlias = i < items.length - 2 ? items[i + 2] : null;

                if (tableAlias != null && tableAlias.endsWith("_"))
                    hibernateTableAliases.put(tableAlias, tableName);
            }
        }

        // Wygeneruj nowe aliasy tablic --------------------------------------------------------------------------------
        Map<String, String> newTableAliases = new HashMap<>(); // <oldTableAlias, newTableAlias>
        for (String hibernateAlias : hibernateTableAliases.keySet()) {
            final String tableNameWithSchema = hibernateTableAliases.get(hibernateAlias);

            final int dotIndex = tableNameWithSchema.indexOf('.');
            final String schema = dotIndex > 0 ? tableNameWithSchema.substring(0, dotIndex) : null; // Schemat tabeli
            final String tableName = dotIndex > 0 ? tableNameWithSchema.substring(dotIndex + 1) : tableNameWithSchema; // Nazwa tabeli bez schematu
            final String firstSchemaLetter = schema != null ? (schema.substring(0, 1) + "_") : ""; // Pierwsza litera schematu tabeli

            final String[] tableNameParts = tableName.split("_");

            for (int wordTrimSize = 1; wordTrimSize < 20; wordTrimSize++) {
                String sb = firstSchemaLetter;
                for (int i = 0; i < tableNameParts.length; i++) {
                    final String tableNameWord = tableNameParts[i];
                    sb += tableNameWord.substring(0, wordTrimSize > tableNameWord.length() ? tableNameWord.length() : wordTrimSize);
                }

                final String generatedAlias = sb;
                if (!newTableAliases.values().contains(generatedAlias)) {
                    newTableAliases.put(hibernateAlias, generatedAlias);
                    break;
                }
            }
        }

        // Usuń aliasy kolumn oraz zmień/usuń aliasy tabel -------------------------------------------------------------
        final boolean removeTablePrefixes = newTableAliases.size() <= 1; // Usuń prefixy tabel w kolumnach jeśli w zapytaniu jest tylko jedna tabela
        StringJoiner result = new StringJoiner(" ");

        QueryStep queryStep = null;
        int indexJump = 1;

        for (int i = 0; i < items.length; i += indexJump) {
            QueryStep step = null;
            indexJump = 1;
            final String currentItem = items[i];
            final String secondItem = i < items.length - 1 ? items[i + 1] : null;
            final String thirdItem = i < items.length - 2 ? items[i + 2] : null;
            String resultItem = currentItem;

            // Pobranie aktualnie przetwarzanego kroku SQL [SELECT, FROM, JOIN, WHERE]
            if (queryStep == null && "select".equals(currentItem))
                step = QueryStep.SELECT;
            else if (queryStep == QueryStep.SELECT && "from".equals(currentItem))
                step = QueryStep.FROM;
            else if (queryStep == QueryStep.FROM && "join".equals(currentItem))
                step = QueryStep.JOIN;
            else if ((queryStep == QueryStep.FROM || queryStep == QueryStep.JOIN) && "where".equals(currentItem))
                step = QueryStep.WHERE;

            // Pomiń przetwarzanie jeśli aktualny element to słowo kluczowe SQL [SELECT, FROM, JOIN, WHERE]
            if (step != null) {
                queryStep = step;
                result.add(resultItem);
                continue;
            }

            // Sprawdzenie czy przetwarzany jest blok z kolumnami czy z tabelami
            switch (queryStep) {
                case SELECT:
                    if ("as".equals(secondItem) && !"end".equals(currentItem)) {
                        indexJump = 3;
                        final int dotIndex = currentItem.indexOf('.'); // Separator między aliasem tabeli i kolumną (table_name.column_name)
                        final boolean addComma = thirdItem.endsWith(","); // Przecinek między kolumnami

                        if (dotIndex > 0) {
                            final int bracketIndex = currentItem.indexOf('(');
                            final String tableAlias = currentItem.substring(bracketIndex >= 0 ? bracketIndex + 1 : 0, dotIndex);
                            final String columnName = currentItem.substring(dotIndex + 1);
                            // Zaktualizuj alias (jeśli zapytanie z wielu tabel) lub usuń (zapytanie z jednej tabeli)
                            resultItem = removeTablePrefixes ? columnName : newTableAliases.get(tableAlias) + "." + columnName;

                            if (bracketIndex >= 0)
                                resultItem = currentItem.substring(0, bracketIndex + 1) + resultItem;
                        }

                        if (addComma)
                            resultItem += ',';
                    } else if ("when".equals(currentItem)) {
                        final int dotIndex = secondItem.indexOf('.');
                        if (dotIndex > 0) {
                            indexJump = 2;
                            result.add(currentItem); // dodanie "when"

                            final String tableAlias = secondItem.substring(0, dotIndex);
                            final String columnName = secondItem.substring(dotIndex + 1);
                            resultItem = removeTablePrefixes ? columnName : newTableAliases.get(tableAlias) + "." + columnName;
                            result.add(resultItem);
                            continue;
                        }
                    }
                    break;
                case FROM:
                    if (newTableAliases.containsKey(currentItem)) {
                        if (removeTablePrefixes)
                            continue;
                        resultItem = newTableAliases.get(currentItem);
                    }
                    break;
                case JOIN:
                    if ("on".equals(secondItem)) {
                        resultItem = newTableAliases.get(currentItem);
                    } else {
                        final int equalIndex = currentItem.indexOf('=');

                        if (equalIndex > 0) {
                            String leftSideJoinOn = currentItem.substring(0, equalIndex);
                            String rightSideJoinOn = currentItem.substring(equalIndex + 1);

                            int dotIndex = leftSideJoinOn.indexOf('.');
                            if (dotIndex > 0)
                                leftSideJoinOn = newTableAliases.get(leftSideJoinOn.substring(0, dotIndex)) + leftSideJoinOn.substring(dotIndex);

                            dotIndex = rightSideJoinOn.indexOf('.');
                            if (dotIndex > 0)
                                rightSideJoinOn = newTableAliases.get(rightSideJoinOn.substring(0, dotIndex)) + rightSideJoinOn.substring(dotIndex);

                            resultItem = leftSideJoinOn + "=" + rightSideJoinOn;
                        }
                    }
                    break;
                case WHERE:
                    final int dotIndex = currentItem.indexOf('.'); // Separator między aliasem tabeli i kolumną (table_name.column_name)

                    if (dotIndex > 0) {
                        final int bracketIndex = currentItem.indexOf('(');
                        final String tableAlias = currentItem.substring(bracketIndex >= 0 ? bracketIndex + 1 : 0, dotIndex);
                        final String columnName = currentItem.substring(dotIndex + 1, currentItem.length());

                        if (removeTablePrefixes) {
                            resultItem = columnName;
                        } else if (newTableAliases.containsKey(tableAlias)) {
                            resultItem = newTableAliases.get(tableAlias) + "." + columnName;
                        }

                        if (bracketIndex >= 0)
                            resultItem = currentItem.substring(0, bracketIndex + 1) + resultItem;
                    }
                    break;
            }
            result.add(resultItem);
        }

        return result.toString();
    }




    enum AnsiColor {
        RESET(),
        RED(31, 41),
        GREEN(32, 42),
        YELLOW(33, 43);

        private int fontColorCode;
        private int backgroundColorCode;

        private AnsiColor() {
            this(0, 0);
        }

        private AnsiColor(int fontColorCode, int backgroundColorCode) {
        }
    }
}