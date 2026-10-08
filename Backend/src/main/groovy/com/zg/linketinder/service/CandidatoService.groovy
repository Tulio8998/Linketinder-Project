package com.zg.linketinder.service

import com.zg.linketinder.dao.CandidatoDAO
import com.zg.linketinder.dao.CandidatoDAOImpl
import com.zg.linketinder.model.Candidato

class CandidatoService {
    private CandidatoDAO candidatoDAO = new CandidatoDAOImpl()

    List<Candidato> listarCandidatoEmpresa(Integer id_empresa) {
        try {
            return candidatoDAO.listarCandidatoEmpresa(id_empresa)
        } catch (Exception e) {
            println "Nao tem candidatos para listar: ${e.message}"
            return []
        }
    }

    List<Candidato> listarCandidatos() {
        try {
            return candidatoDAO.listarTodos()
        } catch (Exception e) {
            println "Nao tem candidatos para listar: ${e.message}"
            return []
        }
    }

    Candidato buscarIdCandidato(Integer id) {
        try {
            return candidatoDAO.buscarPorId(id)
        } catch (Exception e) {
            println "Nao tem candidatos com esse id: ${e.message}"
            return null
        }
    }

    Candidato buscarCpfCandidato(String cpf) {
        try {
            return candidatoDAO.buscarCpfCandidato(cpf)
        } catch (Exception e) {
            println "Nao tem candidato com esse cpf: ${e.message}"
            return null
        }
    }

    Candidato buscarEmailCandidato(String email) {
        try {
            return candidatoDAO.buscarEmailCandidato(email)
        } catch (Exception e) {
            println "Nao tem candidato com esse email: ${e.message}"
            return null
        }
    }

    void salvarCandidato(Candidato candidato) {
        try {
            if (validarCandidato(candidato)) {
                candidatoDAO.salvar(candidato)
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
        Candidato cpfExist = candidatoDAO.buscarCpfCandidato(candidato.cpf)
        if (cpfExist) {
            throw new IllegalArgumentException("Cpf ja cadastrado")
        }
    }

    void validarEmail(Candidato candidato) {
        Candidato emailExist = candidatoDAO.buscarEmailCandidato(candidato.email)
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }

    void atualizarCandidato(Integer id, Candidato candidato) {
        try {
            if (candidato != null && candidatoDAO.buscarPorId(id) != null) {
                candidato.id = id
                candidatoDAO.atualizar(id, candidato)
            } else {
                throw new IllegalArgumentException("Candidato nao encontrado ou dados nulos")
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar candidato: ${e.message}")
        }
    }

    void excluirCandidato(Integer id) {
        try {
            if (candidatoDAO.buscarPorId(id)) {
                candidatoDAO.excluir(id)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("O candidato nao pode ser nulo e deve ter um id valido: ${e.message}")
        }
    }
}