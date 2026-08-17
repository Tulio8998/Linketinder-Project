package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.VagaRepository

class VagaService {
    EmpresaService empresaService
    VagaRepository vagaRepository

    VagaService(EmpresaService empresaServicee) {
        this.empresaService = empresaServicee
        this.vagaRepository = new VagaRepository(empresaServicee.listarTodos())
    }


    def listarTodos() {
        return vagaRepository.listarTodos()
    }

    def adicionarVaga(Vaga vaga) {
        vagaRepository.adicionarVaga(vaga)
    }
}
