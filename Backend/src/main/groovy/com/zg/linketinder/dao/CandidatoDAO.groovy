package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

interface CandidatoDAO {
    void salvarCandidato(Candidato candidato)
    void atualizarCandidato(Candidato candidato)
    void excluirCandidato(Integer id)
    List<Candidato> listarCandidatoEmpresa(Integer id_empresa)
    List<Candidato> listarCandidato()
    Candidato buscarIdCandidato(Integer id)
    Candidato buscarEmailCandidato(String email)
    Candidato buscarCpfCandidato(String cpf)
}
