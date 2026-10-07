package com.zg.linketinder.util

import groovy.sql.Sql

class ConexaoDB {
    static Sql getConexao() {
        String url = "jdbc:postgresql://localhost:5432/linketinder"
        String user = "postgres"
        String password = "postgres"
        String driver = "org.postgresql.Driver"
        return Sql.newInstance(url, user, password, driver)
    }
}
