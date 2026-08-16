package com.zg.linketinder.repository

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Curtida
import com.zg.linketinder.model.Vaga

class CurtidaRepository {
    List<Curtida> curtidas = []

    def adicionarCurtida(Curtida curtida) {
        curtidas.add(curtida)
    }


    def listarTodos() {
        return curtidas
    }
}
