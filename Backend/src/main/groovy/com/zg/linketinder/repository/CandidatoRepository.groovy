package com.zg.linketinder.repository

import com.zg.linketinder.model.Candidato

class CandidatoRepository {
    List<Candidato> candidatos = []

    CandidatoRepository() {
        candidatos.add(new Candidato(
                cpf: "000.000.000-00",
                idade: 21,
                nome: "Túlio",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Desenvolvedor",
        ))
        candidatos.add(new Candidato(
                cpf: "111.111.111-11",
                idade: 21,
                nome: "Kratos",
                email: "teste@teste.com",
                estado: "MG",
                cep: "11111-111",
                descricao: "Desenvolvedor",
        ))
        candidatos.add(new Candidato(
                cpf: "222.222.222-22",
                idade: 21,
                nome: "Geraldao",
                email: "teste@teste.com",
                estado: "MG",
                cep: "22222-222",
                descricao: "Desenvolvedor",
        ))
        candidatos.add(new Candidato(
                cpf: "333.333.333-33",
                idade: 21,
                nome: "Cristiano",
                email: "teste@teste.com",
                estado: "MG",
                cep: "33333-333",
                descricao: "Desenvolvedor",
        ))
        candidatos.add(new Candidato(
                cpf: "444.444.444-44",
                idade: 21,
                nome: "Ronaldo",
                email: "teste@teste.com",
                estado: "MG",
                cep: "44444-444",
                descricao: "Desenvolvedor",
        ))
    }

    def adicionarCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    def listarTodos() {
        return candidatos
    }
}
