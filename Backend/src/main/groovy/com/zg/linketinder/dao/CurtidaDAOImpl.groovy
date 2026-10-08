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
    void salvarCurtidaCandidato(Integer idCandidato, Integer idVaga, Boolean curtiu) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CurtidaSql.INSERIR_CANDIDATO, [idCandidato, idVaga, curtiu])
        }
    }

    @Override
    void salvarCurtidaEmpresa(Integer idEmpresa, Integer idCandidato, Integer idVaga, Boolean curtiu) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CurtidaSql.INSERIR_EMPRESA, [idEmpresa, idCandidato, idVaga, curtiu])
        }
    }

    @Override
    List<CurtidaCandidato> listarCurtidasCandidato() {
        return ConexaoDB.consultar { sql ->
            sql.rows(CurtidaSql.LISTAR_CURTIDAS_CANDIDATO).collect { row ->
                Candidato cand = new Candidato(id: row.id_candidato, nome: row.nome_candidato)
                Vaga vaga = new Vaga(id: row.id_vaga, nome: row.nome_vaga)
                new CurtidaCandidato(id: row.id, candidato: cand, vaga: vaga, curtiu: row.curtiu)
            }
        }
    }

    @Override
    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        return ConexaoDB.consultar { sql ->
            sql.rows(CurtidaSql.LISTAR_CURTIDAS_EMPRESA).collect { row ->
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                Candidato cand = new Candidato(id: row.id_candidato, nome: row.nome_candidato)
                Vaga vaga = new Vaga(id: row.id_vaga, nome: row.nome_vaga)
                new CurtidaEmpresa(id: row.id, empresa: emp, candidato: cand, vaga: vaga, curtiu: row.curtiu)
            }
        }
    }

    @Override
    List<Match> listarMatchEmpresa(Integer idEmpresa) {
        return ConexaoDB.consultar { sql ->
            sql.rows(CurtidaSql.LISTAR_MATCHES_EMPRESA, [idEmpresa]).collect { row ->
                Candidato cand = new Candidato(
                        id: row.id_candidato,
                        nome: row.nome_candidato,
                        email: row.email_candidato
                )
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                Vaga vaga = new Vaga(id: row.id_vaga, nome: row.nome_vaga, empresa: emp)
                new Match(candidato: cand, empresa: emp, vaga: vaga)
            }
        }
    }

    @Override
    List<Match> listarMatchCandidato(Integer idCandidato) {
        return ConexaoDB.consultar { sql ->
            sql.rows(CurtidaSql.LISTAR_MATCHES_CANDIDATO, [idCandidato]).collect { row ->
                Candidato cand = new Candidato(
                        id: row.id_candidato,
                        nome: row.nome_candidato,
                        email: row.email_candidato
                )
                Empresa emp = new Empresa(id: row.id_empresa, nome: row.nome_empresa)
                Vaga vaga = new Vaga(id: row.id_vaga, nome: row.nome_vaga, empresa: emp)
                new Match(candidato: cand, empresa: emp, vaga: vaga)
            }
        }
    }
}