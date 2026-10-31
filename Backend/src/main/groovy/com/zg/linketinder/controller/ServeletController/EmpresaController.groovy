package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.dto.EmpresaDTO
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
    private final EmpresaService service = new EmpresaService()

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            EmpresaDTO dto = JsonHelper.read(req.inputStream, EmpresaDTO)

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
            JsonHelper.respond(resp, 201, empresa)

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

            Empresa empresa = service.buscarPorId(id)
            if (empresa == null) {
                JsonHelper.error(resp, 404, "Empresa ${id} não encontrada")
                return
            }

            EmpresaDTO dto = JsonHelper.read(req.inputStream, EmpresaDTO)

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
            JsonHelper.respond(resp, 200, empresa)

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

                Empresa empresa = service.buscarPorId(id)
                if (empresa == null) {
                    JsonHelper.error(resp, 404, "Empresa ${id} não encontrada")
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

                Empresa empresa = service.buscarPorId(id)
                if (empresa == null) {
                    JsonHelper.error(resp, 404, "Empresa ${id} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, empresa)
                return
            }

            String cnpj = req.getParameter('cnpj')
            if (cnpj != null && !cnpj.isBlank()) {
                Empresa empresa = service.buscarCnpjEmpresa(cnpj)
                if (empresa == null) {
                    JsonHelper.error(resp, 404, "Empresa com CNPJ ${cnpj} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, empresa)
                return
            }

            String email = req.getParameter('email')
            if (email != null && !email.isBlank()) {
                Empresa empresa = service.buscarEmailEmpresa(email)
                if (empresa == null) {
                    JsonHelper.error(resp, 404, "Empresa com email ${email} não encontrado")
                    return
                }
                JsonHelper.respond(resp, 200, empresa)
                return
            }

            List<Empresa> lista = service.listarTodos()
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