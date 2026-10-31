package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.CurtidaCandidato
import com.zg.linketinder.model.CurtidaEmpresa
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

    private final CurtidaService curtidaService = new CurtidaService()
    private final CandidatoService candidatoService = new CandidatoService()
    private final EmpresaService empresaService = new EmpresaService()
    private final VagaService vagaService = new VagaService()

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo

            if (path == '/candidato') {
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer candidatoId = parseIntSafe(body?.candidatoId)
                Integer vagaId = parseIntSafe(body?.vagaId)

                if (candidatoId == null || vagaId == null) {
                    JsonHelper.error(resp, 400, 'Campos obrigatórios: candidatoId, vagaId')
                    return
                }

                Candidato candidato = candidatoService.buscarPorId(candidatoId)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato ${candidatoId} não encontrado")
                    return
                }

                Vaga vaga = vagaService.buscarPorId(vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${vagaId} não encontrada")
                    return
                }

                curtidaService.curtirVagaComoCandidato(candidatoId, vagaId, true)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Vaga curtida com sucesso',
                        candidatoId: candidatoId,
                        vagaId: vagaId
                ])
                return
            }

            if (path == '/empresa') {
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer empresaId = parseIntSafe(body?.empresaId)
                Integer candidatoId = parseIntSafe(body?.candidatoId)
                Integer vagaId = parseIntSafe(body?.vagaId)

                if (empresaId == null || candidatoId == null || vagaId == null) {
                    JsonHelper.error(resp, 400, 'Campos obrigatórios: empresaId, candidatoId, vagaId')
                    return
                }

                Empresa empresa = empresaService.buscarPorId(empresaId)
                if (empresa == null) {
                    JsonHelper.error(resp, 404, "Empresa ${empresaId} não encontrada")
                    return
                }

                Candidato candidato = candidatoService.buscarPorId(candidatoId)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato ${candidatoId} não encontrado")
                    return
                }

                Vaga vaga = vagaService.buscarPorId(vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${vagaId} não encontrada")
                    return
                }

                curtidaService.curtirVagaComoEmpresa(empresa, candidato, vaga, true)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Candidato curtido com sucesso',
                        empresaId: empresaId,
                        candidatoId: candidatoId,
                        vagaId: vagaId
                ])
                return
            }

            JsonHelper.error(resp, 404, "Rota não encontrada: POST ${path}")

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
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
                List<CurtidaCandidato> lista = curtidaService.listarCurtidasCandidato()
                JsonHelper.respond(resp, 200, lista)
                return
            }

            if (path == '/empresa') {
                List<CurtidaEmpresa> lista = curtidaService.listarCurtidasEmpresa()
                JsonHelper.respond(resp, 200, lista)
                return
            }

            if (path.startsWith('/matches/candidato/')) {
                Integer id = parseIdFromPath(path, '/matches/candidato/', resp)
                if (id == null) return

                List<Match> matches = curtidaService.listarMatchCandidato(id)
                JsonHelper.respond(resp, 200, matches)
                return
            }

            if (path.startsWith('/matches/empresa/')) {
                Integer id = parseIdFromPath(path, '/matches/empresa/', resp)
                if (id == null) return

                List<Match> matches = curtidaService.listarMatchEmpresa(id)
                JsonHelper.respond(resp, 200, matches)
                return
            }

            if (path == '/afinidade') {
                Integer candidatoId = parseIntSafe(req.getParameter('candidatoId'))
                Integer vagaId = parseIntSafe(req.getParameter('vagaId'))

                if (candidatoId == null || vagaId == null) {
                    JsonHelper.error(resp, 400, 'Query params obrigatórios: candidatoId, vagaId')
                    return
                }

                Candidato candidato = candidatoService.buscarPorId(candidatoId)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato ${candidatoId} não encontrado")
                    return
                }

                Vaga vaga = vagaService.buscarPorId(vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${vagaId} não encontrada")
                    return
                }

                int afinidade = curtidaService.calcularAfinidade(candidato, vaga)

                JsonHelper.respond(resp, 200, [
                        candidatoId: candidatoId,
                        candidatoNome: candidato.nome,
                        vagaId: vagaId,
                        vagaNome: vaga.nome,
                        afinidade: afinidade
                ])
                return
            }

            JsonHelper.error(resp, 404, "Rota não encontrada: GET ${path}")

        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
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