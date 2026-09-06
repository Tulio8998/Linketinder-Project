package com.zg.linketinder.repository


import com.zg.linketinder.model.Curtida

class CurtidaRepository {
    List<Curtida> curtidas = []

    void adicionarCurtida(Curtida curtida) {
        curtidas.add(curtida)
    }


    List<Curtida> listarTodos() {
        return curtidas
    }
}
