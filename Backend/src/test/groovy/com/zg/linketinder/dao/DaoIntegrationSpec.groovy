package com.zg.linketinder.dao

import com.zg.linketinder.factory.ConexaoDB
import com.zg.linketinder.factory.DatabaseFactory
import com.zg.linketinder.factory.PostgresFactory
import com.zg.linketinder.factory.PostgresTestFactory
import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

abstract class DaoIntegrationSpec extends Specification {
    @Shared
    Sql sql

    private static DatabaseFactory factory = new PostgresTestFactory()

    def setupSpec() {
        ConexaoDB.setFactory(factory)
        sql = ConexaoDB.getConexao()
    }

    def cleanupSpec() {
        limparBanco()
        ConexaoDB.resetar()
    }

    void limparBanco() {
        sql.connection.autoCommit = false
        try {
            sql.execute('DELETE FROM candidatos_competencias')
            sql.execute('DELETE FROM vagas_competencias')
            sql.execute('DELETE FROM curtidas_candidato')
            sql.execute('DELETE FROM curtidas_empresa')
            sql.execute('DELETE FROM vagas')
            sql.execute('DELETE FROM candidatos')
            sql.execute('DELETE FROM empresas')
            sql.execute('DELETE FROM competencias')
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.connection.autoCommit = true
        }
    }
}