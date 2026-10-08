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

    static <T> T emTransacao(Closure<T> operacao) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        try {
            T resultado = operacao.call(sql)
            sql.commit()
            return resultado
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    static <T> T consultar(Closure<T> operacao) {
        Sql sql = ConexaoDB.getConexao()
        try {
            return operacao.call(sql)
        } finally {
            sql.close()
        }
    }
}
