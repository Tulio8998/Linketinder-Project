package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.model.Vaga

interface VagaDAO extends CrudDAO<Vaga> {
    void excluirVaga(Integer idEmpresa, Integer idVaga)
    List<Vaga> listarVagaEmpresa(Integer idEmpresa)
    Vaga buscarIdVagaEmpresa(Integer idEmpresa, Integer idVaga)
    Vaga buscarNomeVagaEmpresa(Integer idEmpresa, String nome)
    Vaga buscarNomeVaga(String nome)
}