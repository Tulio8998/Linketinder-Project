package com.zg.linketinder.dao

import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

abstract class DaoIntegrationSpec extends Specification {
    @Shared
    Sql sql

    def setupSpec() {
        System.setProperty('db.url', 'jdbc:postgresql://localhost:5432/linketinder_test')
        System.setProperty('db.user', 'postgres')
        System.setProperty('db.password', 'postgres')
        System.setProperty('db.driver', 'org.postgresql.Driver')

        ConexaoDB.resetar()
        sql = ConexaoDB.getConexao()
    }

    def cleanupSpec() {
        limparBanco()

        System.clearProperty('db.url')
        System.clearProperty('db.user')
        System.clearProperty('db.password')
        System.clearProperty('db.driver')

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