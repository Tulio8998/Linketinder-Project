package com.zg.linketinder.factory

import groovy.sql.Sql

class ConexaoDB {
    private static Sql instancia
    private static DatabaseFactory factory = new PostgresFactory()

    static void setFactory(DatabaseFactory novaFactory) {
        factory = novaFactory
        resetar()
    }

    static Sql getConexao() {
        if (instancia == null) {
            instancia = factory.criarConexao()
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
}