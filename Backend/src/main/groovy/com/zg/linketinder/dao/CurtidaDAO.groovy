package com.zg.linketinder.dao

import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.curtida.Curtida
import com.zg.linketinder.model.curtida.Match

interface CurtidaDAO {
    void salvarCurtidaCandidato(Integer idCandidato, Integer idVaga, Boolean like)
    void salvarCurtidaEmpresa(Integer idEmpresa, Integer idCandidato, Integer idVaga, Boolean like)
    List<CurtidaEmpresa> listarCurtidasEmpresa()
    List<CurtidaCandidato> listarCurtidasCandidato()
    List<Match> listarMatch()
}