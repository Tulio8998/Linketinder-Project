package com.zg.linketinder.service

import com.zg.linketinder.dao.CurtidaDAO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Curtida
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.CurtidaRepository

class CurtidaService {
    private CurtidaDAO curtidaDAO = new CurtidaDAO()

    void curtirVagaComoCandidato(Candidato candidato, Vaga vaga, Boolean like) {
        if (!candidato || !vaga) {
            throw new IllegalArgumentException("Candidato e Vaga nao podem ser nulos")
        }
        curtidaDAO.salvarCurtidaCandidato(candidato.id, vaga.id, like)
    }

    void curtirVagaComoEmpresa(Empresa empresa, Candidato candidato, Vaga vaga, Boolean like) {
        if (empresa.id != vaga.empresa.id) {
            throw new IllegalArgumentException("A empresa so pode curtir candidatos das suas proprias vagas")
        }
        curtidaDAO.salvarCurtidaEmpresa(empresa.id, candidato.id, vaga.id, like)
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

    def listarCurtidasEmpresa() {
        try {
            curtidaDAO.listarCurtidasEmpresa()
        } catch (Exception e) {
            println "Não tem curtidas disponíveis: ${e.message}"
            return []
        }
    }

    def listarMatch() {
        try {
            curtidaDAO.listarMatch()
        } catch (Exception e) {
            println "Não tem matches: ${e.message}"
            return []
        }
    }


}
