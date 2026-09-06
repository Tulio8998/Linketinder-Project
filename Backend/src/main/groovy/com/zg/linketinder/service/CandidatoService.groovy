package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.repository.CandidatoRepository

class CandidatoService {
    CandidatoRepository candidatoRepository = new CandidatoRepository()

    List<Candidato> listarTodos() {
        return candidatoRepository.listarTodos()
    }

    void adicionarCandidato(Candidato candidato) {
        validarCandidato(candidato)
        candidatoRepository.adicionarCandidato(candidato)
    }

    void validarCandidato(Candidato candidato) {
        if (candidato == null) {
            throw new IllegalArgumentException("O candidato nao pode ser nulo")
        }
        validarCpf(candidato)
        validarEmail(candidato)
    }

    void validarCpf(Candidato candidato) {
        def cpfExist = candidatoRepository.listarTodos().find{
            it.cpf == candidato.cpf
        }
        if (cpfExist) {
            throw new IllegalArgumentException("Cpf ja cadastrado")
        }
    }

    void validarEmail(Candidato candidato) {
        def emailExist = candidatoRepository.listarTodos().find {
            it.email == candidato.email
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }
}