package com.zg.linketinder.factory

import groovy.sql.Sql

class PostgresTestFactory implements DatabaseFactory{
    @Override
    Sql criarConexao() {
        String url = 'jdbc:postgresql://localhost:5432/linketinder_test'
        String user = 'postgres'
        String password = 'postgres'
        String driver = 'org.postgresql.Driver'
        return Sql.newInstance(url, user, password, driver)
    }
}
