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
    List<Candidato> listarCandidato() {
        Sql sql = ConexaoDB.getConexao()
        List<Candidato> lista = []

        def query = '''
                SELECT * FROM candidatos;
            '''
        try {
            sql.eachRow(query) { row ->
                lista.add(new Candidato(id: row.id, cpf: row.cpf, nome: row.nome,
                        email: row.email, senha: row.senha, pais: row.pais, estado: row.estado,
                        cidade: row.cidade, cep: row.cep, data_nascimento: row.data_nascimento, descricao: row.descricao))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarIdCandidato(Integer id) {
        Sql sql = ConexaoDB.getConexao()

        def query = '''
                SELECT * FROM candidatos WHERE id = ?;
            '''
        try {
            def row = sql.firstRow(query, [id])
            if (row) {
                return new Candidato(id: row.id, cpf: row.cpf, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, data_nascimento: row.data_nascimento, descricao: row.descricao
                )
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarEmailCandidato(String email) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM candidatos WHERE email = ?
            '''
        try {
            def row = sql.firstRow(query, [email])
            if (row) {
                return new Candidato(id: row.id, cpf: row.cpf, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, data_nascimento: row.data_nascimento, descricao: row.descricao
                )
            }
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarCpfCandidato(String cpf) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * from candidatos WHERE cpf = ?
            '''
        try {
            def row = sql.firstRow(query, [cpf])
            if (row) {
                return new Candidato(id: row.id, cpf: row.cpf, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, data_nascimento: row.data_nascimento, descricao: row.descricao
                )
            }
        } finally {
            sql.close()
        }
    }
}
