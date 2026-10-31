package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CandidatoController extends HttpServlet {
    private final CandidatoService service = new CandidatoService()

    CandidatoController() {
        this(new CandidatoService())
    }

    CandidatoController(CandidatoService service) {
        this.service = service
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CandidatoDTO dto = JsonHelper.read(req.inputStream, CandidatoDTO)

            Candidato candidato = new Candidato(
                    cpf: new CpfVO(dto.cpf),
                    nome: dto.nome,
                    email: new EmailVO(dto.email),
                    senha: dto.senha,
                    dataNascimento: dto.dataNascimento,
                    descricao: dto.descricao,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade,
                            cep: dto.cep
                    )
            )
            service.salvar(candidato)
            resp.setHeader('Location', "/api/candidatos/${candidato.id}")
            JsonHelper.respond(resp, 201, candidato)

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            Integer id = extrairId(req, resp)
            if (id == null) return

            Candidato candidato = service.buscarPorId(id)
            if (candidato == null) {
                JsonHelper.error(resp, 404, "Candidato ${id} não encontrado")
                return
            }

            CandidatoDTO dto = JsonHelper.read(req.inputStream, CandidatoDTO)

            candidato = new Candidato(
                    id: id,
                    cpf: new CpfVO(dto.cpf),
                    nome: dto.nome,
                    email: new EmailVO(dto.email),
                    senha: dto.senha,
                    dataNascimento: dto.dataNascimento,
                    descricao: dto.descricao,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade,
                            cep: dto.cep
                    )
            )
            service.atualizar(id, candidato)
            JsonHelper.respond(resp, 200, candidato)

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

                Candidato candidato = service.buscarPorId(id)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato ${id} não encontrado")
                    return
                }
                service.excluir(id)
                resp.status = 204
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

                Candidato candidato = service.buscarPorId(id)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato ${id} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, candidato)
                return
            }

            String cpf = req.getParameter('cpf')
            if (cpf != null && !cpf.isBlank()) {
                Candidato candidato = service.buscarCpfCandidato(cpf)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato com CPF ${cpf} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, candidato)
                return
            }

            String email = req.getParameter('email')
            if (email != null && !email.isBlank()) {
                Candidato candidato = service.buscarEmailCandidato(email)
                if (candidato == null) {
                    JsonHelper.error(resp, 404, "Candidato com email ${email} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, candidato)
                return
            }

            String empresaIdStr = req.getParameter('empresaId')
            if (empresaIdStr != null && !empresaIdStr.isBlank()) {
                Integer empresaId
                try {
                    empresaId = empresaIdStr as Integer
                } catch (NumberFormatException ignored) {
                    JsonHelper.error(resp, 400, "empresaId inválido: ${empresaIdStr}")
                    return
                }

                List<Candidato> lista = service.listarCandidatoEmpresa(empresaId)
                JsonHelper.respond(resp, 200, lista)
                return
            }

            List<Candidato> lista = service.listarTodos()
            JsonHelper.respond(resp, 200, lista)

        } catch (Exception e) {
            e.printStackTrace()
            JsonHelper.error(resp, 500, 'Erro interno do servidor')
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