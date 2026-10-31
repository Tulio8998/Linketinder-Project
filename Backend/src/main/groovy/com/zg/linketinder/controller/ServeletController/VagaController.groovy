package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.dto.VagaDTO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.VagaService
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class VagaController extends HttpServlet {

    private final VagaService vagaService = new VagaService()
    private final EmpresaService empresaService = new EmpresaService()

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            VagaDTO dto = JsonHelper.read(req.inputStream, VagaDTO)

            if (dto.empresa?.id == null) {
                JsonHelper.error(resp, 400, 'Campo obrigatório: empresa.id')
                return
            }

            Empresa empresa = empresaService.buscarPorId(dto.empresa.id)
            if (empresa == null) {
                JsonHelper.error(resp, 404, "Empresa ${dto.empresa.id} não encontrada")
                return
            }

            Vaga vaga = new Vaga(
                    nome: dto.nome,
                    descricao: dto.descricao,
                    empresa: empresa,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade
                    )
            )
            vagaService.salvar(vaga)
            resp.setHeader('Location', "/api/vagas/${vaga.id}")
            JsonHelper.respond(resp, 201, vaga)

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer id = extrairId(req, resp)
            if (id == null) return

            Vaga vaga = vagaService.buscarPorId(id)
            if (vaga == null) {
                JsonHelper.error(resp, 404, "Vaga ${id} não encontrada")
                return
            }

            VagaDTO dto = JsonHelper.read(req.inputStream, VagaDTO)

            if (dto.empresa?.id == null) {
                JsonHelper.error(resp, 400, 'Campo obrigatório: empresa.id')
                return
            }

            Empresa empresa = empresaService.buscarPorId(dto.empresa.id)
            if (empresa == null) {
                JsonHelper.error(resp, 404, "Empresa ${dto.empresa.id} não encontrada")
                return
            }

            vaga = new Vaga(
                    id: id,
                    nome: dto.nome,
                    descricao: dto.descricao,
                    empresa: empresa,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade
                    )
            )
            vagaService.atualizar(id, vaga)
            JsonHelper.respond(resp, 200, vaga)

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
            String pathInfo = req.pathInfo

            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return

                Vaga vaga = vagaService.buscarPorId(id)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${id} não encontrada")
                    return
                }
                vagaService.excluirVagaDaEmpresa(vaga.empresa.id, id)
                resp.status = 204
            } else {
                JsonHelper.error(resp, 400, 'ID é obrigatório na URL')
            }

        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, "ID inválido")
        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.pathInfo
            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return

                Vaga vaga = vagaService.buscarPorId(id)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga ${id} não encontrada")
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            String empresaIdStr = req.getParameter('empresaId')
            String vagaIdStr = req.getParameter('vagaId')
            String nome = req.getParameter('nome')

            if (empresaIdStr && vagaIdStr) {
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return
                Integer vagaId = parseInteger(resp, vagaIdStr, 'vagaId')
                if (vagaId == null) return

                Vaga vaga = vagaService.buscarIdVagaEmpresa(empresaId, vagaId)
                if (vaga == null) {
                    JsonHelper.error(resp, 404,
                            "Vaga ${vagaId} não encontrada para a empresa ${empresaId}")
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            if (empresaIdStr && nome && !nome.isBlank()) {
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return

                Vaga vaga = vagaService.buscarNomeVagaEmpresa(empresaId, nome)
                if (vaga == null) {
                    JsonHelper.error(resp, 404,
                            "Vaga '${nome}' não encontrada para a empresa ${empresaId}")
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            if (empresaIdStr) {
                Integer empresaId = parseInteger(resp, empresaIdStr, 'empresaId')
                if (empresaId == null) return

                List<Vaga> lista = vagaService.listarVagaEmpresa(empresaId)
                JsonHelper.respond(resp, 200, lista)
                return
            }

            if (nome && !nome.isBlank()) {
                Vaga vaga = vagaService.buscarNomeVaga(nome)
                if (vaga == null) {
                    JsonHelper.error(resp, 404, "Vaga com nome '${nome}' não encontrada")
                    return
                }
                JsonHelper.respond(resp, 200, vaga)
                return
            }

            List<Vaga> lista = vagaService.listarTodos()
            JsonHelper.respond(resp, 200, lista)

        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
        }
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