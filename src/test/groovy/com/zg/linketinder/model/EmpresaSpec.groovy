package com.zg.linketinder.model

import spock.lang.Specification

class EmpresaSpec extends Specification {

    def "deve retornar um cnpj"() {
        given: "uma empresa recem instanciada"
        Empresa empresa = new Empresa()

        when: "um cnpj é atribuido e solicitado"
        empresa.cnpj = "00.000.000/0000-00"
        def cnpjSolicitado = empresa.cnpj

        then: "o cnpj retornado deve ser exatamente o que foi definido"
        cnpjSolicitado == "00.000.000/0000-00"
    }

    def "deve alterar um cnpj"() {
        given: "uma empresa recem instanciada"
        Empresa empresa = new Empresa()

        when: "o cnpj é alterado"
        empresa.setCnpj("11.111.111/1111-11")

        then: "o cnpj deve ser exatamente o que foi definido"
        empresa.cnpj == "11.111.111/1111-11"
    }

    def "deve retornar um pais"() {
        given: "uma empresa recem instanciada"
        Empresa empresa = new Empresa()

        when: "um pais é atribuido e solicitado"
        empresa.pais = "Brasil"
        def paisSolicitado = empresa.pais

        then: "o pais retornado deve ser exatamente o que foi definido"
        paisSolicitado == "Brasil"
    }

    def "deve alterar um pais"() {
        given: "uma empresa recem instanciada"
        Empresa empresa = new Empresa()

        when: "o pais é alterado"
        empresa.setPais("Argentina")

        then: "o pais deve ser exatamente o que foi definido"
        empresa.pais == "Argentina"
    }
}