package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.dto.VagaGestaoDTO
import com.zg.linketinder.dto.VagaDTO
import com.zg.linketinder.dto.VagaResumoDTO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class VagaController extends HttpServlet {

    private final VagaService vagaService
    private final EmpresaService empresaService

    VagaController() {
        this(new VagaService(), new EmpresaService())
    }

    VagaController(VagaService vagaService, EmpresaService empresaService) {
        this.vagaService = vagaService
        this.empresaService = empresaService
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Empresa empresa = empresaAutenticada(req, resp)
            if (empresa == null) return
            VagaDTO dto = JsonHelper.read(req.inputStream, VagaDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados da vaga obrigatórios')
                return
            }

            if (dto.empresa?.id != null && dto.empresa.id != empresa.id) {
                JsonHelper.error(resp, 403, 'A empresa autenticada não pode cadastrar vagas para outra empresa')
                return
            }

            Vaga vaga = new Vaga(
                    nome: dto.nome,
                    descricao: dto.descricao,
                    area: dto.area,
                    empresa: empresa,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade
                    )
            )
            vagaService.salvar(vaga)
            resp.setHeader('Location', "/api/vagas/${vaga.id}")
            JsonHelper.respond(resp, 201, gestao(vaga))

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer id = extrairId(req, resp)
            if (id == null) return
            Empresa empresaAutenticada = empresaAutenticada(req, resp)
            if (empresaAutenticada == null) return

            Vaga vaga = vagaService.buscarPorId(id)
            if (vaga == null) {
                JsonHelper.error(resp, 404, "Vaga ${id} não encontrada")
                return
            }
            if (vaga.empresa?.id != empresaAutenticada.id) {
                JsonHelper.error(resp, 404, 'Vaga não encontrada')
                return
            }

            VagaDTO dto = JsonHelper.read(req.inputStream, VagaDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados da vaga obrigatórios')
                return
            }

            if (dto.empresa?.id != null && dto.empresa.id != empresaAutenticada.id) {
                JsonHelper.error(resp, 403, 'A empresa autenticada não pode editar vagas de outra empresa')
                return
            }

            vaga = new Vaga(
                    id: id,
                    nome: dto.nome,
                    descricao: dto.descricao,
                    area: dto.area,
                    empresa: empresaAutenticada,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade
                    )
            )
            vagaService.atualizar(id, vaga)
            JsonHelper.respond(resp, 200, gestao(vaga))

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.pathInfo

            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return

                Vaga vaga = vagaService.buscarPorId(id)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${id} não encontrada")
                    return
                }
                if (vaga.empresa?.id != empresa.id) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }
                vagaService.excluirVagaDaEmpresa(empresa.id, id)
                resp.status = 204
            } else {
                JsonHelper.error(resp, 400, 'ID é obrigatório na URL')
            }

        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, "ID inválido")
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.pathInfo
            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return

                VagaResumoDTO vaga = vagaService.buscarResumoPublicoPorId(id)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            String empresaIdStr = req.getParameter('empresaId')
            String vagaIdStr = req.getParameter('vagaId')
            String nome = req.getParameter('nome')

            if (empresaIdStr && vagaIdStr) {
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return
                if (empresaId != empresa.id) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }
                Integer vagaId = parseInteger(resp, vagaIdStr, 'vagaId')
                if (vagaId == null) return

                Vaga vaga = vagaService.buscarIdVagaEmpresa(empresaId, vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404,
                            "Vaga ${vagaId} não encontrada para a empresa ${empresaId}")
                    return
                }
                JsonHelper.respond(resp, 200, gestao(vaga))
                return
            }

            if (empresaIdStr && nome && !nome.isBlank()) {
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return
                if (empresaId != empresa.id) {
                    JsonHelper.error(resp, 404, 'Vaga não encontrada')
                    return
                }

                Vaga vaga = vagaService.buscarNomeVagaEmpresa(empresaId, nome)
                if (vaga == null) {
                    JsonHelper.error(resp, 404,
                            "Vaga '${nome}' não encontrada para a empresa ${empresaId}")
                    return
                }
                JsonHelper.respond(resp, 200, gestao(vaga))
                return
            }

            if (empresaIdStr) {
                Empresa empresa = empresaAutenticada(req, resp)
                if (empresa == null) return
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return
                if (empresaId != empresa.id) {
                    JsonHelper.error(resp, 404, 'Vagas não encontradas')
                    return
                }

                List<Vaga> lista = vagaService.listarVagaEmpresa(empresaId)
                JsonHelper.respond(resp, 200, lista.collect { gestao(it) })
                return
            }

            if (nome && !nome.isBlank()) {
                VagaResumoDTO vaga = vagaService.buscarResumoPublicoPorNome(nome)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga com nome '${nome}' não encontrada")
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            List<VagaResumoDTO> lista = vagaService.listarResumosPublicos()
            JsonHelper.respond(resp, 200, lista)

        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private Empresa empresaAutenticada(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        try {
            Empresa empresa = empresaService.buscarEmailEmpresaParaAutorizacao(email)
            if (empresa == null) {
                JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a uma empresa')
            }
            return empresa
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
    }

    private static VagaGestaoDTO gestao(Vaga vaga) {
        return new VagaGestaoDTO(
                id: vaga.id,
                nome: vaga.nome,
                descricao: vaga.descricao,
                area: vaga.area,
                pais: vaga.endereco?.pais,
                estado: vaga.endereco?.estado,
                cidade: vaga.endereco?.cidade
        )
    }

    private Integer parseInteger(HttpServletResponse resp, String valor, String campo) {
        try {
            return valor as Integer
        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, "${campo} inválido: ${valor}")
            return null
        }
    }

    private Integer extrairId(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.pathInfo
        if (pathInfo == null || pathInfo == '/' || pathInfo.isEmpty()) {
            JsonHelper.error(resp, 400, 'ID é obrigatorio na URL')
            return null
        }
        try {
            return pathInfo.substring(1) as Integer
        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, "ID invalido: ${pathInfo.substring(1)}")
            return null
        }
    }
}