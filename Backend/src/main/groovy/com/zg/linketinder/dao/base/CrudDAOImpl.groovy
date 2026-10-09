package com.zg.linketinder.dao.base

import com.zg.linketinder.factory.ConexaoDB

abstract class CrudDAOImpl<T> implements CrudDAO<T> {

    protected abstract String getSqlInserir()
    protected abstract String getSqlAtualizar()
    protected abstract String getSqlExcluir()
    protected abstract String getSqlBuscarPorId()
    protected abstract String getSqlListarTodos()
    protected abstract List getParametrosInserir(T entidade)
    protected abstract List getParametrosAtualizar(Integer id, T entidade)
    protected abstract T mapearResultado(def row)

    @Override
    void salvar(T entidade) {
        ConexaoDB.emTransacao { sql ->
            sql.executeInsert(
                    getSqlInserir(),
                    getParametrosInserir(entidade)
            )
        }
    }

    @Override
    void atualizar(Integer id, T entidade) {
        ConexaoDB.emTransacao { sql ->
            sql.executeUpdate(
                    getSqlAtualizar(),
                    getParametrosAtualizar(id, entidade)
            )
        }
    }

    @Override
    void excluir(Integer id) {
        ConexaoDB.emTransacao { sql ->
            sql.executeUpdate(
                    getSqlExcluir(),
                    [id]
            )
        }
    }

    @Override
    T buscarPorId(Integer id) {
        ConexaoDB.consultar { sql ->
            def linhas = sql.rows(getSqlBuscarPorId(), [id])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    List<T> listarTodos() {
        ConexaoDB.consultar { sql ->
            return sql.rows(getSqlListarTodos()).collect { mapearResultado(it) }
        }
    }
}