package com.zg.linketinder.repository

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga

class VagaRepository {
    List<Vaga> vagas = []

    VagaRepository(List<Empresa> empresas) {
        if (empresas.size() >= 5) {
            vagas.add( new Vaga(
                    nome: "Back",
                    descricao: "Desenvolver",
                    estado: "MG",
                    cidade: "Belo Horizonte",
                    competencias: ["Desenvolvimento agil", "DevOps", "Python", "Linux", "Java", "Groovy"],
                    empresa: empresas.get(0)
            ))

            vagas.add( new Vaga(
                    nome: "DevOps",
                    descricao: "Desenvolver",
                    estado: "MG",
                    cidade: "Belo Horizonte",
                    competencias: ["TypeScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                    empresa: empresas.get(1)
            ))

            vagas.add( new Vaga(
                    nome: "Fullstack",
                    descricao: "Desenvolver",
                    estado: "MG",
                    cidade: "Belo Horizonte",
                    competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                    empresa: empresas.get(2)
            ))

            vagas.add( new Vaga(
                    nome: "Data Analytic",
                    descricao: "Desenvolver",
                    estado: "MG",
                    cidade: "Belo Horizonte",
                    competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                    empresa: empresas.get(3)
            ))

            vagas.add( new Vaga(
                    nome: "Front",
                    descricao: "Desenvolver",
                    estado: "MG",
                    cidade: "Belo Horizonte",
                    competencias: ["JavaScript", "DevOps", "Python", "Linux", "Java", "Groovy"],
                    empresa: empresas.get(4)
            ))
        }
    }

    def adicionarVaga(Vaga vaga) {
        vagas.add(vaga)
    }

    def listarTodos(){
        return vagas
    }
}
