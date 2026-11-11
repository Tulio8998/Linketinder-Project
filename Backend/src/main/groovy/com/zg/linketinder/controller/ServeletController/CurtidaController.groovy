package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.model.curtida.Match
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CurtidaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CurtidaController extends HttpServlet {

    private final CurtidaService curtidaService
    private final CandidatoService candidatoService
    private final EmpresaService empresaService
    private final VagaService vagaService

    CurtidaController() {
        this(new CurtidaService(), new CandidatoService(), new EmpresaService(), new VagaService())
    }

    CurtidaController(
            CurtidaService curtidaService,
            CandidatoService candidatoService,
            EmpresaService empresaService,
            VagaService vagaService
    ) {
        this.curtidaService = curtidaService
        this.candidatoService = candidatoService
        this.empresaService = empresaService
        this.vagaService = vagaService
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo

            if (path == '/candidato') {
                String principalEmail = ServletIdentity.requirePrincipalEmail(req, resp)
                if (principalEmail == null) return
                Candidato autenticado = candidatoService.buscarEmailCandidatoParaAutorizacao(principalEmail)
                if (autenticado == null) {
                    JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a um candidato')
                    return
                }
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer candidatoId = parseIntSafe(body?.candidatoId)
                Integer vagaId = parseIntSafe(body?.vagaId)

                if (candidatoId == null || vagaId == null || candidatoId != autenticado.id) {
                    JsonHelper.error(resp, 400, 'IDs da vaga ou do candidato inválidos')
                    return
                }

                Vaga vaga = vagaService.buscarPorId(vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }

                curtidaService.curtirVagaComoCandidato(autenticado.id, vagaId, true)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Vaga curtida com sucesso'
                ])
                return
            }

            if (path == '/empresa') {
                JsonHelper.error(resp, 403,
                        'Avaliações da empresa devem ser registradas por um recrutador autenticado')
                return
            }

            JsonHelper.error(resp, 404, "Rota não encontrada: POST ${path}")

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo

            if (path == null || path == '/' || path.isEmpty()) {
                JsonHelper.error(resp, 400,
                        'Especifique o recurso: /candidato, /empresa, /matches/candidato/{id}, ' +
                                '/matches/empresa/{id} ou /afinidade')
                return
            }

            if (path == '/candidato') {
                JsonHelper.error(resp, 403, 'A listagem global de curtidas não está disponível')
                return
            }

            if (path == '/empresa') {
                JsonHelper.error(resp, 403, 'A listagem global de avaliações não está disponível')
                return
            }

            if (path.startsWith('/matches/candidato/')) {
                Integer id = parseIdFromPath(path, '/matches/candidato/', resp)
                if (id == null) return
                String email = ServletIdentity.requirePrincipalEmail(req, resp)
                if (email == null) return
                Candidato candidato = candidatoService.buscarEmailCandidatoParaAutorizacao(email)
                if (candidato == null || candidato.id != id) {
                    JsonHelper.error(resp, 404, 'Match não encontrado')
                    return
                }

                List<Match> matches = curtidaService.listarMatchCandidato(id)
                JsonHelper.respond(resp, 200, matches)
                return
            }

            if (path.startsWith('/matches/empresa/')) {
                Integer id = parseIdFromPath(path, '/matches/empresa/', resp)
                if (id == null) return
                String email = ServletIdentity.requirePrincipalEmail(req, resp)
                if (email == null) return
                Empresa empresa = empresaService.buscarEmailEmpresaParaAutorizacao(email)
                if (empresa == null || empresa.id != id) {
                    JsonHelper.error(resp, 404, 'Match não encontrado')
                    return
                }

                List<Match> matches = curtidaService.listarMatchEmpresa(id)
                JsonHelper.respond(resp, 200, matches)
                return
            }

            if (path == '/afinidade') {
                String email = ServletIdentity.requirePrincipalEmail(req, resp)
                if (email == null) return
                Integer candidatoId = parseIntSafe(req.getParameter('candidatoId'))
                Integer vagaId = parseIntSafe(req.getParameter('vagaId'))

                if (candidatoId == null || vagaId == null) {
                    JsonHelper.error(resp, 400, 'Query params obrigatórios: candidatoId, vagaId')
                    return
                }

                Candidato candidato = candidatoService.buscarEmailCandidatoParaAutorizacao(email)
                if (candidato == null || candidato.id != candidatoId) {
                    JsonHelper.error(resp, 404, 'Afinidade não encontrada')
                    return
                }

                Vaga vaga = vagaService.buscarPorId(vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }

                int afinidade = curtidaService.calcularAfinidade(candidato, vaga)

                JsonHelper.respond(resp, 200, [
                        vagaNome: vaga.nome,
                        area: vaga.area,
                        afinidade: afinidade
                ])
                return
            }

            JsonHelper.error(resp, 404, "Rota não encontrada: GET ${path}")

        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private Integer parseIdFromPath(String path, String prefix, HttpServletResponse resp) {
        try {
            return path.substring(prefix.length()) as Integer
        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, "ID inválido: ${path.substring(prefix.length())}")
            return null
        }
    }

    private Integer parseIntSafe(Object value) {
        if (value == null) return null
        try {
            return value as Integer
        } catch (Exception ignored) {
            return null
        }
    }
}