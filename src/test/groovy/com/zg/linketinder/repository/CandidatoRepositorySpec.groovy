package com.zg.linketinder.repository

import com.zg.linketinder.model.Candidato
import spock.lang.Specification

class CandidatoRepositorySpec extends Specification {

    def "deve adicionar um novo candidato ao array"() {
        given: "um repostory de candidatos"
        def repository = new CandidatoRepository()

        and: "um novo candidato"
        def candidato = new Candidato(
                cpf: "555.555.555-55",
                idade: 25,
                nome: "Novo Candidato",
                email: "novo@teste.com",
                estado: "MG",
                cep: "55555-555",
                descricao: "Desenvolvedor"
        )

        def tamanhoInicial = repository.listarTodos().size()

        when: "um novo candidato é adicionado ao repository"
        repository.adicionarCandidato(candidato)

        then: "o tamanho da lista deve aumentar em um"
        repository.listarTodos().size() == tamanhoInicial + 1
    }

    def "deve listar todos os candidatos"() {
        given:
        def repository = new CandidatoRepository()

        when:
        def candidatos = repository.listarTodos()

        then:
        candidatos.size() == 5
    }
}