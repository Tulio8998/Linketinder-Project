package com.zg.linketinder.service

import com.zg.linketinder.dao.CandidatoDAO
import com.zg.linketinder.dao.CandidatoDAOImpl
import com.zg.linketinder.dao.CompetenciaDAO
import com.zg.linketinder.dao.CompetenciaDAOImpl
import com.zg.linketinder.model.Competencia

class CompetenciaService {
    private CompetenciaDAO competenciaDAO = new CompetenciaDAOImpl()
    private CandidatoDAO candidatoDAO = new CandidatoDAOImpl()

    void adicionarCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        try {
            if (competenciaDAO.buscarIdCompetenciaCandidato(id_candidato, id_competencia) != null) {
                throw new IllegalArgumentException("O candidato ja possui esta competencia")
            }
            if (candidatoDAO.buscarIdCandidato(id_candidato) && competenciaDAO.buscarIdCompetencia(id_competencia)) {
                competenciaDAO.salvaCompetenciaCandidato(id_candidato, id_competencia)
            } else {
                throw new IllegalArgumentException("Candidato ou Competencia nao encontrados no sistema")
            }
        } catch(Exception e) {
            throw new IllegalArgumentException("Erro ao vincular competencia ao candidato: ${e.message}")
        }
    }

    void excluirCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        try {
            if (candidatoDAO.buscarIdCandidato(id_candidato) && competenciaDAO.buscarIdCompetencia(id_competencia)) {
                competenciaDAO.excluirCompetenciaCandidato(id_candidato, id_competencia)
            } else {
                throw new IllegalArgumentException("Candidato ou Competencia nao encontrados no sistema")
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao remover competencia do candidato: ${e.message}")
        }
    }

    List<Competencia> listarCompetenciasCandidato(Integer id_candidato) {
        try {
            return competenciaDAO.listarCompetenciasCandidato(id_candidato)
        } catch (Exception e) {
            println "Erro ao listar competencias do candidato: ${e.message}"
            return []
        }
    }

    Competencia buscarIdCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        try {
            return competenciaDAO.buscarIdCompetenciaCandidato(id_candidato, id_competencia)
        } catch (Exception e) {
            println "Erro ao buscar competencia do candidato por ID: ${e.message}"
            return null
        }
    }

    Competencia buscarNomeCompetenciaCandidato(Integer id_candidato, String nome) {
        try {
            return competenciaDAO.buscarNomeCompetenciaCandidato(id_candidato, nome)
        } catch (Exception e) {
            println "Erro ao buscar competencia do candidato por Nome: ${e.message}"
            return null
        }
    }

    void adicionarCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        try {
            if (competenciaDAO.buscarIdCompetenciaVaga(id_vaga, id_competencia) != null) {
                throw new IllegalArgumentException("Esta vaga ja possui esta competencia")
            }
            competenciaDAO.salvaCompetenciaVaga(id_vaga, id_competencia)
        } catch(Exception e) {
            throw new IllegalArgumentException("Erro ao vincular competencia a vaga. Verifique se a vaga e a competencia existem: ${e.message}")
        }
    }

    void excluirCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        try {
            competenciaDAO.excluirCompetenciaVaga(id_vaga, id_competencia)
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao remover competencia da vaga: ${e.message}")
        }
    }

    List<Competencia> listarCompetenciasVaga(Integer id_vaga) {
        try {
            return competenciaDAO.listarCompetenciasVaga(id_vaga)
        } catch (Exception e) {
            println "Erro ao listar competencias da vaga: ${e.message}"
            return []
        }
    }

    Competencia buscarIdCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        try {
            return competenciaDAO.buscarIdCompetenciaVaga(id_vaga, id_competencia)
        } catch (Exception e) {
            println "Erro ao buscar competencia da vaga por ID: ${e.message}"
            return null
        }
    }

    Competencia buscarNomeCompetenciaVaga(Integer id_vaga, String nome) {
        try {
            return competenciaDAO.buscarNomeCompetenciaVaga(id_vaga, nome)
        } catch (Exception e) {
            println "Erro ao buscar competencia da vaga por Nome: ${e.message}"
            return null
        }
    }

    List<Competencia> listarCompetencia() {
        try {
            return  competenciaDAO.listarCompetencia()
        } catch (Exception e) {
            println "Erro ao listar competencias: ${e.message}"
            return []
        }
    }
}