package com.zg.linketinder.controller.ServeletController

import com.zg.linketinder.api.JsonHelper
import com.zg.linketinder.api.ServletIdentity
import com.zg.linketinder.dto.AvaliacaoRecrutadorRequestDTO
import com.zg.linketinder.dto.AvaliacaoRecrutadorDTO
import com.zg.linketinder.dto.CandidatoAnonimoDTO
import com.zg.linketinder.dto.EmpresaCnpjDTO
import com.zg.linketinder.dto.RecrutadorDTO
import com.zg.linketinder.dto.RecrutadorResumoDTO
import com.zg.linketinder.dto.VagaTriagemDTO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.RecrutadorService
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


class RecrutadorController extends HttpServlet {
    private final RecrutadorService recrutadorService
    private final EmpresaService empresaService
    private final CandidatoService candidatoService

    RecrutadorController() {
        this(new RecrutadorService(), new EmpresaService(), new CandidatoService())
    }

    RecrutadorController(
            RecrutadorService recrutadorService,
            EmpresaService empresaService,
            CandidatoService candidatoService
    ) {
        this.recrutadorService = recrutadorService
        this.empresaService = empresaService
        this.candidatoService = candidatoService
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.pathInfo ?: '/'
        if (path == '/empresa/recrutadores') {
            cadastrar(req, resp)
            return
        }
        if (path == '/me/avaliacoes') {
            avaliar(req, resp)
            return
        }
        JsonHelper.error(resp, 404, 'Rota não encontrada')
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.pathInfo ?: '/'
        if (path == '/me') {
            Recrutador recrutador = recrutadorAutenticado(req, resp)
            if (recrutador != null) JsonHelper.respond(resp, 200, resumo(recrutador))
            return
        }
        if (path == '/empresa/recrutadores') {
            listarDaEmpresa(req, resp)
            return
        }
        if (path == '/me/vagas') {
            listarVagas(req, resp)
            return
        }
        if (path.startsWith('/me/vagas/') && path.endsWith('/candidatos')) {
            listarCandidatos(req, resp, path)
            return
        }
        if (path == '/me/historico') {
            listarHistorico(req, resp)
            return
        }
        if (path == '/matches/candidato') {
            listarMatchesCandidato(req, resp)
            return
        }
        JsonHelper.error(resp, 404, 'Rota não encontrada')
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.pathInfo ?: ''
        if (!path.startsWith('/empresa/recrutadores/')) {
            JsonHelper.error(resp, 404, 'Rota não encontrada')
            return
        }

        Empresa empresa = empresaAutenticada(req, resp)
        if (empresa == null) return

        Integer id = parseId(path.substring('/empresa/recrutadores/'.length()), resp)
        if (id == null) return
        try {
            recrutadorService.desativar(id, empresa.id)
            resp.status = 204
        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 404, e.message)
        }
    }

    private void cadastrar(HttpServletRequest req, HttpServletResponse resp) {
        Empresa empresa = empresaAutenticada(req, resp)
        if (empresa == null) return
        try {
            RecrutadorDTO dto = JsonHelper.read(req.inputStream, RecrutadorDTO)
            if (dto == null) {
                JsonHelper.error(resp, 400, 'Dados do recrutador obrigatórios')
                return
            }
            Recrutador recrutador = new Recrutador(
                    empresa: empresa,
                    nome: dto.nome,
                    area: dto.area,
                    descricao: dto.descricao,
                    email: new EmailVO(dto.email),
                    cpf: new CpfVO(dto.cpf),
                    dataNascimento: dto.dataNascimento,
                    senha: dto.senha
            )
            recrutadorService.cadastrar(recrutador)
            resp.setHeader('Location', "/api/recrutadores/empresa/recrutadores/${recrutador.id}")
            JsonHelper.respond(resp, 201, resumo(recrutador))
        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void listarDaEmpresa(HttpServletRequest req, HttpServletResponse resp) {
        Empresa empresa = empresaAutenticada(req, resp)
        if (empresa == null) return
        try {
            List<RecrutadorResumoDTO> lista = recrutadorService.listarPorEmpresa(empresa.id)
                    .collect { resumo(it) }
            JsonHelper.respond(resp, 200, lista)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void listarVagas(HttpServletRequest req, HttpServletResponse resp) {
        Recrutador recrutador = recrutadorAutenticado(req, resp)
        if (recrutador == null) return
        try {
            List<VagaTriagemDTO> vagas = recrutadorService.listarVagasDaArea(recrutador.id)
            JsonHelper.respond(resp, 200, vagas)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void listarCandidatos(HttpServletRequest req, HttpServletResponse resp, String path) {
        Recrutador recrutador = recrutadorAutenticado(req, resp)
        if (recrutador == null) return
        String rawId = path.substring('/me/vagas/'.length(), path.length() - '/candidatos'.length())
        Integer vagaId = parseId(rawId, resp)
        if (vagaId == null) return
        try {
            List<CandidatoAnonimoDTO> candidatos =
                    recrutadorService.listarCandidatosAnonimos(recrutador.id, vagaId)
            JsonHelper.respond(resp, 200, candidatos)
        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 404, 'Vaga de triagem não encontrada')
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void avaliar(HttpServletRequest req, HttpServletResponse resp) {
        Recrutador recrutador = recrutadorAutenticado(req, resp)
        if (recrutador == null) return
        try {
            AvaliacaoRecrutadorRequestDTO dto =
                    JsonHelper.read(req.inputStream, AvaliacaoRecrutadorRequestDTO)
            if (dto == null || dto.candidatoId == null || dto.vagaId == null || dto.curtiu == null) {
                JsonHelper.error(resp, 400, 'Campos obrigatórios: candidatoId, vagaId, curtiu')
                return
            }
            boolean inserida = recrutadorService.registrarAvaliacao(
                    recrutador.id, dto.candidatoId, dto.vagaId, dto.curtiu)
            if (!inserida) {
                JsonHelper.error(resp, 409, 'A vaga já possui uma avaliação para este candidato')
                return
            }
            JsonHelper.respond(resp, 201, [mensagem: 'Avaliação registrada'])
        } catch (IllegalArgumentException e) {
            JsonHelper.error(resp, 400, e.message)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void listarHistorico(HttpServletRequest req, HttpServletResponse resp) {
        Recrutador recrutador = recrutadorAutenticado(req, resp)
        if (recrutador == null) return
        try {
            List<AvaliacaoRecrutadorDTO> historico =
                    recrutadorService.listarHistorico(recrutador.id)
            JsonHelper.respond(resp, 200, historico)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private void listarMatchesCandidato(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return
        def candidato
        try {
            candidato = candidatoService.buscarEmailCandidatoParaAutorizacao(email)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return
        }
        if (candidato == null) {
            JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a um candidato')
            return
        }
        try {
            JsonHelper.respond(resp, 200, recrutadorService.listarMatchesCandidato(candidato.id))
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
        }
    }

    private Empresa empresaAutenticada(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        Empresa empresa
        try {
            empresa = empresaService.buscarEmailEmpresaParaAutorizacao(email)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
        if (empresa == null) {
            JsonHelper.error(resp, 403, 'Identidade autenticada não corresponde a uma empresa')
            return null
        }
        return empresa
    }

    private Recrutador recrutadorAutenticado(HttpServletRequest req, HttpServletResponse resp) {
        String email = ServletIdentity.requirePrincipalEmail(req, resp)
        if (email == null) return null
        Recrutador recrutador
        try {
            recrutador = recrutadorService.buscarPorEmail(email)
        } catch (Exception e) {
            JsonHelper.internalError(resp, e)
            return null
        }
        if (recrutador == null || recrutador.ativo != true) {
            JsonHelper.error(resp, 403, 'Recrutador não encontrado ou inativo')
            return null
        }
        return recrutador
    }

    private static RecrutadorResumoDTO resumo(Recrutador recrutador) {
        return new RecrutadorResumoDTO(
                id: recrutador.id,
                nome: recrutador.nome,
                area: recrutador.area,
                descricao: recrutador.descricao,
                email: recrutador.email?.valor,
                ativo: recrutador.ativo,
                empresa: recrutador.empresa?.cnpj?.valor == null
                        ? null
                        : new EmpresaCnpjDTO(cnpj: recrutador.empresa.cnpj.valor)
        )
    }

    private static Integer parseId(String rawId, HttpServletResponse resp) {
        try {
            Integer id = rawId as Integer
            if (id < 1) throw new NumberFormatException()
            return id
        } catch (NumberFormatException ignored) {
            JsonHelper.error(resp, 400, 'ID inválido')
            return null
        }
    }
}
