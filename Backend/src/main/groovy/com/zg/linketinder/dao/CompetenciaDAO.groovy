package com.zg.linketinder.dao

import com.zg.linketinder.model.Competencia

interface CompetenciaDAO {
    void salvaCompetencia(Competencia competencia)
    void excluirCompetencia(Integer id)
    List<Competencia> listarCompetencia()
    Competencia buscarIdCompetencia(Integer id)
    Competencia buscarNomeCompetencia(String nome)
}