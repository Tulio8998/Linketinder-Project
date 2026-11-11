package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.CompetenciaService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CompetenciaController extends HttpServlet {

    private final CompetenciaService service
    private final CandidatoService candidatoService
    private final EmpresaService empresaService
    private final VagaService vagaService

    CompetenciaController() {
        this(new CompetenciaService(), new CandidatoService(), new EmpresaService(), new VagaService())
    }

    CompetenciaController(
            CompetenciaService service,
            CandidatoService candidatoService,
            EmpresaService empresaService,
            VagaService vagaService
    ) {
        this.service = service
        this.candidatoService = candidatoService
        this.empresaService = empresaService
        this.vagaService = vagaService
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.pathInfo

            if (path == '/candidato') {
                Candidato candidato = candidatoAutenticado(req, resp)
                if (candidato == null) return
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer candidatoId = parseIntSafe(body?.candidatoId)
                Integer competenciaId = parseIntSafe(body?.competenciaId)

                if (candidatoId != candidato.id || competenciaId == null) {
                    JsonHelper.error(resp, 400, 'Identidade ou competência inválida')
                    return
                }

                service.adicionarCompetenciaCandidato(candidatoId, competenciaId)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Competência adicionada ao perfil'
                ])
                return
            }

            if (path == '/vaga') {
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                Map body = JsonHelper.read(req.inputStream, Map)
                Integer vagaId = parseIntSafe(body?.vagaId)
                Integer competenciaId = parseIntSafe(body?.competenciaId)

                if (vagaId == null || competenciaId == null) {
                    JsonHelper.error(resp, 400, 'Campos obrigatórios: vagaId, competenciaId')
                    return
                }
                if (!vagaPertenceAVaga(vagaId, empresa.id, resp)) return

                service.adicionarCompetenciaVaga(vagaId, competenciaId)

                JsonHelper.respond(resp, 201, [
                        mensagem: 'Competência adicionada à vaga'
                ])
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
                Candidato candidato = candidatoAutenticado(req, resp)
                if (candidato == null) return
                if (idPrincipal != candidato.id) {
                    JsonHelper.error(resp, 404, 'Competências não encontradas')
                    return
                }
                service.excluirCompetenciaCandidato(idPrincipal, idCompetencia)
                resp.status = 204
                return
            }

            if (tipo == 'vaga') {
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                if (!vagaPertenceAVaga(idPrincipal, empresa.id, resp)) return
                service.excluirCompetenciaVaga(idPrincipal, idCompetencia)
                resp.status = 204
                return
            }

            JsonHelper.error(resp, 404, "Recurso desconhecido: ${tipo}")

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
                Candidato candidato = candidatoAutenticado(req, resp)
                if (candidato == null) return
                if (idPrincipal != candidato.id) {
                    JsonHelper.error(resp, 404, 'Competências não encontradas')
                    return
                }
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
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                if (!vagaPertenceAVaga(idPrincipal, empresa.id, resp)) return
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
            JsonHelper.internalError(resp, e)
        }
    }

    private Candidato candidatoAutenticado(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        try {
            Candidato candidato = candidatoService.buscarEmailCandidatoParaAutorizacao(email)
            if (candidato == null) JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a um candidato')
            return candidato
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
    }

    private Empresa empresaAutenticada(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        try {
            Empresa empresa = empresaService.buscarEmailEmpresaParaAutorizacao(email)
            if (empresa == null) JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a uma empresa')
            return empresa
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
    }

    private boolean vagaPertenceAVaga(Integer vagaId, Integer empresaId, HttpServletResponse resp) {
        try {
            Vaga vaga = vagaService.buscarPorId(vagaId)
            if (vaga == null || vaga.empresa?.id != empresaId) {
                JsonHelper.error(resp, 404, 'Vaga não encontrada')
                return false
            }
            return true
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return false
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