package com.zg.linketinder.model

import spock.lang.Specification

class CandidatoSpec extends Specification {
    def "deve retornar um cpf"() {
        given: "um candidato recem instanciado"
        def candidato = new Candidato()

        when: "um cpf é atribuido e solicitado"
        candidato.cpf = "000.000.000-00"
        def cpfSolicitado = candidato.cpf

        then: "o cpf retornado deve ser exatamente o que foi definido"
        cpfSolicitado == "000.000.000-00"
    }

    def "deve alterar um cpf"() {
        given: "um candidato recem instanciado"
        def candidato = new Candidato()

        when: "o cpf é alterado"
        candidato.setCpf("111.111.111-11")

        then: "o cpf deve ser exatamente o que foi definido"
        candidato.cpf == "111.111.111-11"

    }

    def "deve retornar uma idade"() {
        given: "um candidato recem instanciado"
        def candidato = new Candidato()

        when: "uma idade é atribuido e solicitado"
        candidato.idade = 21
        def idadeSolicitada = candidato.idade

        then: "a idade retornada deve ser exatamente o que foi definida"
        idadeSolicitada == 21
    }

    def "deve alterar uma idade"() {
        given: "um candidato recem instanciado"
        def candidato = new Candidato()

        when: "a idade é alterada"
        candidato.setIdade(25)

        then: "a idade deve ser exatamente o que foi definida"
        candidato.idade == 25
    }
}
