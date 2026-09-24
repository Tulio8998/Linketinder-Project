package com.zg.linketinder.dao

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class VagaDAOImpl implements VagaDAO{
    @Override
    void salvarVaga(Vaga vaga) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa)
                VALUES (?, ?, ?, ?, ?, ?)
            '''

        def queryCompetencia = '''
                INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (?, ?)
            '''
        try {
            def chaves = sql.executeInsert(query, [vaga.nome, vaga.descricao, vaga.pais,
                                                   vaga.estado, vaga.cidade, vaga.empresa.id])
            def idVaga = chaves[0][0]
            if (vaga.competencias && !vaga.competencias.isEmpty()) {
                vaga.competencias.each { c ->
                    sql.executeInsert(queryCompetencia, [idVaga, c.id])
                }
            }
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void atualizarVaga(Vaga vaga) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                UPDATE vagas SET nome = ?, descricao = ?, pais = ?, estado = ?, cidade = ?
                WHERE id = ?
            '''
        try {
            sql.executeUpdate(query, [vaga.nome, vaga.descricao, vaga.pais,
                                      vaga.estado, vaga.cidade, vaga.id])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirVaga(Integer id_empresa, Integer id_vaga) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                DELETE FROM vagas WHERE id = ? AND id_empresa = ?
            '''
        try {
            sql.executeUpdate(query, [id_vaga, id_empresa])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<Vaga> listarVaga(Integer id_empresa) {
        Sql sql = ConexaoDB.getConexao()
        List<Vaga> lista = []

        def query = '''
                SELECT v.*, e.nome AS nome_empresa 
                FROM vagas v
                INNER JOIN empresas e ON v.id_empresa = e.id
                WHERE v.id_empresa = ?
            '''
        try {
            sql.eachRow(query, [id_empresa]) { row ->
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                lista.add(new Vaga(id: row.id, nome: row.nome, descricao: row.descricao,
                        pais: row.pais, estado: row.estado, cidade: row.cidade, empresa: emp ))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Vaga buscarIdVaga(Integer id_empresa, Integer id_vaga) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT v.*, e.nome AS nome_empresa 
                FROM vagas v
                INNER JOIN empresas e ON v.id_empresa = e.id
                WHERE v.id_empresa = ? AND v.id = ?
            '''
        try {
            def row = sql.firstRow(query, [id_empresa, id_vaga])
            if (row) {
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                return new Vaga(id: row.id, nome: row.nome, descricao: row.descricao,
                        pais: row.pais, estado: row.estado, cidade: row.cidade, empresa: emp
                )
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    Vaga buscarNomeVaga(Integer id_empresa, String nome) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT v.*, e.nome AS nome_empresa 
                FROM vagas v
                INNER JOIN empresas e ON v.id_empresa = e.id
                WHERE v.id_empresa = ? AND v.nome = ?
            '''
        try {
            def row = sql.firstRow(query, [id_empresa, nome])
            if (row) {
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                return new Vaga(id: row.id, nome: row.nome, descricao: row.descricao,
                        pais: row.pais, estado: row.estado, cidade: row.cidade, empresa: emp)
            }
            return null
        } finally {
            sql.close()
        }
    }
}