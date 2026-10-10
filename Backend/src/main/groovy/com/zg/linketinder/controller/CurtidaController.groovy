package com.zg.linketinder.controller

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.CurtidaEmpresa
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CurtidaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService

class CurtidaController {
    CurtidaService curtidaService = new CurtidaService()
    CandidatoService candidatoService = new CandidatoService()
    EmpresaService empresaService = new EmpresaService()
    VagaService vagaService = new VagaService()

    String curtirVagaComoCandidato(Integer idCandidato, Integer idVaga) {
        try {
            curtidaService.curtirVagaComoCandidato(idCandidato, idVaga, true)
            return "Vaga curtida com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro ao curtir: ${e.message}"
        }
    }

    String curtirCandidatoComoEmpresa(Integer idEmpresa, Integer idCandidato, Integer idVaga) {
        try {
            Empresa empresa = empresaService.buscarPorId(idEmpresa)
            Candidato candidato = candidatoService.buscarPorId(idCandidato)
            Vaga vaga = vagaService.buscarPorId(idVaga)

            if (!empresa || !candidato || !vaga) {
                return "Erro: Empresa, Candidato ou Vaga não encontrados."
            }

            curtidaService.curtirVagaComoEmpresa(empresa, candidato, vaga, true)
            return "Candidato curtido com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro ao curtir: ${e.message}"
        }
    }

    String calcularAfinidade(Integer idCandidato, Integer idVaga) {
        try {
            Candidato candidato = candidatoService.buscarPorId(idCandidato)
            Vaga vaga = vagaService.buscarPorId(idVaga)

            if (!candidato || !vaga) {
                return "Erro: Candidato ou Vaga não encontrados."
            }

            int afinidade = curtidaService.calcularAfinidade(candidato, vaga)
            return "O(a) candidato(a) ${candidato.nome} tem ${afinidade}% de afinidade com a vaga ${vaga.nome}."
        } catch (IllegalArgumentException e) {
            return "Erro ao calcular afinidade: ${e.message}"
        }
    }

    List<CurtidaCandidato> listarCurtidasCandidato() {
        return curtidaService.listarCurtidasCandidato()
    }

    List<CurtidaEmpresa> listarCurtidasEmpresa() {
        return curtidaService.listarCurtidasEmpresa()
    }

    List<Match> listarMatchesCandidato(Integer idCandidato) {
        return curtidaService.listarMatchCandidato(idCandidato)
    }

    List<Match> listarMatchesEmpresa(Integer idEmpresa) {
        return curtidaService.listarMatchEmpresa(idEmpresa)
    }
}