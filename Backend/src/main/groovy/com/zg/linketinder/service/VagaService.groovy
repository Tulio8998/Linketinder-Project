package com.zg.linketinder.service

import com.zg.linketinder.dao.VagaDAO
import com.zg.linketinder.dao.VagaDAOImpl
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.VagaRepository

class VagaService {
    private VagaDAO vagaDAO = new VagaDAOImpl()

    void salvarVaga(Vaga vaga) {
        try {
            if (vaga != null) {
                vagaDAO.salvarVaga(vaga)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar vaga: ${e.message}")
        }
    }

    void atualizarVaga(Integer id_empresa, Vaga vaga) {
        try {
            if (vaga != null && vagaDAO.buscarIdVaga(id_empresa, vaga.id) != null) {
                vagaDAO.atualizarVaga(vaga)
            } else {
                throw new IllegalArgumentException("Vaga nao encontrado ou dados nulos")
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar vaga: ${e.message}")
        }
    }

    void excluirVaga(Integer id_empresa, Integer id_vaga) {
        try {
            if (vagaDAO.buscarIdVaga(id_empresa, id_vaga)) {
                vagaDAO.excluirVaga(id_empresa, id_vaga)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("A vaga nao pode ser nulo e deve ter um id valido: ${e.message}")
        }
    }

    List<Vaga> listarVaga(Integer id_empresa) {
        try {
            return vagaDAO.listarVaga(id_empresa)
        } catch (Exception e) {
            println "Nao tem vaga para listar: ${e.message}"
            return []
        }
    }

    Vaga buscarIdVaga(Integer id_empresa, Integer id_vaga) {
        try {
            return vagaDAO.buscarIdVaga(id_empresa, id_vaga)
        } catch (Exception e) {
            println "Nao tem vaga com esse id: ${e.message}"
            return null
        }
    }

    Vaga buscarNomeVaga(Integer id_empresa, String nome) {
        try {
            return vagaDAO.buscarNomeVaga(id_empresa, nome)
        } catch (Exception e) {
            println "Nao tem vaga com esse nome: ${e.message}"
            return null
        }
    }
}
