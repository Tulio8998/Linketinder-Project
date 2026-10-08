package com.zg.linketinder.service.base

import com.zg.linketinder.dao.base.CrudDAO

abstract class CrudService<T> {
    protected abstract CrudDAO<T> getDao()
    protected void validarAntesSalvar(T entidade) {}
    protected void validarAntesAtualizar(Integer id, T entidade) {}

    void salvar(T entidade) {
        try {
            if (entidade == null) throw new IllegalArgumentException("A entidade nao pode ser nula")
            validarAntesSalvar(entidade)
            getDao().salvar(entidade)
        } catch (IllegalArgumentException e) {
            throw e
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar: ${e.message}")
        }
    }

    void atualizar(Integer id, T entidade) {
        try {
            if (entidade == null) throw new IllegalArgumentException("A entidade nao pode ser nula")
            if (getDao().buscarPorId(id) == null) throw new IllegalArgumentException("Entidade nao encontrada no banco")
            validarAntesAtualizar(id, entidade)
            getDao().atualizar(id, entidade)
        } catch (IllegalArgumentException e) {
            throw e
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar: ${e.message}")
        }
    }

    void excluir(Integer id) {
        try {
            if (getDao().buscarPorId(id) == null) throw new IllegalArgumentException("Entidade nao encontrada para exclusao")
            getDao().excluir(id)
        } catch (IllegalArgumentException e) {
            throw e
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao excluir: ${e.message}")
        }
    }

    List<T> listarTodos() {
        try {
            return getDao().listarTodos()
        } catch (Exception e) {
            println "Erro ao listar: ${e.message}"
            return []
        }
    }

    T buscarPorId(Integer id) {
        try {
            return getDao().buscarPorId(id)
        } catch (Exception e) {
            println "Erro ao buscar id: ${e.message}"
            return null
        }
    }
}