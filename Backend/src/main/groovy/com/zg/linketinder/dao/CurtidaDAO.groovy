package com.zg.linketinder.dao

import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class CurtidaDAO {

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

    def listarCurtidasEmpresa() {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                SELECT * FROM curtidas_empresa;
            '''
        try {
            def resultados = sql.rows(query)
            sql.commit()
            return resultados
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    def listarMatch() {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                SELECT c.nome AS nome_candidato, c.email AS email_candidato, e.nome AS nome_empresa, v.nome AS titulo_vaga
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
            def resultados = sql.rows(query)
            sql.commit()
            return resultados
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }
}