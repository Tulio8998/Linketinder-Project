package com.zg.linketinder.controller

import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.service.CandidatoService
import spock.lang.Specification

import java.time.LocalDate

class CandidatoControllerSpec extends Specification {
    CandidatoService candidatoServiceMock = Mock()
    CandidatoController candidatoController = new CandidatoController(candidatoService: candidatoServiceMock)

    def "deve cadastrar um candidato com sucesso"() {
        given: "um candidato DTO valido"
        CandidatoDTO candidatoDTO = new CandidatoDTO(
                cpf: "111.111.111-11",
                email: "teste@teste.teste",
                nome: "Teste",
                idade: 21,
                senha: "12345678",
                pais: "Brasil",
                estado: "MG",
                cidade: "Belo Horizonte",
                cep: "12342-244",
                dataNascimento: LocalDate.of(2004, 10, 1),
                descricao: "Teste"
        )

        when: "é solicitado o cadastro"
        String resposta = candidatoController.cadastrarCandidato(candidatoDTO)

        then: "o service deve salvar a entidade e retornar sucesso"
        1 * candidatoServiceMock.salvar(_)
        resposta == "Candidato cadastrado com sucesso!"
    }

    def "deve tratar erro ao cadastrar candidato invalido"() {
        given: "um candidato DTO com CPF vazio"
        CandidatoDTO candidatoDTO = new CandidatoDTO(
                cpf: "",
                email: "teste@teste.teste",
                nome: "Teste"
        )

        when: "tento cadastrar"
        String resposta = candidatoController.cadastrarCandidato(candidatoDTO)

        then: "o service não é chamado e retorna mensagem de erro"
        0 * candidatoServiceMock.salvar(_)
        resposta.contains("Erro no cadastro")
    }

    def "deve atualizar um candidato com sucesso"() {
        given: "um id e um candidato DTO valido"
        Integer id = 1
        CandidatoDTO candidatoDTO = new CandidatoDTO(
                cpf: "111.111.111-11",
                email: "novoemail@teste.com",
                nome: "Teste Atualizado",
                idade: 22,
                senha: "nova_senha",
                pais: "Brasil",
                estado: "SP",
                cidade: "São Paulo",
                cep: "00000-000",
                dataNascimento: LocalDate.of(2004, 10, 1),
                descricao: "Nova descricao"
        )

        when: "é solicitada a atualização"
        String resposta = candidatoController.atualizarCandidato(id, candidatoDTO)

        then: "o service atualiza a entidade e retorna sucesso"
        1 * candidatoServiceMock.atualizar(id, _)
        resposta == "Candidato atualizado com sucesso!"
    }

    def "deve tratar erro ao atualizar candidato com dados invalidos"() {
        given: "um id e um candidato DTO com email invalido"
        Integer id = 1
        CandidatoDTO candidatoDTO = new CandidatoDTO(
                cpf: "111.111.111-11",
                email: "email_invalido",
                nome: "Teste"
        )

        when: "é solicitada a atualização"
        String resposta = candidatoController.atualizarCandidato(id, candidatoDTO)

        then: "o service não é chamado e retorna erro"
        0 * candidatoServiceMock.atualizar(_, _)
        resposta.contains("Erro na atualização")
    }

    def "deve excluir um candidato com sucesso"() {
        given: "um id existente"
        Integer id = 1

        when: "é solicitada a exclusão"
        String resposta = candidatoController.excluirCandidato(id)

        then: "o service exclui e retorna sucesso"
        1 * candidatoServiceMock.excluir(id)
        resposta == "Candidato excluído com sucesso!"
    }

    def "deve tratar erro ao excluir candidato inexistente"() {
        given: "um id que causará exceção no service"
        Integer id = 99
        candidatoServiceMock.excluir(id) >> { throw new IllegalArgumentException("Candidato não encontrado") }

        when: "é solicitada a exclusão"
        String resposta = candidatoController.excluirCandidato(id)

        then: "retorna a mensagem de erro formatada"
        resposta == "Erro na exclusão: Candidato não encontrado"
    }

    def "deve listar os candidatos"() {
        given: "uma lista mockada de candidatos"
        List<Candidato> listaMockada = [new Candidato(nome: "Candidato 1"), new Candidato(nome: "Candidato 2")]

        when: "solicito a listagem"
        List<Candidato> resultado = candidatoController.listarCandidatos()

        then: "o service retorna a lista"
        1 * candidatoServiceMock.listarTodos() >> listaMockada
        resultado.size() == 2
        resultado[0].nome == "Candidato 1"
    }

    def "deve buscar pelo id"() {
        given: "um id valido e um candidato mockado"
        Integer id = 1
        Candidato candidatoMock = new Candidato(nome: "Candidato 1")

        when: "solicito a busca"
        Candidato resultado = candidatoController.buscarPorId(id)

        then: "o service retorna o candidato"
        1 * candidatoServiceMock.buscarPorId(id) >> candidatoMock
        resultado == candidatoMock
    }

    def "deve buscar pelo email"() {
        given: "um email e um candidato mockado"
        String email = "teste@teste.com"
        Candidato candidatoMock = new Candidato(nome: "Candidato 1")

        when: "solicito a busca"
        Candidato resultado = candidatoController.buscarPorEmail(email)

        then: "o service retorna o candidato correto"
        1 * candidatoServiceMock.buscarEmailCandidato(email) >> candidatoMock
        resultado == candidatoMock
    }

    def "deve buscar pelo cpf"() {
        given: "um cpf e um candidato mockado"
        String cpf = "111.111.111-11"
        Candidato candidatoMock = new Candidato(nome: "Candidato 1")

        when: "solicito a busca"
        Candidato resultado = candidatoController.buscarPorCpf(cpf)

        then: "o service retorna o candidato"
        1 * candidatoServiceMock.buscarCpfCandidato(cpf) >> candidatoMock
        resultado == candidatoMock
    }

    def "deve listar candidato para empresa"() {
        given: "um id de empresa e uma lista mockada"
        Integer idEmpresa = 1
        List<Candidato> listaMockada = [new Candidato(nome: "Anonimo 1")]

        when: "solicito a listagem de candidatos para a empresa"
        List<Candidato> resultado = candidatoController.listarCandidatoEmpresa(idEmpresa)

        then: "o service retorna a lista filtrada"
        1 * candidatoServiceMock.listarCandidatoEmpresa(idEmpresa) >> listaMockada
        resultado == listaMockada
    }
}