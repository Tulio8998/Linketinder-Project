package com.zg.linketinder.factory

import groovy.sql.Sql

class PostgresFactory implements DatabaseFactory{
    @Override
    Sql criarConexao() {
        String url = System.getProperty('db.url') ?: 'jdbc:postgresql://localhost:5432/linketinder'
        String user = System.getProperty('db.user') ?: 'postgres'
        String password = System.getProperty('db.password') ?: 'postgres'
        String driver = System.getProperty('db.driver') ?: 'org.postgresql.Driver'
        return Sql.newInstance(url, user, password, driver)
    }
}
