package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class CandidatoDAOImpl implements CandidatoDAO{
    @Override
    void salvarCandidato(Candidato candidato) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO candidatos (cpf, nome, email, senha,
                pais, estado, cidade, cep, data_nascimento, descricao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            '''
        try {
            sql.executeInsert(query, [candidato.cpf, candidato.nome, candidato.email, candidato.senha,
                                      candidato.pais, candidato.estado, candidato.cidade, candidato.cep, candidato.data_nascimento, candidato.descricao])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void atualizarCandidato(Candidato candidato) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                UPDATE candidatos 
                SET cpf = ?, nome = ?, email = ?, senha = ?, pais = ?, estado = ?, cidade = ?, cep = ?, data_nascimento = ?, descricao = ? 
                WHERE id = ?
            '''
        try {
            sql.executeUpdate(query, [candidato.cpf, candidato.nome, candidato.email, candidato.senha,
                candidato.pais, candidato.estado, candidato.cidade, candidato.cep, candidato.data_nascimento,
                candidato.descricao, candidato.id])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirCandidato(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                DELETE FROM candidatos WHERE id = ?
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
    List<Map>  listarCandidato() {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                SELECT * FROM candidatos;
            '''
        try {
            def resultado = sql.rows(query)
            sql.commit()
            return resultado
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    Map buscarIdCandidato(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                SELECT * FROM candidatos WHERE id = ?;
            '''
        try {
            def resultado = sql.firstRow(query, [id])
            sql.commit()
            return resultado
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }
}
