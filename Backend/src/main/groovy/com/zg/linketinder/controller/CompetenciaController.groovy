package com.zg.linketinder.controller

import com.zg.linketinder.model.Competencia
import com.zg.linketinder.service.CompetenciaService

class CompetenciaController {
    CompetenciaService competenciaService = new CompetenciaService()

    List<Competencia> listarTodas() {
        return competenciaService.listarTodos()
    }

    // Ações para Candidato
    String adicionarCompetenciaCandidato(Integer idCandidato, Integer idCompetencia) {
        try {
            competenciaService.adicionarCompetenciaCandidato(idCandidato, idCompetencia)
            return "Competência adicionada ao candidato com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro: ${e.message}"
        }
    }

    String excluirCompetenciaCandidato(Integer idCandidato, Integer idCompetencia) {
        try {
            competenciaService.excluirCompetenciaCandidato(idCandidato, idCompetencia)
            return "Competência removida do candidato com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro: ${e.message}"
        }
    }

    List<Competencia> listarCompetenciasCandidato(Integer idCandidato) {
        return competenciaService.listarCompetenciasCandidato(idCandidato)
    }

    Competencia buscarIdCompetenciaCandidato(Integer idCandidato, Integer idCompetencia) {
        return competenciaService.buscarIdCompetenciaCandidato(idCandidato, idCompetencia)
    }

    Competencia buscarNomeCompetenciaCandidato(Integer idCandidato, String nome) {
        return competenciaService.buscarNomeCompetenciaCandidato(idCandidato, nome)
    }

    // Ações para Vaga
    String adicionarCompetenciaVaga(Integer idVaga, Integer idCompetencia) {
        try {
            competenciaService.adicionarCompetenciaVaga(idVaga, idCompetencia)
            return "Competência adicionada à vaga com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro: ${e.message}"
        }
    }

    String excluirCompetenciaVaga(Integer idVaga, Integer idCompetencia) {
        try {
            competenciaService.excluirCompetenciaVaga(idVaga, idCompetencia)
            return "Competência removida da vaga com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro: ${e.message}"
        }
    }

    List<Competencia> listarCompetenciasVaga(Integer idVaga) {
        return competenciaService.listarCompetenciasVaga(idVaga)
    }

    Competencia buscarIdCompetenciaVaga(Integer idVaga, Integer idCompetencia) {
        return competenciaService.buscarIdCompetenciaVaga(idVaga, idCompetencia)
    }

    Competencia buscarNomeCompetenciaVaga(Integer idVaga, String nome) {
        return competenciaService.buscarNomeCompetenciaVaga(idVaga, nome)
    }
}