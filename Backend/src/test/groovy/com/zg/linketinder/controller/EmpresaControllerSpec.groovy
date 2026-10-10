package com.zg.linketinder.controller

import com.zg.linketinder.dto.EmpresaDTO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.service.EmpresaService
import spock.lang.Specification

class EmpresaControllerSpec extends Specification {

    EmpresaService empresaServiceMock = Mock()
    EmpresaController empresaController = new EmpresaController(empresaService: empresaServiceMock)

    def "deve cadastrar uma empresa com sucesso"() {
        given: "uma empresa DTO valida"
        EmpresaDTO empresaDTO = new EmpresaDTO(
                cnpj: "12.345.678/0001-90",
                nome: "Empresa Teste",
                email: "empresa@teste.com",
                senha: "senha123",
                pais: "Brasil",
                estado: "MG",
                cidade: "Belo Horizonte",
                cep: "30000-000",
                descricao: "Empresa de tecnologia"
        )

        when: "é solicitado o cadastro"
        String resposta = empresaController.cadastrarEmpresa(empresaDTO)

        then: "o service deve salvar a entidade e retornar sucesso"
        1 * empresaServiceMock.salvar(_)
        resposta == "Empresa cadastrada com sucesso!"
    }

    def "deve tratar erro ao cadastrar empresa invalida"() {
        given: "uma empresa DTO com email invalido"
        EmpresaDTO empresaDTO = new EmpresaDTO(
                cnpj: "12.345.678/0001-90",
                nome: "Empresa Teste",
                email: "email-invalido",
                senha: "senha"
        )

        when: "é solicitado o cadastro"
        String resposta = empresaController.cadastrarEmpresa(empresaDTO)

        then: "retorna erro sem chamar o service"
        0 * empresaServiceMock.salvar(_)
        resposta.contains("Erro no cadastro")
    }

    def "deve atualizar uma empresa com sucesso"() {
        given: "um id e uma empresa DTO valida"
        Integer id = 1
        EmpresaDTO empresaDTO = new EmpresaDTO(
                cnpj: "12.345.678/0001-90",
                nome: "Empresa Atualizada",
                email: "novo@empresa.com",
                senha: "nova_senha",
                pais: "Brasil",
                estado: "SP",
                cidade: "São Paulo",
                cep: "01000-000",
                descricao: "Nova desc"
        )

        when: "é solicitada a atualização"
        String resposta = empresaController.atualizarEmpresa(id, empresaDTO)

        then: "o service deve atualizar a entidade e retornar sucesso"
        1 * empresaServiceMock.atualizar(id, _)
        resposta == "Empresa atualizada com sucesso!"
    }

    def "deve tratar erro ao atualizar empresa com dados invalidos"() {
        given: "um id e uma empresa DTO com CNPJ vazio"
        Integer id = 1
        EmpresaDTO empresaDTO = new EmpresaDTO(
                cnpj: "",
                nome: "Empresa Atualizada",
                email: "novo@empresa.com"
        )

        when: "é solicitada a atualização"
        String resposta = empresaController.atualizarEmpresa(id, empresaDTO)

        then: "retorna erro sem chamar o service"
        0 * empresaServiceMock.atualizar(_, _)
        resposta.contains("Erro na atualização")
    }

    def "deve excluir uma empresa com sucesso"() {
        given: "um id valido"
        Integer id = 1

        when: "é solicitada a exclusão"
        String resposta = empresaController.excluirEmpresa(id)

        then: "o service exclui e retorna sucesso"
        1 * empresaServiceMock.excluir(id)
        resposta == "Empresa excluída com sucesso!"
    }

    def "deve tratar erro ao excluir empresa inexistente"() {
        given: "um id que causa exceção no service"
        Integer id = 99
        empresaServiceMock.excluir(id) >> { throw new IllegalArgumentException("Empresa não encontrada") }

        when: "é solicitada a exclusão"
        String resposta = empresaController.excluirEmpresa(id)

        then: "retorna a mensagem de erro"
        resposta == "Erro na exclusão: Empresa não encontrada"
    }

    def "deve listar as empresas"() {
        given: "uma lista mockada"
        List<Empresa> listaMockada = [new Empresa(nome: "Empresa 1"), new Empresa(nome: "Empresa 2")]

        when: "solicito a listagem"
        List<Empresa> resultado = empresaController.listarEmpresas()

        then: "retorna a lista do service"
        1 * empresaServiceMock.listarTodos() >> listaMockada
        resultado.size() == 2
        resultado[0].nome == "Empresa 1"
    }

    def "deve buscar pelo id"() {
        given: "um id valido"
        Integer id = 1
        Empresa empresaMock = new Empresa(nome: "Empresa 1")

        when: "solicito a busca"
        Empresa resultado = empresaController.buscarPorId(id)

        then: "retorna a empresa do service"
        1 * empresaServiceMock.buscarPorId(id) >> empresaMock
        resultado == empresaMock
    }

    def "deve buscar pelo cnpj"() {
        given: "um cnpj valido"
        String cnpj = "12.345.678/0001-90"
        Empresa empresaMock = new Empresa(nome: "Empresa 1")

        when: "solicito a busca"
        Empresa resultado = empresaController.buscarPorCnpj(cnpj)

        then: "retorna a empresa do service"
        1 * empresaServiceMock.buscarCnpjEmpresa(cnpj) >> empresaMock
        resultado == empresaMock
    }

    def "deve buscar pelo email"() {
        given: "um email valido"
        String email = "teste@empresa.com"
        Empresa empresaMock = new Empresa(nome: "Empresa 1")

        when: "solicito a busca"
        Empresa resultado = empresaController.buscarPorEmail(email)

        then: "retorna a empresa do service"
        1 * empresaServiceMock.buscarEmailEmpresa(email) >> empresaMock
        resultado == empresaMock
    }
}