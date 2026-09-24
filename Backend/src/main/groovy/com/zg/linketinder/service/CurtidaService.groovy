package com.zg.linketinder.service

import com.zg.linketinder.dao.CurtidaDAO
import com.zg.linketinder.dao.CurtidaDAOImpl
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.curtida.Match

class CurtidaService {
    private CurtidaDAO curtidaDAO = new CurtidaDAOImpl()

    void curtirVagaComoCandidato(Candidato candidato, Vaga vaga, Boolean like) {
        try {
            if (candidato && vaga) {
                curtidaDAO.salvarCurtidaCandidato(candidato.id, vaga.id, like)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Candidato e Vaga nao podem ser nulos: ${e.message}")
        }
    }

    void curtirVagaComoEmpresa(Empresa empresa, Candidato candidato, Vaga vaga, Boolean like) {
        try {
            if (empresa?.id == vaga?.empresa?.id) {
                curtidaDAO.salvarCurtidaEmpresa(empresa.id, candidato.id, vaga.id, like)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("A empresa so pode curtir candidatos das suas proprias vagas: ${e.message}")
        }
    }

    int calcularAfinidade(Candidato candidato, Vaga vaga) {
        if (!candidato || !vaga) {
           throw new IllegalArgumentException("Candidato e Vaga nao podem ser nulos")
        }
        if (!vaga.competencias || vaga.competencias.isEmpty()) {
            return 0
        }
        def compIguais = vaga.competencias.findAll { c -> candidato.competencias.contains(c)}
        def qntCompVaga = vaga.competencias.size()
        def qntCompTotal = compIguais.size()

        return (qntCompTotal * 100) / qntCompVaga
    }

    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        try {
            return curtidaDAO.listarCurtidasEmpresa()
        } catch (Exception e) {
            println "Nao tem curtidas disponiveis: ${e.message}"
            return []
        }
    }

    List<CurtidaCandidato> listarCurtidasCandidato() {
        try {
            return curtidaDAO.listarCurtidasCandidato()
        } catch (Exception e) {
            println "Nao tem curtidas disponiveis: ${e.message}"
            return []
        }
    }

    List<Match> listarMatch() {
        try {
            return curtidaDAO.listarMatch()
        } catch (Exception e) {
            println "Nao tem matches: ${e.message}"
            return []
        }
    }


}
