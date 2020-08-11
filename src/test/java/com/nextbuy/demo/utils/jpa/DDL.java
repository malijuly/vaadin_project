package com.nextbuy.demo.utils.jpa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DDL {

    @Test
    void createSchema() {
        JpaEntityDdlExport.start(null);
    }

}