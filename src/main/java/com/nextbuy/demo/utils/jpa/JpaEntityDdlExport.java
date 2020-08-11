package com.nextbuy.demo.utils.jpa;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Blazej Palmaka
 */
public class JpaEntityDdlExport {

    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

//    private static final String SCHEMA_SQL = "schema_%s.sql";
    private static final String SCHEMA_SQL = "src/main/resources/schema.sql";
    private final static String PATTERN = "classpath*:**/*.class";
    private final static Class<? extends Dialect> DIALECT_CLASS = H2Dialect.class;
    //    private final static Class<? extends Dialect> DIALECT_CLASS = PostgreSQL10Dialect.class;
    private final static DDLFormatterImpl DDL_FORMATTER = DDLFormatterImpl.INSTANCE;

    public static void start(String[] args) {
        Map<String, Object> settings = new HashMap<>();
        settings.put("hibernate.dialect", DIALECT_CLASS);
        settings.put("hibernate.format_sql", true);
        settings.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        settings.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        settings.put("hibernate.id.new_generator_mappings", true);

        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources metadata = new MetadataSources(standardServiceRegistry);
        String pattern = getPattern(args);
        List<Class<?>> classes = getClassesByAnnotation(Entity.class, pattern);
        classes.forEach(metadata::addAnnotatedClass);
        MetadataImplementor metadataImplementor = (MetadataImplementor) metadata.getMetadataBuilder().build();
        SchemaExport schema = new SchemaExport();
        String outputFile = SCHEMA_SQL; //getOutputFilename(args);
        clearFileIfExist(outputFile);
        schema.setOutputFile(outputFile);
        schema.setDelimiter(";");
        schema.createOnly(EnumSet.of(TargetType.SCRIPT), metadataImplementor);
//        appendSemicolon(outputFile);
        appendMetaData(outputFile, settings);
    }

    private static String getPattern(String[] args) {
        String pattern = PATTERN;
        if (args != null && args.length >= 3
                && StringUtils.hasText(args[2])) {
            pattern = args[2];
        }
        return pattern;
    }

    private static void appendMetaData(String outputFile, Map<String, Object> settings) {
        String charsetName = "UTF-8";
        File ddlFile = new File(outputFile);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/* Generate Environment\n");
            for (Map.Entry<String, Object> entry : settings.entrySet()) {
                sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            sb.append("*/\n");
            String ddlFileContents = StreamUtils.copyToString(new FileInputStream(ddlFile), Charset.forName(charsetName));
            ddlFileContents = Stream.of(ddlFileContents.split(LINE_SEPARATOR))
                    .map(line -> DDL_FORMATTER.format(line) + System.lineSeparator())
                    .collect(Collectors.joining());
            sb.append(ddlFileContents);
            FileCopyUtils.copy(sb.toString().getBytes(charsetName), ddlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendSemicolon(String outputFile) {
        String charsetName = "UTF-8";
        File ddlFile = new File(outputFile);
        try {
            String ddlFileContents = StreamUtils.copyToString(new FileInputStream(ddlFile), Charset.forName(charsetName)).trim();
            ddlFileContents = ddlFileContents.replaceAll("\r\n", ";\r\n");
            ddlFileContents = ddlFileContents.replaceAll("\n\n", ";\n\n");
            ddlFileContents = ddlFileContents.replaceAll("\n.*(?![\f\n\r])$", ";\n");
            FileCopyUtils.copy(ddlFileContents.getBytes(charsetName), ddlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation, String pattern) {
        return getResources(pattern).stream()
                .map(r -> metadataReader(r))
                .filter(Objects::nonNull)
                .filter(mr -> mr.getAnnotationMetadata().hasAnnotation(annotation.getName()))
                .map(mr -> entityClass(mr))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<Resource> getResources(String pattern) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources(pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(resources);
    }


    private static Class<?> entityClass(MetadataReader mr) {
        String className = mr.getClassMetadata().getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.printf("%s Class not found", className);
            return null;
        }
        return clazz;
    }

    private static MetadataReader metadataReader(Resource r) {
        MetadataReader mr;
        try {
            mr = new SimpleMetadataReaderFactory().getMetadataReader(r);
        } catch (Throwable e) {
            System.err.printf(e.getMessage());
            return null;
        }
        return mr;
    }

    private static void clearFileIfExist(String outputFile) {
        if(Files.exists(Path.of(outputFile))) {
            try(PrintWriter writer = new PrintWriter(outputFile)) {
                writer.print("");
            } catch (Exception e) {
                System.err.printf(e.getMessage());
            }
        }
    }

    private static String getOutputFilename(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        if (args != null && args.length > 0
                && StringUtils.hasText(args[0])) {
            String customSchemaName = args[0];
            if (customSchemaName.contains("%s")) {
                return String.format(customSchemaName, currentDate);
            }
            return customSchemaName;
        }
        return String.format(SCHEMA_SQL, currentDate);
    }
}