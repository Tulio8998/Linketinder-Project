package com.zg.linketinder.repository

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga

class VagaRepository {
    List<Vaga> vagas = []

    def adicionarVaga(Vaga vaga) {
        vagas.add(vaga)
    }

    def listarTodos() {
        return vagas
    }
}
