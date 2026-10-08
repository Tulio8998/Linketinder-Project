package com.zg.linketinder.service

import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.dao.CandidatoDAO
import com.zg.linketinder.dao.CandidatoDAOImpl
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.service.base.CrudService

class CandidatoService extends CrudService<Candidato> {

    private CandidatoDAO candidatoDAO = new CandidatoDAOImpl()

    @Override
    protected CrudDAO<Candidato> getDao() { return candidatoDAO }

    @Override
    protected void validarAntesSalvar(Candidato candidato) {
        if (candidatoDAO.buscarCpfCandidato(candidato.cpf)) throw new IllegalArgumentException("CPF ja cadastrado")
        if (candidatoDAO.buscarEmailCandidato(candidato.email)) throw new IllegalArgumentException("Email ja cadastrado")
    }

    List<Candidato> listarCandidatoEmpresa(Integer id_empresa) {
        try { return candidatoDAO.listarCandidatoEmpresa(id_empresa) }
        catch (Exception e) { println "Erro ao listar: ${e.message}"; return [] }
    }

    Candidato buscarCpfCandidato(String cpf) {
        try { return candidatoDAO.buscarCpfCandidato(cpf) }
        catch (Exception e) { println "Nao tem candidato com esse cpf: ${e.message}"; return null }
    }

    Candidato buscarEmailCandidato(String email) {
        try { return candidatoDAO.buscarEmailCandidato(email) }
        catch (Exception e) { println "Nao tem candidato com esse email: ${e.message}"; return null }
    }
}