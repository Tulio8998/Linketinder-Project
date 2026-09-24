package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class CompetenciaDAOImpl implements CompetenciaDAO{
    @Override
    void salvaCompetencia(Competencia competencia) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO competencias (nome) VALUES (?)
            '''

        try {
            sql.executeInsert(query, [competencia.nome])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirCompetencia(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                DELETE FROM competencias WHERE id = ?
            '''

        try {
            sql.executeUpdate(query, [id])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<Competencia> listarCompetencia() {
        Sql sql = ConexaoDB.getConexao()
        List<Competencia> lista = []

        def query = '''
                SELECT * FROM competencias
            '''
        try {
            sql.eachRow(query) { row ->
                lista.add(new Competencia(id: row.id, nome: row.nome))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarIdCompetencia(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM competencias WHERE id = ?
            '''
        try {
            def row = sql.firstRow(query, [id])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarNomeCompetencia(String nome) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM competencias WHERE nome = ?
            '''
        try {
            def row = sql.firstRow(query, [nome])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }
}
