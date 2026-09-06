package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Curtida
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.CurtidaRepository

class CurtidaService {
    CurtidaRepository curtidaRepository = new CurtidaRepository()

    List<Curtida> listarTodos() {
        return curtidaRepository.listarTodos()
    }

    List<Curtida> listarCurtidasDeVolta() {
        return curtidaRepository.listarTodos().findAll({
            it.curtiuDeVolta
        })
    }

    void curtirVaga(Candidato candidato, Vaga vaga) {
        curtidaRepository.adicionarCurtida(new Curtida(
                candidato: candidato,
                vaga: vaga,
                curtiuDeVolta: false
        ))
    }

    void curtirCandidato(Empresa empresa, Candidato candidato) {
        Curtida curtida = curtidaRepository.listarTodos().find {
            it.candidato == candidato && it.vaga.empresa == empresa
        }
        if (curtida) {
            curtida.curtiuDeVolta = true
        }
    }
}
