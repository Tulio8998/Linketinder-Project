package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAOImpl
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.sql.CompetenciaSql
import com.zg.linketinder.util.ConexaoDB

class CompetenciaDAOImpl extends CrudDAOImpl<Competencia> implements CompetenciaDAO {

    @Override
    protected String getSqlInserir() { return CompetenciaSql.INSERIR }

    @Override
    protected String getSqlAtualizar() { return CompetenciaSql.ATUALIZAR }

    @Override
    protected String getSqlExcluir() { return CompetenciaSql.EXCLUIR }

    @Override
    protected String getSqlBuscarPorId() { return CompetenciaSql.BUSCAR_POR_ID }

    @Override
    protected String getSqlListarTodos() { return CompetenciaSql.LISTAR_TODAS }

    @Override
    protected List getParametrosInserir(Competencia competencia) {
        return [competencia.nome]
    }

    @Override
    protected List getParametrosAtualizar(Integer id, Competencia competencia) {
        return [competencia.nome, id]
    }

    @Override
    protected Competencia mapearResultado(def row) {
        return new Competencia(id: row.id, nome: row.nome)
    }

    @Override
    Competencia buscarNomeCompetencia(String nome) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(CompetenciaSql.BUSCAR_POR_NOME, [nome])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    void salvaCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CompetenciaSql.INSERIR_CANDIDATO, [id_candidato, id_competencia])
        }
    }

    @Override
    void excluirCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        ConexaoDB.emTransacao { sql ->
            sql.executeUpdate(CompetenciaSql.EXCLUIR_CANDIDATO, [id_candidato, id_competencia])
        }
    }

    @Override
    List<Competencia> listarCompetenciasCandidato(Integer id_candidato) {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CompetenciaSql.LISTAR_POR_CANDIDATO, [id_candidato]).collect { mapearResultado(it) }
        }
    }

    @Override
    Competencia buscarIdCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(CompetenciaSql.BUSCAR_POR_ID_E_CANDIDATO, [id_candidato, id_competencia])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    Competencia buscarNomeCompetenciaCandidato(Integer id_candidato, String nome) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(CompetenciaSql.BUSCAR_POR_NOME_E_CANDIDATO, [id_candidato, nome])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    void salvaCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(CompetenciaSql.INSERIR_VAGA, [id_vaga, id_competencia])
        }
    }

    @Override
    void excluirCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        ConexaoDB.emTransacao { sql ->
            sql.executeUpdate(CompetenciaSql.EXCLUIR_VAGA, [id_vaga, id_competencia])
        }
    }

    @Override
    List<Competencia> listarCompetenciasVaga(Integer id_vaga) {
        return ConexaoDB.consultar { sql ->
            return sql.rows(CompetenciaSql.LISTAR_POR_VAGA, [id_vaga]).collect { mapearResultado(it) }
        }
    }

    @Override
    Competencia buscarIdCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(CompetenciaSql.BUSCAR_POR_ID_E_VAGA, [id_vaga, id_competencia])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    Competencia buscarNomeCompetenciaVaga(Integer id_vaga, String nome) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(CompetenciaSql.BUSCAR_POR_NOME_E_VAGA, [id_vaga, nome])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }
}