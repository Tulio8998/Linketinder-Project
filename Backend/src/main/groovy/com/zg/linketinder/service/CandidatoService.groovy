package com.zg.linketinder.service

import com.zg.linketinder.dao.CandidatoDAO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.repository.CandidatoRepository

class CandidatoService {
    private CandidatoDAO candidatoDAO = new CandidatoDAO()

    def listarCandidatos() {
        try {
            return candidatoDAO.listarCandidato()
        } catch (Exception e) {
            println "Nao tem candidatos para listar: ${e.message}"
            return []
        }
    }

    def buscarIdCandidato(Candidato candidato) {
        try {
            return candidatoDAO.buscarIdCandidato(candidato.id)
        } catch (Exception e) {
            println "Nao tem candidatos para listar: ${e.message}"
            return []
        }
    }

    void salvarCandidato(Candidato candidato) {
        try {
            if (validarCandidato(candidato)) {
                candidatoDAO.salvarCandidato(candidato)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar candidato: ${e.message}")
        }
    }

    boolean validarCandidato(Candidato candidato) {
        if (!candidato) {
            throw new IllegalArgumentException("O candidato nao pode ser nulo")
        }
        validarCpf(candidato)
        validarEmail(candidato)
        return true
    }

    void validarCpf(Candidato candidato) {
        def cpfExist = candidatoDAO.listarCandidato().find{
            it.cpf == candidato.cpf
        }
        if (cpfExist) {
            throw new IllegalArgumentException("Cpf ja cadastrado")
        }
    }

    void validarEmail(Candidato candidato) {
        def emailExist = candidatoDAO.listarCandidato().find {
            it.email == candidato.email
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }

    void atualizarCandidato(Candidato candidato) {
        try {
            if (validarCandidato(candidato)) {
                candidatoDAO.atualizarCandidato(candidato)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar candidato: ${e.message}")
        }
    }

    void excluirCandidato(Candidato candidato) {
        try {
            if (candidato && candidatoDAO.buscarIdCandidato(candidato.id)) {
                candidatoDAO.excluirCandidato(candidato.id)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("O candidato nao pode ser nulo e deve ter um id valido: ${e.message}")
        }
    }
}