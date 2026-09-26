package com.zg.linketinder.util

import groovy.sql.Sql

class ConexaoDB {
    static Sql getConexao() {
        def url = "jdbc:postgresql://localhost:5432/linketinder"
        def user = "postgres"
        def password = "postgres"
        def driver = "org.postgresql.Driver"
        return Sql.newInstance(url, user, password, driver)
    }
}
