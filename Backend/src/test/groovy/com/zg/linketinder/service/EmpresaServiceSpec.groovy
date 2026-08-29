package com.zg.linketinder.service

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.repository.EmpresaRepository
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {
    def service = new EmpresaService()

    def "deve listar todas as empresas"() {
        given: "repository mockado de empresa"
        EmpresaRepository repositoryMock = Mock(EmpresaRepository)
        service.empresaRepository = repositoryMock

        def empresas = [
            new Empresa(nome: "Empresa 1"),
            new Empresa(nome: "Empresa 2")
        ]

        and: "o repository deve retornar uma lista de empresas"
        repositoryMock.listarTodos() >> empresas

        when: "a lista de empresas solicitada"
        def resultado = service.listarTodos()

        then: "o service deve retornar a lista fornecida pelo repository"
        resultado == empresas
    }

    def "deve adicionar uma nova empresa"() {
        given: "um repository mockado"
        EmpresaRepository repositoryMock = Mock(EmpresaRepository)
        service.empresaRepository = repositoryMock

        and: "uma nova empresa"
        def empresa = new Empresa(
                cnpj: "00.000.000/0000-00",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Zero Glosa"
        )

        when: "o metodo adicionar for chamado"
        service.adicionarEmpresa(empresa)

        then: "o repository deve receber a empresa"
        1 * repositoryMock.adicionarEmpresa(empresa)
    }

    def "nao deve permitir insercao de empresa nulo"() {
        when: "é tentado inserir um valor nulo em vez empresa"
        service.adicionarEmpresa(null)

        then: "deve lancar uma excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "A empresa nao pode ser nulo"
    }

    def "nao deve permitir cnpj duplicado"() {
        given: "repository mockado de empresa"
        EmpresaRepository repositoryMock = Mock(EmpresaRepository)
        service.empresaRepository = repositoryMock

        def empresas = [
                new Empresa(cnpj: "55.555.555/5555-55",),
        ]

        and: "o repository deve retornar empresas cadastradas"
        repositoryMock.listarTodos() >> empresas

        and: "uma nova empresa com cnpj ja existente"
        def empresa = new Empresa(
                cnpj: "55.555.555/5555-55",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Zero Glosa"
        )

        when: "adicionar uma nova empresa com cnpj ja existente"
        service.adicionarEmpresa(empresa)

        then: "deve lancar um excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "Cnpj ja cadastrado"
    }

    def "nao deve permitir email duplicado"() {
        given: "repository mockado de empresa"
        EmpresaRepository repositoryMock = Mock(EmpresaRepository)
        service.empresaRepository = repositoryMock

        def empresas = [
                new Empresa(email: "teste@teste.com"),
        ]

        and: "o repository deve retornar empresas cadastradas"
        repositoryMock.listarTodos() >> empresas

        and: "uma nova empresa com email existente"
        def empresa = new Empresa(
                cnpj: "55.555.555/5555-55",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Zero Glosa"
        )

        when: "adicionar uma nova empresa com email ja existente"
        service.adicionarEmpresa(empresa)

        then: "deve lancar um excecao"
        def erro = thrown(IllegalArgumentException)
        erro.message == "Email ja cadastrado"
    }
}
