package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAOImpl
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.sql.CandidatoSql
import com.zg.linketinder.factory.ConexaoDB
import groovy.sql.Sql
import java.time.LocalDate

class CandidatoDAOImpl extends CrudDAOImpl<Candidato> implements CandidatoDAO {

    @Override
    protected String getSqlInserir() { return CandidatoSql.INSERIR }

    @Override
    protected String getSqlAtualizar() { return CandidatoSql.ATUALIZAR }

    @Override
    protected String getSqlExcluir() { return CandidatoSql.EXCLUIR }

    @Override
    protected String getSqlBuscarPorId() { return CandidatoSql.BUSCAR_POR_ID }

    @Override
    protected String getSqlListarTodos() { return CandidatoSql.LISTAR_TODOS }

    @Override
    protected List getParametrosInserir(Candidato candidato) {
        return [candidato.cpf, candidato.nome, candidato.email, candidato.senha, candidato.pais,
                candidato.estado, candidato.cidade, candidato.cep, candidato.dataNascimento, candidato.descricao]
    }

    @Override
    protected List getParametrosAtualizar(Integer id, Candidato candidato) {
        return [candidato.cpf, candidato.nome, candidato.email, candidato.senha, candidato.pais,
                candidato.estado, candidato.cidade, candidato.cep, candidato.dataNascimento, candidato.descricao, id]
    }

    @Override
    protected Candidato mapearResultado(def row) {
        return mapearLinha(row)
    }

    @Override
    List<Candidato> listarTodos() {
        return ConexaoDB.consultar { Sql sql ->
            return agruparPorId(sql.rows(CandidatoSql.LISTAR_TODOS))
        }
    }

    @Override
    Candidato buscarPorId(Integer id) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(CandidatoSql.BUSCAR_POR_ID, [id]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    @Override
    void salvar(Candidato candidato) {
        ConexaoDB.emTransacao { Sql sql ->
            def chaves = sql.executeInsert(CandidatoSql.INSERIR, getParametrosInserir(candidato))
            long idCandidato = chaves[0][0] as long
            salvarCompetencias(sql, idCandidato, candidato.competencias)
            candidato.id = (int) idCandidato
        }
    }

    @Override
    void atualizar(Integer id, Candidato candidato) {
        ConexaoDB.emTransacao { Sql sql ->
            sql.executeUpdate(CandidatoSql.ATUALIZAR, getParametrosAtualizar(id, candidato))
            sql.executeUpdate(CandidatoSql.EXCLUIR_COMPETENCIAS, [id])
            salvarCompetencias(sql, id, candidato.competencias)
        }
    }

    @Override
    List<Candidato> listarCandidatoEmpresa(Integer idEmpresa) {
        return ConexaoDB.consultar { Sql sql ->
            agruparPorId(sql.rows(CandidatoSql.LISTAR_POR_EMPRESA, [idEmpresa]))
        }
    }

    @Override
    Candidato buscarEmailCandidato(String email) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(CandidatoSql.BUSCAR_POR_EMAIL, [email]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    @Override
    Candidato buscarCpfCandidato(String cpf) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(CandidatoSql.BUSCAR_POR_CPF, [cpf]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    private List<Candidato> agruparPorId(List linhas) {
        if (!linhas || linhas.isEmpty()) return []
        Map<Integer, Candidato> mapa = [:]
        linhas.each { linha ->
            Integer id = (linha.id instanceof Number) ? ((Number) linha.id).intValue() : Integer.parseInt(linha.id.toString())
            if (!mapa.containsKey(id)) {
                mapa[id] = mapearLinha(linha)
            }
            if (linha.id_competencia != null) {
                Integer idComp = (linha.id_competencia instanceof Number) ?
                        ((Number) linha.id_competencia).intValue() :
                        Integer.parseInt(linha.id_competencia.toString())
                mapa[id].competencias.add(new Competencia(
                        id: idComp,
                        nome: linha.nome_competencia
                ))
            }
        }
        return mapa.values().toList()
    }

    private Candidato mapearLinha(linha) {
        return new Candidato(
                id: (linha.id instanceof Number) ? ((Number) linha.id).intValue() : Integer.parseInt(linha.id.toString()),
                cpf: linha.cpf,
                nome: linha.nome,
                email: linha.email,
                senha: linha.senha,
                pais: linha.pais,
                estado: linha.estado,
                cidade: linha.cidade,
                cep: linha.cep,
                dataNascimento: paraDataLocal(linha.data_nascimento),
                descricao: linha.descricao,
                competencias: []
        )
    }

    private void salvarCompetencias(Sql sql, long idCandidato, List<Competencia> competencias) {
        if (!competencias) return
        competencias.each { competencia ->
            sql.executeInsert(CandidatoSql.INSERIR_COMPETENCIA, [idCandidato, competencia.id])
        }
    }

    private LocalDate paraDataLocal(valor) {
        return valor instanceof java.sql.Date ? valor.toLocalDate() : valor
    }
}