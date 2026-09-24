package com.zg.linketinder.dao

import com.zg.linketinder.model.Vaga

interface VagaDAO {
    void salvarVaga(Vaga vaga)
    void atualizarVaga(Vaga vaga)
    void excluirVaga(Integer id_empresa, Integer id_vaga)
    List<Vaga> listarVaga(Integer id_empresa)
    Vaga buscarIdVaga(Integer id_empresa, Integer id_vaga)
    Vaga buscarNomeVaga(Integer id_empresa, String nome)
}