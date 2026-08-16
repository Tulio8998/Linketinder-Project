package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.repository.VagaRepository

class VagaService {
    EmpresaService empresaService
    VagaRepository vagaRepository = new VagaRepository()

    VagaService(EmpresaService empresaService) {
        this.empresaService = empresaService
        vagaRepository.adicionarVaga( new Vaga(
                nome: "Back",
                descricao: "Desenvolver",
                estado: "MG",
                cidade: "Belo Horizonte",
                competencias: ["Desenvolvimento agil", "DevOps", "Python", "Linux", "Java", "Groovy"],
                empresa: empresaService.listarTodos().get(0)
        ))

        vagaRepository.adicionarVaga( new Vaga(
                nome: "DevOps",
                descricao: "Desenvolver",
                estado: "MG",
                cidade: "Belo Horizonte",
                competencias: ["TypeScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                empresa: empresaService.listarTodos().get(1)
        ))

        vagaRepository.adicionarVaga( new Vaga(
                nome: "Fullstack",
                descricao: "Desenvolver",
                estado: "MG",
                cidade: "Belo Horizonte",
                competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                empresa: empresaService.listarTodos().get(2)
        ))

        vagaRepository.adicionarVaga( new Vaga(
                nome: "Data Analytic",
                descricao: "Desenvolver",
                estado: "MG",
                cidade: "Belo Horizonte",
                competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                empresa: empresaService.listarTodos().get(3)
        ))

        vagaRepository.adicionarVaga( new Vaga(
                nome: "Front",
                descricao: "Desenvolver",
                estado: "MG",
                cidade: "Belo Horizonte",
                competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                empresa: empresaService.listarTodos().get(4)
        ))
    }

    def listarTodos() {
        return vagaRepository.listarTodos()
    }

    def adicionarVaga(Vaga vaga) {
        vagaRepository.adicionarVaga(vaga)
    }
}
