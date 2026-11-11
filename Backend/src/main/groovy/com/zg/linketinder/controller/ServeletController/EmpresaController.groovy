package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.dto.EmpresaDTO
import com.zg.linketinder.dto.EmpresaPerfilDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.vo.CnpjVO
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class EmpresaController extends HttpServlet {
    private final EmpresaService service

    EmpresaController() {
        this(new EmpresaService())
    }

    EmpresaController(EmpresaService service) {
        this.service = service
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            EmpresaDTO dto = JsonHelper.read(req.inputStream, EmpresaDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados da empresa obrigatórios')
                return
            }

            Empresa empresa = new Empresa(
                    cnpj: new CnpjVO(dto.cnpj),
                    nome: dto.nome,
                    email: new EmailVO(dto.email),
                    senha: dto.senha,
                    descricao: dto.descricao,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade,
                            cep: dto.cep
                    )
            )
            service.salvar(empresa)
            resp.setHeader('Location', "/api/empresas/${empresa.id}")
            JsonHelper.respond(resp, 201, [id: empresa.id])

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

            Empresa autenticada = empresaAutenticada(req, resp)
            if (autenticada == null) return
            if (autenticada.id != id) {
                JsonHelper.error(resp, 404, 'Empresa não encontrada')
                return
            }

            EmpresaDTO dto = JsonHelper.read(req.inputStream, EmpresaDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados da empresa obrigatórios')
                return
            }

            empresa = new Empresa(
                    id: id,
                    cnpj: new CnpjVO(dto.cnpj),
                    nome: dto.nome,
                    email: new EmailVO(dto.email),
                    senha: dto.senha,
                    descricao: dto.descricao,
                    endereco: new Endereco(
                            pais: dto.pais,
                            estado: dto.estado,
                            cidade: dto.cidade,
                            cep: dto.cep
                    )
            )
            service.atualizar(id, empresa)
            JsonHelper.respond(resp, 200, [id: empresa.id])

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

                Empresa autenticada = empresaAutenticada(req, resp)
                if (autenticada == null) return
                if (autenticada.id != id) {
                    JsonHelper.error(resp, 404, 'Empresa não encontrada')
                    return
                }
                service.excluir(autenticada.id)
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
            Empresa autenticada = empresaAutenticada(req, resp)
            if (autenticada == null) return
            String pathInfo = req.pathInfo

            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo != '/') {
                Integer id = extrairId(req, resp)
                if (id == null) return
                if (autenticada.id != id) {
                    JsonHelper.error(resp, 404, 'Empresa não encontrada')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(autenticada))
                return
            }

            String cnpj = req.getParameter('cnpj')
            if (cnpj != null && !cnpj.isBlank()) {
                if (autenticada.cnpj?.valor != cnpj) {
                    JsonHelper.error(resp, 404, 'Empresa não encontrada')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(autenticada))
                return
            }

            String email = req.getParameter('email')
            if (email != null && !email.isBlank()) {
                if (!autenticada.email?.valor?.equalsIgnoreCase(email)) {
                    JsonHelper.error(resp, 404, 'Empresa não encontrada')
                    return
                }
                JsonHelper.respond(resp, 200, perfil(autenticada))
                return
            }

            JsonHelper.respond(resp, 200, perfil(autenticada))

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

    private Empresa empresaAutenticada(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        try {
            Empresa empresa = service.buscarEmailEmpresaParaAutorizacao(email)
            if (empresa == null) {
                JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a uma empresa')
            }
            return empresa
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
    }

    private static EmpresaPerfilDTO perfil(Empresa empresa) {
        return new EmpresaPerfilDTO(
                id: empresa.id,
                nome: empresa.nome,
                email: empresa.email?.valor,
                cnpj: empresa.cnpj?.valor,
                descricao: empresa.descricao,
                endereco: empresa.endereco
        )
    }
}