package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.api.AnonymousVagaTokenStore
import com.zg.linketinder.service.CompetenciaService
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CurtidaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.ServletInputStream
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.security.Principal

class LegacyApiPrivacySpec extends Specification {

    def "bloqueia leitura de perfis de candidatos sem identidade autenticada"() {
        given:
        CandidatoService service = Mock()
        CandidatoController controller = new CandidatoController(service)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/', null), response(captured))

        then:
        captured.status == 401
        captured.body().contains('Autenticação obrigatória')
        0 * service._
    }

    def "limita consulta por ID ao candidato autenticado e não serializa senha"() {
        given:
        CandidatoService service = Mock()
        CandidatoController controller = new CandidatoController(service)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/4', 'candidate@example.com'), response(captured))

        then:
        1 * service.buscarEmailCandidatoParaAutorizacao('candidate@example.com') >>
                new Candidato(id: 4, nome: 'Candidata', email: new com.zg.linketinder.vo.EmailVO(
                        'candidate@example.com'), cpf: new com.zg.linketinder.vo.CpfVO('123.456.789-09'),
                        senha: 'nunca-serializar')
        captured.status == 200
        captured.body().contains('Candidata')
        captured.body().contains('123.456.789-09')
        !captured.body().contains('nunca-serializar')
        !captured.body().contains('senha')
    }

    def "não revela perfil de outro candidato ao usuário autenticado"() {
        given:
        CandidatoService service = Mock()
        CandidatoController controller = new CandidatoController(service)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/5', 'candidate@example.com'), response(captured))

        then:
        1 * service.buscarEmailCandidatoParaAutorizacao('candidate@example.com') >>
                new Candidato(id: 4, email: new com.zg.linketinder.vo.EmailVO('candidate@example.com'))
        captured.status == 404
        !captured.body().contains('candidate@example.com')
        0 * service.buscarPorId(_)
    }

    def "bloqueia leitura de empresas sem identidade autenticada"() {
        given:
        EmpresaService service = Mock()
        EmpresaController controller = new EmpresaController(service)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/', null), response(captured))

        then:
        captured.status == 401
        0 * service._
    }

    def "bloqueia consulta de competências de candidato sem sua identidade"() {
        given:
        CompetenciaService competenciaService = Mock()
        CandidatoService candidatoService = Mock()
        CompetenciaController controller = new CompetenciaController(
                competenciaService, candidatoService, Mock(EmpresaService), Mock(VagaService))
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/candidato/4', null), response(captured))

        then:
        captured.status == 401
        0 * competenciaService._
        0 * candidatoService._
    }

    def "bloqueia acesso por ID de vaga sem empresa autenticada"() {
        given:
        VagaService vagaService = Mock()
        CompetenciaController controller = new CompetenciaController(
                Mock(CompetenciaService), Mock(CandidatoService), Mock(EmpresaService), vagaService)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/vaga/73', null), response(captured))

        then:
        captured.status == 401
        0 * vagaService._
    }

    def "empresa autenticada acessa apenas perfil próprio sem serializar senha"() {
        given:
        EmpresaService service = Mock()
        EmpresaController controller = new EmpresaController(service)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/', 'company@example.com'), response(captured))

        then:
        1 * service.buscarEmailEmpresaParaAutorizacao('company@example.com') >>
                new Empresa(id: 2, nome: 'Empresa', email: new com.zg.linketinder.vo.EmailVO(
                        'company@example.com'), cnpj: new com.zg.linketinder.vo.CnpjVO(
                        '12.345.678/0001-90'), senha: 'senha-secreta')
        captured.status == 200
        captured.body().contains('Empresa')
        !captured.body().contains('senha-secreta')
        !captured.body().contains('senha')
    }

    def "publica vagas anônimas sem IDs nem identidade da empresa"() {
        given:
        VagaService vagaService = Mock()
        EmpresaService empresaService = Mock()
        VagaController controller = new VagaController(vagaService, empresaService)
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/', null), response(captured))

        then:
        1 * vagaService.listarTodos() >> [
                new Vaga(
                        id: 739,
                        nome: 'Engenharia de Dados',
                        descricao: 'Construir plataformas de dados',
                        area: 'TI',
                        endereco: new com.zg.linketinder.model.Endereco(
                                pais: 'Brasil', estado: 'SP', cidade: 'São Paulo'),
                        empresa: new Empresa(id: 88, nome: 'Empresa Confidencial'),
                        competencias: [new Competencia(nome: 'PostgreSQL')]
                )
        ]
        captured.status == 200
        String json = captured.body()
        json.contains('Engenharia de Dados')
        json.contains('Construir plataformas de dados')
        json.contains('PostgreSQL')
        json.contains('TI')
        !json.contains('739')
        !json.contains('88')
        !json.contains('Empresa Confidencial')
        !json.contains('Brasil')
        !json.contains('idEmpresa')
    }

    def "bloqueia consulta anônima de vaga por identificador estável"() {
        given:
        VagaService vagaService = Mock()
        VagaController controller = new VagaController(vagaService, Mock(EmpresaService))
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/739', null), response(captured))

        then:
        captured.status == 401
        0 * vagaService._
    }

    def "bloqueia listagens globais de curtidas e matches sem autenticação"() {
        given:
        CurtidaService service = Mock()
        CandidatoService candidatoService = Mock()
        CurtidaController controller = new CurtidaController(
                service, candidatoService, Mock(EmpresaService), Mock(VagaService))
        CapturedBody captured = new CapturedBody()

        when:
        controller.doGet(request('/matches/candidato/42', null), response(captured))

        then:
        captured.status == 401
        0 * candidatoService._
        0 * service._
    }

    def "não permite que o endpoint legado de curtida da empresa ignore autoria do recrutador"() {
        given:
        CurtidaService service = Mock()
        CurtidaController controller = new CurtidaController(
                service, Mock(CandidatoService), Mock(EmpresaService), Mock(VagaService))
        CapturedBody captured = new CapturedBody()

        when:
        controller.doPost(request('/empresa', null), response(captured))

        then:
        captured.status == 403
        captured.body().contains('recrutador autenticado')
        0 * service._
    }

    def "curtida de candidato resolve token anônimo usando somente a identidade autenticada"() {
        given:
        CandidatoService candidatoService = Mock()
        CurtidaService curtidaService = Mock()
        VagaService vagaService = Mock()
        CurtidaController controller = new CurtidaController(
                curtidaService, candidatoService, Mock(EmpresaService), vagaService)
        CapturedBody captured = new CapturedBody()
        String token = AnonymousVagaTokenStore.INSTANCE.criar(73)
        HttpServletRequest request = request('/candidato', 'candidate@example.com')
        request.inputStream >> input('{"token":"' + token + '"}')

        when:
        controller.doPost(request, response(captured))

        then:
        1 * candidatoService.buscarEmailCandidatoParaAutorizacao('candidate@example.com') >>
                new Candidato(id: 18)
        1 * vagaService.buscarPorId(73) >> new Vaga(id: 73)
        1 * curtidaService.curtirVagaComoCandidato(18, 73, true)
        captured.status == 201
        captured.body().contains('Vaga curtida com sucesso')
        !captured.body().contains('18')
        !captured.body().contains('73')
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

    private static ServletInputStream input(String value) {
        ByteArrayInputStream bytes = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8))
        return new ServletInputStream() {
            @Override
            int read() { bytes.read() }

            @Override
            boolean isFinished() { bytes.available() == 0 }

            @Override
            boolean isReady() { true }

            @Override
            void setReadListener(jakarta.servlet.ReadListener listener) {}
        }
    }

    private HttpServletResponse response(CapturedBody captured) {
        HttpServletResponse response = Mock()
        response.getOutputStream() >> captured.output
        response.setStatus(_ as int) >> { int status -> captured.status = status }
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
