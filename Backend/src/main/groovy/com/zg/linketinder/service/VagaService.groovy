package com.zg.linketinder.service


import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.VagaRepository

class VagaService {
    EmpresaService empresaService
    VagaRepository vagaRepository

    VagaService(EmpresaService empresaServicee) {
        this.empresaService = empresaServicee
        this.vagaRepository = new VagaRepository(empresaServicee.listarTodos())
    }


    List<Vaga> listarTodos() {
        return vagaRepository.listarTodos()
    }

    void adicionarVaga(Vaga vaga) {
        vagaRepository.adicionarVaga(vaga)
    }
}
