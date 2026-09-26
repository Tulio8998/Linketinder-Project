package com.zg.linketinder.dao

import com.zg.linketinder.model.Competencia

interface CompetenciaDAO {
    List<Competencia> listarCompetencia()
    Competencia buscarIdCompetencia(Integer id)
    Competencia buscarNomeCompetencia(String nome)

    void salvaCompetenciaCandidato(Integer id_candidato, Integer id_competencia)
    void excluirCompetenciaCandidato(Integer id_candidato, Integer id_competencia)
    List<Competencia> listarCompetenciasCandidato(Integer id_candidato)
    Competencia buscarIdCompetenciaCandidato(Integer id_candidato, Integer id_competencia)
    Competencia buscarNomeCompetenciaCandidato(Integer id_candidato, String nome)

    void salvaCompetenciaVaga(Integer id_vaga, Integer id_competencia)
    void excluirCompetenciaVaga(Integer id_vaga, Integer id_competencia)
    List<Competencia> listarCompetenciasVaga(Integer id_vaga)
    Competencia buscarIdCompetenciaVaga(Integer id_vaga, Integer id_competencia)
    Competencia buscarNomeCompetenciaVaga(Integer id_vaga, String nome)
}