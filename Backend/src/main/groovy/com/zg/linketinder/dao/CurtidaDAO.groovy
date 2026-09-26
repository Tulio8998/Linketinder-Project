package com.zg.linketinder.dao

import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.curtida.Curtida
import com.zg.linketinder.model.curtida.Match

interface CurtidaDAO {
    void salvarCurtidaCandidato(Integer id_candidato, Integer id_vaga, Boolean like)
    void salvarCurtidaEmpresa(Integer id_empresa, Integer id_candidato, Integer id_vaga, Boolean like)
    List<CurtidaEmpresa> listarCurtidasEmpresa()
    List<CurtidaCandidato> listarCurtidasCandidato()
    List<Match> listarMatchEmpresa(Integer id_empresa)
    List<Match> listarMatchCandidato(Integer id_empresa)
}