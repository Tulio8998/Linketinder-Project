package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.curtida.Curtida
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class CurtidaDAOImpl implements CurtidaDAO{

    @Override
    void salvarCurtidaCandidato(Integer idCandidato, Integer idVaga, Boolean like) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu)
                VALUES (?, ?, ?)
                ON CONFLICT (id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
            '''
        try {
            sql.executeInsert(query, [idCandidato, idVaga, like])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void salvarCurtidaEmpresa(Integer idEmpresa, Integer idCandidato, Integer idVaga, Boolean like) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu) 
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id_empresa, id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
            '''
        try {
            sql.executeInsert(query, [idEmpresa, idCandidato, idVaga, like])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        Sql sql = ConexaoDB.getConexao()
        List<CurtidaEmpresa> lista = []
        def query = '''
                SELECT * FROM curtidas_empresa;
            '''
        try {
            sql.eachRow(query) { row ->
                Empresa emp = new Empresa(id: row.id_empresa)
                Candidato cand = new Candidato(id: row.id_candidato)
                Vaga vg = new Vaga(id: row.id_vaga)
                lista.add(new CurtidaEmpresa (id: row.id, empresa: emp, candidato: cand, vaga: vg, curtiu: row.curtiu))
            }
            return lista
        }  finally {
            sql.close()
        }
    }

    @Override
    List<CurtidaCandidato> listarCurtidasCandidato() {
        Sql sql = ConexaoDB.getConexao()
        List<CurtidaCandidato> lista = []
        def query = '''
                SELECT * FROM curtidas_candidato;
            '''
        try {
            sql.eachRow(query) { row ->
                Candidato cand = new Candidato(id: row.id_candidato)
                Vaga vg = new Vaga(id: row.id_vaga)
                lista.add(new CurtidaCandidato (id: row.id, candidato: cand, vaga: vg, curtiu: row.curtiu))
            }
            return lista
        }  finally {
            sql.close()
        }
    }

    @Override
    List<Match> listarMatch() {
        Sql sql = ConexaoDB.getConexao()
        List<Match> lista = []

        def query = '''
                SELECT c.id AS id_candidato, c.nome AS nome_candidato, c.email AS email_candidato, 
                       e.id AS id_empresa, e.nome AS nome_empresa, 
                       v.id AS id_vaga, v.nome AS nome_vaga
                FROM curtidas_candidato cc
                INNER JOIN curtidas_empresa ce 
                    ON cc.id_candidato = ce.id_candidato 
                    AND cc.id_vaga = ce.id_vaga
                INNER JOIN candidatos c 
                    ON cc.id_candidato = c.id
                INNER JOIN empresas e 
                    ON ce.id_empresa = e.id
                INNER JOIN vagas v 
                    ON cc.id_vaga = v.id
                WHERE cc.curtiu = TRUE 
                  AND ce.curtiu = TRUE;
            '''
        try {
            sql.eachRow(query) { row ->
                Candidato cand = new Candidato(id: row.id_candidato, nome: row.nome_candidato, email: row.email_candidato)
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                Vaga vg = new Vaga(id: row.id_vaga, nome: row.nome_vaga, empresa: emp)
                lista.add(new Match(candidato: cand, empresa: emp, vaga: vg))
            }
            return lista
        } finally {
            sql.close()
        }
    }
}