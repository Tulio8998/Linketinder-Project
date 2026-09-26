package com.zg.linketinder.dao

import com.zg.linketinder.model.Vaga

interface VagaDAO {
    void salvarVaga(Vaga vaga)
    void atualizarVaga(Vaga vaga)
    void excluirVaga(Integer id_empresa, Integer id_vaga)
    List<Vaga> listarVagaEmpresa(Integer id_empresa)
    List<Vaga> listarTodasVagas()
    Vaga buscarIdVagaEmpresa(Integer id_empresa, Integer id_vaga)
    Vaga buscarNomeVagaEmpresa(Integer id_empresa, String nome)
    Vaga buscarIdVaga(Integer id_vaga)
    Vaga buscarNomeVaga(String nome)
}