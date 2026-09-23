package com.zg.linketinder.dao

import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

interface CurtidaDAO {
    void salvarCurtidaCandidato(Integer idCandidato, Integer idVaga, Boolean like)
    void salvarCurtidaEmpresa(Integer idEmpresa, Integer idCandidato, Integer idVaga, Boolean like)
    List<Map> listarCurtidasEmpresa()
    List<Map> listarMatch()
}