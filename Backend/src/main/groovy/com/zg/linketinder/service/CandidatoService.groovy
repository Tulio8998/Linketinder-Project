package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.repository.CandidatoRepository

class CandidatoService {
    CandidatoRepository candidatoRepository = new CandidatoRepository()

    def listarTodos() {
        return candidatoRepository.listarTodos()
    }

    def adicionarCandidato(Candidato candidato) {
        validarCandidato(candidato)
        candidatoRepository.adicionarCandidato(candidato)
    }

    def validarCandidato(Candidato candidato) {
        if (candidato == null) {
            throw new IllegalArgumentException("O candidato nao pode ser nulo")
        }
        validarCpf(candidato)
        validarEmail(candidato)
    }

    def validarCpf(Candidato candidato) {
        def cpfExist = candidatoRepository.listarTodos().find{
            it.cpf == candidato.cpf
        }
        if (cpfExist) {
            throw new IllegalArgumentException("Cpf ja cadastrado")
        }
    }

    def validarEmail(Candidato candidato) {
        def emailExist = candidatoRepository.listarTodos().find {
            it.email == candidato.email
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }
}