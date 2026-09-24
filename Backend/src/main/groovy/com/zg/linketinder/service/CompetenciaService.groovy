package com.zg.linketinder.service

import com.zg.linketinder.dao.CompetenciaDAO
import com.zg.linketinder.dao.CompetenciaDAOImpl
import com.zg.linketinder.model.Competencia

class CompetenciaService {
    CompetenciaDAO competenciaDAO = new CompetenciaDAOImpl()

    void salvaCompetencia(Competencia competencia) {
        try {
            if (competencia != null) {
                competenciaDAO.salvaCompetencia(competencia)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar comptencia: ${e.message}")
        }
    }

    void excluirCompetencia(Integer id) {
        try {
            if (competenciaDAO.buscarIdCompetencia(id)) {
                competenciaDAO.excluirCompetencia(id)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("A competencia nao pode ser nulo e deve ter um id valido: ${e.message}")
        }
    }

    List<Competencia> listarCompetencia() {
        try {
            return competenciaDAO.listarCompetencia()
        } catch (Exception e) {
            println "Nao tem competencias para listar: ${e.message}"
            return []
        }
    }

    Competencia buscarIdCompetencia(Integer id) {
        try {
            return competenciaDAO.buscarIdCompetencia(id)
        } catch (Exception e) {
            println "Nao tem competencia com esse id: ${e.message}"
            return null
        }
    }

    Competencia buscarNomeCompetencia(String nome) {
        try {
            return competenciaDAO.buscarNomeCompetencia(nome)
        } catch (Exception e) {
            println "Nao tem candidato com esse nome: ${e.message}"
            return null
        }
    }
}
