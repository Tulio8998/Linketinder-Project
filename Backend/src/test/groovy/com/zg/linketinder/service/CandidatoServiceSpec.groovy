package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.repository.CandidatoRepository
import spock.lang.Specification

class CandidatoServiceSpec extends Specification {
    def service = new CandidatoService()

    def "deve listar todos os candidatos"() {
        given: "repository mockado de candidato"
        CandidatoRepository repositoryMock = Mock(CandidatoRepository)
        service.candidatoRepository = repositoryMock

        def candidatos = [
                new Candidato(nome: "Candidato 1"),
                new Candidato(nome: "Candidato 2")
        ]

        and: "o repository deve retornar uma lista de candidatos"
        repositoryMock.listarTodos() >> candidatos

        when: "a lista de candidatos solicitada"
        def resultado = service.listarTodos()

        then: "o service deve retornar a lista fornecida pelo repository"
        resultado == candidatos
    }

    def "deve adicionar um novo candidato"() {
        given: "repository mockado de candidato"
        CandidatoRepository repositoryMock = Mock(CandidatoRepository)
        service.candidatoRepository = repositoryMock

        and: "um novo candidato"
        def candidato = new Candidato(
                cpf: "000.000.000-00",
                idade: 21,
                nome: "Túlio",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Desenvolvedor"
        )

        when: "o metodo de adicionar for chamado"
        service.adicionarCandidato(candidato)

        then: "o repository deve receber um candidato"
        1 * repositoryMock.adicionarCandidato(candidato)
    }

    def "nao deve permitir insercao de candidato nulo"() {
        when: "é tentado inserir um valor nulo em vez candidato"
        service.adicionarCandidato(null)

        then: "deve lancar um excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "O candidato nao pode ser nulo"
    }

    def "nao deve permitir cpf duplicado"() {
        given: "repository mockado de candidato"
        CandidatoRepository repositoryMock = Mock(CandidatoRepository)
        service.candidatoRepository = repositoryMock

        def candidatos = [
                new Candidato(cpf: "666.666.666-66",),
        ]

        and: "o repository deve retornar candidatos cadastradas"
        repositoryMock.listarTodos() >> candidatos

        and: "uma nova candidato com cpf ja existente"
        def candidato = new Candidato(
                cpf: "666.666.666-66",
                idade: 21,
                nome: "Túlio",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Desenvolvedor"
        )

        when: "adicionar uma nova candidato com cpf ja existente"
        service.adicionarCandidato(candidato)

        then: "deve lancar um excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "Cpf ja cadastrado"
    }

    def "nao deve permitir email duplicado"() {
        given: "repository mockado de candidato"
        CandidatoRepository repositoryMock = Mock(CandidatoRepository)
        service.candidatoRepository = repositoryMock

        def candidatos = [
                new Candidato(email: "teste@teste.com"),
        ]

        and: "o repository deve retornar candidatos cadastradas"
        repositoryMock.listarTodos() >> candidatos

        and: "uma nova empresa com email existente"
        def candidato = new Candidato(
                cpf: "000.000.000-00",
                idade: 21,
                nome: "Túlio",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Desenvolvedor"
        )

        when: "adicionar uma nova candidato com email ja existente"
        service.adicionarCandidato(candidato)

        then: "deve lancar um excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "Email ja cadastrado"
    }
}
