package com.zg.linketinder.util

import groovy.sql.Sql

class ConexaoDB {
    private static Sql instancia

    static Sql getConexao() {
        if (instancia == null) {
            instancia = criar()
        }
        return instancia
    }

    static <T> T emTransacao(Closure<T> operacao) {
        Sql sql = getConexao()
        sql.connection.autoCommit = false
        try {
            T resultado = operacao.call(sql)
            sql.commit()
            return resultado
        } catch (Exception e) {
            sql.rollback()
            throw e
        }
    }

    static <T> T consultar(Closure<T> operacao) {
        Sql sql = getConexao()
        return operacao.call(sql)
    }

    static void resetar() {
        if (instancia != null) {
            try { instancia.close() } catch (Exception ignored) { }
            instancia = null
        }
    }

    private static Sql criar() {
        String url = System.getProperty('db.url') ?: 'jdbc:postgresql://localhost:5432/linketinder'
        String user = System.getProperty('db.user') ?: 'postgres'
        String password = System.getProperty('db.password') ?: 'postgres'
        String driver = System.getProperty('db.driver') ?: 'org.postgresql.Driver'
        return Sql.newInstance(url, user, password, driver)
    }
}