package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.ReviewTokenStore
import com.zg.linketinder.dto.MatchRecrutadorDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.PerfilCandidatoAnonimo
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.RecrutadorService
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.security.Principal

class RecrutadorControllerSpec extends Specification {
    RecrutadorService recrutadorService = Mock()
    EmpresaService empresaService = Mock()
    CandidatoService candidatoService = Mock()
    ReviewTokenStore tokenStore = new ReviewTokenStore()
    RecrutadorController controller = new RecrutadorController(
            recrutadorService, empresaService, candidatoService, tokenStore)

    def "bloqueia leitura sem identidade autenticada"() {
        given:
        HttpServletRequest request = request('/me/vagas', null)
        CapturedBody captured = new CapturedBody()
        HttpServletResponse response = response(captured)

        when:
        controller.doGet(request, response)

        then:
        captured.status == 401
        captured.body().contains('Autenticação obrigatória')
        0 * recrutadorService._
    }

    def "retorna vagas de triagem sem identificador interno ou identidade da empresa"() {
        given:
        Recrutador recrutador = new Recrutador(id: 8, empresa: new Empresa(id: 9),
                nome: 'Recrutadora', area: 'TI', ativo: true)
        HttpServletRequest request = request('/me/vagas', 'recrutadora@example.com')
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request, response(captured))

        then:
        1 * recrutadorService.buscarPorEmail('recrutadora@example.com') >> recrutador
        1 * recrutadorService.listarVagasDaArea(8) >> [
                new Vaga(id: 727, nome: 'Engenheira de Software', descricao: 'Criar serviços',
                        area: 'TI', empresa: new Empresa(id: 9, nome: 'Empresa Secreta'),
                        competencias: [new Competencia(nome: 'Groovy')])
        ]
        captured.status == 200
        String json = captured.body()
        json.contains('Engenheira de Software')
        json.contains('Groovy')
        !json.contains('727')
        !json.contains('Empresa Secreta')
        !json.contains('idEmpresa')
        !json.contains('senha')
    }

    def "candidatos são serializados sem dados pessoais nem identificador interno"() {
        given:
        Recrutador recrutador = new Recrutador(id: 8, area: 'TI', ativo: true)
        String tokenVaga = tokenStore.criarTokenVaga(8, 41)
        HttpServletRequest request = request("/me/vagas/${tokenVaga}/candidatos",
                'recrutadora@example.com')
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request, response(captured))

        then:
        1 * recrutadorService.buscarPorEmail('recrutadora@example.com') >> recrutador
        1 * recrutadorService.listarCandidatosAnonimos(8, 41) >> [
                new PerfilCandidatoAnonimo(
                        idInterno: 987654,
                        descricao: 'Experiência em sistemas distribuídos',
                        competencias: ['Groovy', 'PostgreSQL'],
                        formacoes: ['Ciência da Computação']
                )
        ]
        captured.status == 200
        String json = captured.body()
        json.contains('Experiência em sistemas distribuídos')
        json.contains('Ciência da Computação')
        !json.contains('987654')
        !json.contains('nome')
        !json.contains('email')
        !json.contains('cpf')
        !json.contains('dataNascimento')
        !json.contains('senha')
    }

    def "contato do match é retornado ao candidato autenticado sem dados sensíveis do recrutador"() {
        given:
        HttpServletRequest request = request('/matches/candidato', 'candidato@example.com')
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request, response(captured))

        then:
        1 * candidatoService.buscarEmailCandidatoParaAutorizacao('candidato@example.com') >>
                new Candidato(id: 19)
        1 * recrutadorService.listarMatchesCandidato(19) >> [
                new MatchRecrutadorDTO(
                        nomeRecrutador: 'Recrutadora',
                        emailRecrutador: 'recrutadora@empresa.com',
                        nomeEmpresa: 'Empresa Match',
                        emailEmpresa: 'contato@empresa.com',
                        cnpjEmpresa: '12.345.678/0001-90',
                        nomeVaga: 'Engenheira de Software'
                )
        ]
        captured.status == 200
        String json = captured.body()
        json.contains('recrutadora@empresa.com')
        json.contains('Empresa Match')
        !json.contains('cpf')
        !json.contains('dataNascimento')
        !json.contains('senha')
    }

    private HttpServletRequest request(String path, String email) {
        HttpServletRequest request = Mock()
        request.pathInfo >> path
        request.userPrincipal >> (email == null ? null : new Principal() {
            @Override
            String getName() { email }
        })
        return request
    }

    private HttpServletResponse response(CapturedBody captured) {
        HttpServletResponse response = Mock()
        response.getOutputStream() >> captured.output
        response.setStatus(_ as int) >> { int value -> captured.status = value }
        return response
    }

    private static class CapturedBody {
        private final ByteArrayOutputStream bytes = new ByteArrayOutputStream()
        private int status = 200
        private final ServletOutputStream output

        CapturedBody() {
            output = new ServletOutputStream() {
                @Override
                void write(int value) { bytes.write(value) }

                @Override
                boolean isReady() { true }

                @Override
                void setWriteListener(WriteListener listener) {}
            }
        }

        String body() { bytes.toString(StandardCharsets.UTF_8) }
    }
}
