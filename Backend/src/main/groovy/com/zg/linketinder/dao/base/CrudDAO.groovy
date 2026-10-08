package com.zg.linketinder.dao.base

interface CrudDAO<T> {
    void salvar(T entidade)
    void atualizar(Integer id, T entidade)
    void excluir(Integer id)
    T buscarPorId(Integer id)
    List<T> listarTodos()
}
