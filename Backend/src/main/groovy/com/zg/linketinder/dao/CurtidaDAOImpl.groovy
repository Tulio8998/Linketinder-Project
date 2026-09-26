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
    void salvarCurtidaCandidato(Integer id_candidato, Integer id_vaga, Boolean like) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu)
                VALUES (?, ?, ?)
                ON CONFLICT (id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
            '''
        try {
            sql.executeInsert(query, [id_candidato, id_vaga, like])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void salvarCurtidaEmpresa(Integer id_empresa, Integer id_candidato, Integer id_vaga, Boolean like) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu) 
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id_empresa, id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
            '''
        try {
            sql.executeInsert(query, [id_empresa, id_candidato, id_vaga, like])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<CurtidaCandidato> listarCurtidasCandidato() {
        Sql sql = ConexaoDB.getConexao()
        List<CurtidaCandidato> lista = []

        def query = '''
                SELECT cc.id, cc.curtiu,
                       c.id AS id_candidato, c.nome AS nome_candidato,
                       v.id AS id_vaga, v.nome AS nome_vaga
                FROM curtidas_candidato cc
                INNER JOIN candidatos c ON cc.id_candidato = c.id
                INNER JOIN vagas v ON cc.id_vaga = v.id;
            '''
        try {
            sql.eachRow(query) { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga)
                lista.add(new CurtidaCandidato (id: row.getInt('id'), candidato: cand, vaga: vg, curtiu: row.curtiu))
            }
            return lista
        }  finally {
            sql.close()
        }
    }

    @Override
    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        Sql sql = ConexaoDB.getConexao()
        List<CurtidaEmpresa> lista = []
        def query = '''
                SELECT ce.id, ce.curtiu,
                       e.id AS id_empresa, e.nome AS nome_empresa,
                       c.id AS id_candidato, c.nome AS nome_candidato,
                       v.id AS id_vaga, v.nome AS nome_vaga
                FROM curtidas_empresa ce
                INNER JOIN empresas e ON ce.id_empresa = e.id
                INNER JOIN candidatos c ON ce.id_candidato = c.id
                INNER JOIN vagas v ON ce.id_vaga = v.id;
            '''
        try {
            sql.eachRow(query) { row ->
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga)

                lista.add(new CurtidaEmpresa (id: row.getInt('id'), empresa: emp, candidato: cand, vaga: vg, curtiu: row.curtiu))
            }
            return lista
        }  finally {
            sql.close()
        }
    }

    @Override
    List<Match> listarMatchEmpresa(Integer id_empresa) {
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
                  AND ce.curtiu = TRUE
                  AND e.id = ?;
            '''
        try {
            sql.eachRow(query, [id_empresa]) { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato, email: row.email_candidato)
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga, empresa: emp)
                lista.add(new Match(candidato: cand, empresa: emp, vaga: vg))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    List<Match> listarMatchCandidato(Integer id_candidato) {
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
                  AND ce.curtiu = TRUE
                  AND c.id = ?;
            '''
        try {
            sql.eachRow(query, [id_candidato]) { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato, email: row.email_candidato)
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga, empresa: emp)
                lista.add(new Match(candidato: cand, empresa: emp, vaga: vg))
            }
            return lista
        } finally {
            sql.close()
        }
    }
}