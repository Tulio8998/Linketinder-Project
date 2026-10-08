package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.sql.CurtidaSql
import com.zg.linketinder.util.ConexaoDB

class CurtidaDAOImpl implements CurtidaDAO {

    @Override
    void salvarCurtidaCandidato(Integer id_candidato, Integer id_vaga, Boolean like) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CurtidaSql.INSERIR_CANDIDATO, [id_candidato, id_vaga, like])
        }
    }

    @Override
    void salvarCurtidaEmpresa(Integer id_empresa, Integer id_candidato, Integer id_vaga, Boolean like) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CurtidaSql.INSERIR_EMPRESA, [id_empresa, id_candidato, id_vaga, like])
        }
    }

    @Override
    List<CurtidaCandidato> listarCurtidasCandidato() {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CurtidaSql.LISTAR_CURTIDAS_CANDIDATO).collect { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga)
                new CurtidaCandidato(id: row.getInt('id'), candidato: cand, vaga: vg, curtiu: row.curtiu)
            }
        }
    }

    @Override
    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CurtidaSql.LISTAR_CURTIDAS_EMPRESA).collect { row ->
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga)
                new CurtidaEmpresa(id: row.getInt('id'), empresa: emp, candidato: cand, vaga: vg, curtiu: row.curtiu)
            }
        }
    }

    @Override
    List<Match> listarMatchEmpresa(Integer id_empresa) {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CurtidaSql.LISTAR_MATCHES_EMPRESA, [id_empresa]).collect { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato, email: row.email_candidato)
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga, empresa: emp)
                new Match(candidato: cand, empresa: emp, vaga: vg)
            }
        }
    }

    @Override
    List<Match> listarMatchCandidato(Integer id_candidato) {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CurtidaSql.LISTAR_MATCHES_CANDIDATO, [id_candidato]).collect { row ->
                Candidato cand = new Candidato(id: row.getInt('id_candidato'), nome: row.nome_candidato, email: row.email_candidato)
                Empresa emp = new Empresa(id: row.getInt('id_empresa'), nome: row.nome_empresa)
                Vaga vg = new Vaga(id: row.getInt('id_vaga'), nome: row.nome_vaga, empresa: emp)
                new Match(candidato: cand, empresa: emp, vaga: vg)
            }
        }
    }
}