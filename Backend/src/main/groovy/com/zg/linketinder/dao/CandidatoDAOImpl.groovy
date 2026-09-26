package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

import java.time.LocalDate

class CandidatoDAOImpl implements CandidatoDAO{
    @Override
    void salvarCandidato(Candidato candidato) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def queryCandidato = '''
                INSERT INTO candidatos (cpf, nome, email, senha,
                pais, estado, cidade, cep, data_nascimento, descricao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            '''

        def queryCompetencia = '''
                INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (?, ?)
            '''
        try {
            def chaves = sql.executeInsert(queryCandidato, [candidato.cpf, candidato.nome, candidato.email, candidato.senha,
                                      candidato.pais, candidato.estado, candidato.cidade, candidato.cep, candidato.data_nascimento, candidato.descricao])
            def idCandidato = chaves[0][0]
            if (candidato.competencias && !candidato.competencias.isEmpty()) {
                candidato.competencias.each { c ->
                    sql.executeInsert(queryCompetencia, [idCandidato, c.id])
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
    List<Candidato> listarCandidatoEmpresa(Integer id_empresa) {
        Sql sql = ConexaoDB.getConexao()
        Map<Integer, Candidato> mapaCandidatos = [:]

        def query = '''
                SELECT ct.id, '***.***.***-**' AS cpf, 'anonimo' || ct.id AS nome, 'anonimo@email.com' AS email, 
                       ct.senha, ct.pais, ct.estado, ct.cidade, ct.cep, ct.data_nascimento, ct.descricao, 
                       cm.id AS id_competencia, cm.nome AS nome_competencia 
                            FROM candidatos AS ct
                            INNER JOIN curtidas_candidato cc ON cc.id_candidato = ct.id
                            INNER JOIN vagas v ON v.id = cc.id_vaga
                            LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                            LEFT JOIN competencias cm ON cm.id = c.id_competencia
                            WHERE v.id_empresa = ? AND cc.curtiu = TRUE;
            '''
        try {
            sql.eachRow(query, [id_empresa]) { row ->
                Integer idCand = row.getInt('id')
                if (!mapaCandidatos.containsKey(idCand)) {
                    LocalDate dataNasc = row.data_nascimento instanceof java.sql.Date ? row.data_nascimento.toLocalDate() : row.data_nascimento
                    Candidato cand = new Candidato(id: idCand, cpf: row.cpf, nome: row.nome,
                            email: row.email, senha: row.senha, pais: row.pais, estado: row.estado,
                            cidade: row.cidade, cep: row.cep, data_nascimento: dataNasc, descricao: row.descricao)
                    cand.competencias = []
                    mapaCandidatos[idCand] = cand
                }

                if (row.id_competencia != null) {
                    mapaCandidatos[idCand].competencias.add(new Competencia(id: row.getInt('id_competencia'), nome: row.nome_competencia))
                }
            }
            return mapaCandidatos.values().toList()
        } finally {
            sql.close()
        }
    }

    @Override
    List<Candidato> listarCandidato() {
        Sql sql = ConexaoDB.getConexao()
        Map<Integer, Candidato> mapaCandidatos = [:]

        def query = '''
                SELECT ct.id, ct.cpf, ct. nome, ct.email, 
                                       ct.senha, ct.pais, ct.estado, ct.cidade, ct.cep, ct.data_nascimento, ct.descricao, 
                                       cm.id AS id_competencia, cm.nome AS nome_competencia 
                                FROM candidatos AS ct
                                LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                                LEFT JOIN competencias cm ON cm.id = c.id_competencia;
            '''
        try {
            sql.eachRow(query) { row ->
                Integer idCand = row.getInt('id')
                if (!mapaCandidatos.containsKey(idCand)) {
                    LocalDate dataNasc = row.data_nascimento instanceof java.sql.Date ? row.data_nascimento.toLocalDate() : row.data_nascimento
                    Candidato cand = new Candidato(id: idCand, cpf: row.cpf, nome: row.nome,
                            email: row.email, senha: row.senha, pais: row.pais, estado: row.estado,
                            cidade: row.cidade, cep: row.cep, data_nascimento: dataNasc, descricao: row.descricao)
                    cand.competencias = []
                    mapaCandidatos[idCand] = cand
                }

                if (row.id_competencia != null) {
                    mapaCandidatos[idCand].competencias.add(new Competencia(id: row.getInt('id_competencia'), nome: row.nome_competencia))
                }
            }
            return mapaCandidatos.values().toList()
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarIdCandidato(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        Candidato candidato = null
        def query = '''
                SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia FROM candidatos ct
                LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                LEFT JOIN competencias cm ON cm.id = c.id_competencia
                WHERE ct.id = ?;
            '''
        try {
            sql.eachRow(query, [id]) { row ->
                if (candidato == null) {
                    LocalDate dataNasc = row.data_nascimento instanceof java.sql.Date ? row.data_nascimento.toLocalDate() : row.data_nascimento
                    candidato = new Candidato(id: row.getInt('id'), cpf: row.cpf, nome: row.nome, email: row.email,
                            senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                            cep: row.cep, data_nascimento: dataNasc, descricao: row.descricao)
                    candidato.competencias = []
                }
                if (row.id_competencia != null) {
                    candidato.competencias.add(new Competencia(id: row.getInt('id_competencia'), nome: row.nome_competencia))
                }
            }
            return candidato
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarEmailCandidato(String email) {
        Sql sql = ConexaoDB.getConexao()
        Candidato candidato = null
        def query = '''
                SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia FROM candidatos ct
                LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                LEFT JOIN competencias cm ON cm.id = c.id_competencia
                WHERE ct.email = ?;
            '''
        try {
            sql.eachRow(query, [email]) { row ->
                if (candidato == null) {
                    LocalDate dataNasc = row.data_nascimento instanceof java.sql.Date ? row.data_nascimento.toLocalDate() : row.data_nascimento
                    candidato = new Candidato(id: row.getInt('id'), cpf: row.cpf, nome: row.nome, email: row.email,
                            senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                            cep: row.cep, data_nascimento: dataNasc, descricao: row.descricao)
                    candidato.competencias = []
                }
                if (row.id_competencia != null) {
                    candidato.competencias.add(new Competencia(id: row.getInt('id_competencia'), nome: row.nome_competencia))
                }
            }
            return candidato
        } finally {
            sql.close()
        }
    }

    @Override
    Candidato buscarCpfCandidato(String cpf) {
        Sql sql = ConexaoDB.getConexao()
        Candidato candidato = null
        def query = '''
                SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia FROM candidatos ct
                LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                LEFT JOIN competencias cm ON cm.id = c.id_competencia
                WHERE ct.cpf = ?;
            '''
        try {
            sql.eachRow(query, [cpf]) { row ->
                if (candidato == null) {
                    LocalDate dataNasc = row.data_nascimento instanceof java.sql.Date ? row.data_nascimento.toLocalDate() : row.data_nascimento
                    candidato = new Candidato(id: row.getInt('id'), cpf: row.cpf, nome: row.nome, email: row.email,
                            senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                            cep: row.cep, data_nascimento: dataNasc, descricao: row.descricao)
                    candidato.competencias = []
                }
                if (row.id_competencia != null) {
                    candidato.competencias.add(new Competencia(id: row.getInt('id_competencia'), nome: row.nome_competencia))
                }
            }
            return candidato
        } finally {
            sql.close()
        }
    }
}
