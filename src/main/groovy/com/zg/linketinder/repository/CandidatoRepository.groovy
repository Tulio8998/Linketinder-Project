package com.zg.linketinder.repository

import com.zg.linketinder.model.Candidato

class CandidatoRepository {
    List<Candidato> candidatos = []

    def adicionarCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    def listarTodos() {
        return candidatos
    }
}
