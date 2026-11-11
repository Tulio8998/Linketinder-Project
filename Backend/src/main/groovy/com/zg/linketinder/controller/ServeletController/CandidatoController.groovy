package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.dto.CandidatoPerfilDTO
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
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados do candidato obrigatórios')
                return
            }

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
            JsonHelper.respond(resp, 201, [id: candidato.id])

        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            Integer id = extrairId(req, resp)
            if (id == null) return

            Candidato autenticado = candidatoAutenticado(req, resp)
            if (autenticado == null) return
            if (autenticado.id != id) {
                JsonHelper.error(resp, 404, 'Candidato não encontrado')
                return
            }

            CandidatoDTO dto = JsonHelper.read(req.inputStream, CandidatoDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados do candidato obrigatórios')
                return
            }

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
            JsonHelper.respond(resp, 200, [id: candidato.id])

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

                Candidato autenticado = candidatoAutenticado(req, resp)
                if (autenticado == null) return
                if (autenticado.id != id) {
                    JsonHelper.error(resp, 404, 'Candidato não encontrado')
                    return
                }
                service.excluir(id)
                resp.status = 204
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
            Candidato candidato = candidatoAutenticado(req, resp)
            if (candidato == null) return
            String pathInfo = req.pathInfo

            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return
                if (candidato.id != id) {
                    JsonHelper.error(resp, 404, 'Candidato não encontrado')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(candidato))
                return
            }

            String cpf = req.getParameter('cpf')
            if (cpf != null && !cpf.isBlank()) {
                if (candidato.cpf?.valor != cpf) {
                    JsonHelper.error(resp, 404, 'Candidato não encontrado')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(candidato))
                return
            }

            String email = req.getParameter('email')
            if (email != null && !email.isBlank()) {
                if (!candidato.email?.valor?.equalsIgnoreCase(email)) {
                    JsonHelper.error(resp, 404, 'Candidato não encontrado')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(candidato))
                return
            }

            String empresaIdStr = req.getParameter('empresaId')
            if (empresaIdStr != null && !empresaIdStr.isBlank()) {
                JsonHelper.error(resp, 403, 'Listagem de candidatos por empresa não está disponível')
                return
            }

            JsonHelper.respond(resp, 200, perfil(candidato))

        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
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

    private Candidato candidatoAutenticado(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        try {
            Candidato candidato = service.buscarEmailCandidatoParaAutorizacao(email)
            if (candidato == null) {
                JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a um candidato')
            }
            return candidato
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
    }

    private static CandidatoPerfilDTO perfil(Candidato candidato) {
        return new CandidatoPerfilDTO(
                id: candidato.id,
                nome: candidato.nome,
                email: candidato.email?.valor,
                cpf: candidato.cpf?.valor,
                dataNascimento: candidato.dataNascimento,
                descricao: candidato.descricao,
                endereco: candidato.endereco
        )
    }
}