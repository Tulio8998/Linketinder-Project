package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.model.Candidato

interface CandidatoDAO extends CrudDAO<Candidato> {
    List<Candidato> listarCandidatoEmpresa(Integer id_empresa)
    Candidato buscarEmailCandidato(String email)
    Candidato buscarCpfCandidato(String cpf)
}
