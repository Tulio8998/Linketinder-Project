package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.service.CompetenciaService
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CompetenciaController extends HttpServlet {

    private final CompetenciaService service = new CompetenciaService()

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo

            if (path == '/candidato') {
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer candidatoId = parseIntSafe(body?.candidatoId)
                Integer competenciaId = parseIntSafe(body?.competenciaId)

                if (candidatoId == null || competenciaId == null) {
                    JsonHelper.error(resp, 400, 'Campos obrigatórios: candidatoId, competenciaId')
                    return
                }

                service.adicionarCompetenciaCandidato(candidatoId, competenciaId)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Competência adicionada ao candidato',
                        candidatoId: candidatoId,
                        competenciaId: competenciaId
                ])
                return
            }

            if (path == '/vaga') {
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer vagaId = parseIntSafe(body?.vagaId)
                Integer competenciaId = parseIntSafe(body?.competenciaId)

                if (vagaId == null || competenciaId == null) {
                    JsonHelper.error(resp, 400, 'Campos obrigatórios: vagaId, competenciaId')
                    return
                }

                service.adicionarCompetenciaVaga(vagaId, competenciaId)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Competência adicionada à vaga',
                        vagaId: vagaId,
                        competenciaId: competenciaId
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo
            if (path == null) {
                JsonHelper.error(resp, 400, 'Formato: DELETE')
                return
            }

            String[] segments = path.split('/')

            if (segments.length != 4) {
                JsonHelper.error(resp, 400, 'Formato: DELETE')
                return
            }

            String tipo = segments[1]
            Integer idPrincipal
            Integer idCompetencia

            try {
                idPrincipal = segments[2] as Integer
                idCompetencia = segments[3] as Integer
            } catch (NumberFormatException ignored) {
                JsonHelper.error(resp, 400, "IDs inválidos: ${segments[2]}/${segments[3]}")
                return
            }

            if (tipo == 'candidato') {
                service.excluirCompetenciaCandidato(idPrincipal, idCompetencia)
                resp.status = 204
                return
            }

            if (tipo == 'vaga') {
                service.excluirCompetenciaVaga(idPrincipal, idCompetencia)
                resp.status = 204
                return
            }

            JsonHelper.error(resp, 404, "Recurso desconhecido: ${tipo}")

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
                List<Competencia> lista = service.listarTodos()
                JsonHelper.respond(resp, 200, lista)
                return
            }

            String[] segments = path.split('/')

            if (segments.length < 3) {
                JsonHelper.error(resp, 400, 'Formato: GET')
                return
            }

            String tipo = segments[1]
            Integer idPrincipal

            try {
                idPrincipal = segments[2] as Integer
            } catch (NumberFormatException ignored) {
                JsonHelper.error(resp, 400, "ID inválido: ${segments[2]}")
                return
            }

            String nome = req.getParameter('nome')

            if (tipo == 'candidato') {
                if (nome != null && !nome.isBlank()) {
                    Competencia comp = service.buscarNomeCompetenciaCandidato(idPrincipal, nome)
                    if (comp == null) {
                        JsonHelper.error(resp, 404,
                                "Competência '${nome}' não encontrada para o candidato ${idPrincipal}")
                        return
                    }
                    JsonHelper.respond(resp, 200, comp)
                    return
                }

                if (segments.length == 4) {
                    Integer idCompetencia

                    try {
                        idCompetencia = segments[3] as Integer
                    } catch (NumberFormatException ignored) {
                        JsonHelper.error(resp, 400, "ID de competência inválido: ${segments[3]}")
                        return
                    }

                    Competencia comp = service.buscarIdCompetenciaCandidato(idPrincipal, idCompetencia)
                    if (comp == null) {
                        JsonHelper.error(resp, 404,
                                "Competência ${idCompetencia} não encontrada para o candidato ${idPrincipal}")
                        return
                    }
                    JsonHelper.respond(resp, 200, comp)
                    return
                }

                List<Competencia> lista = service.listarCompetenciasCandidato(idPrincipal)
                JsonHelper.respond(resp, 200, lista)
                return
            }

            if (tipo == 'vaga') {
                if (nome != null && !nome.isBlank()) {
                    Competencia comp = service.buscarNomeCompetenciaVaga(idPrincipal, nome)
                    if (comp == null) {
                        JsonHelper.error(resp, 404,
                                "Competência '${nome}' não encontrada para a vaga ${idPrincipal}")
                        return
                    }
                    JsonHelper.respond(resp, 200, comp)
                    return
                }

                if (segments.length == 4) {
                    Integer idCompetencia

                    try {
                        idCompetencia = segments[3] as Integer
                    } catch (NumberFormatException ignored) {
                        JsonHelper.error(resp, 400, "ID de competência inválido: ${segments[3]}")
                        return
                    }

                    Competencia comp = service.buscarIdCompetenciaVaga(idPrincipal, idCompetencia)
                    if (comp == null) {
                        JsonHelper.error(resp, 404,
                                "Competência ${idCompetencia} não encontrada para a vaga ${idPrincipal}")
                        return
                    }
                    JsonHelper.respond(resp, 200, comp)
                    return
                }

                List<Competencia> lista = service.listarCompetenciasVaga(idPrincipal)
                JsonHelper.respond(resp, 200, lista)
                return
            }

            JsonHelper.error(resp, 404, "Recurso desconhecido: ${tipo}")

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
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