package com.zg.linketinder.api

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.service.RecrutadorService
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse

import java.nio.charset.StandardCharsets
import java.security.Principal
import java.util.Base64

class AuthFilter implements Filter {
    private static final String DEFAULT_FRONTEND_ORIGIN = 'http://localhost:5173'

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request
        HttpServletResponse resp = (HttpServletResponse) response
        configurarCors(req, resp)

        if (req.method == 'OPTIONS') {
            resp.status = HttpServletResponse.SC_NO_CONTENT
            return
        }

        String authHeader = req.getHeader('Authorization')
        if (authHeader == null) {
            chain.doFilter(request, response)
            return
        }

        String[] credentials = decodificarCredenciais(authHeader)
        if (credentials == null || !credenciaisValidas(credentials[0], credentials[1])) {
            resp.setHeader('WWW-Authenticate', 'Basic realm="Linketinder"')
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, 'Credenciais inválidas')
            return
        }

        String email = credentials[0]
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(req) {
            @Override
            Principal getUserPrincipal() {
                return () -> email
            }
        }
        chain.doFilter(wrapper, response)
    }

    private static void configurarCors(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader('Origin')
        String permitido = System.getProperty('linketinder.frontend.origin', DEFAULT_FRONTEND_ORIGIN)
        if (origin == permitido || origin == 'http://127.0.0.1:5173') {
            response.setHeader('Access-Control-Allow-Origin', origin)
            response.setHeader('Vary', 'Origin')
            response.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
            response.setHeader('Access-Control-Allow-Headers', 'Authorization, Content-Type')
            response.setHeader('Access-Control-Max-Age', '600')
        }
    }

    private static String[] decodificarCredenciais(String header) {
        if (!header.startsWith('Basic ')) return null
        try {
            String decoded = new String(
                    Base64.decoder.decode(header.substring(6)),
                    StandardCharsets.UTF_8
            )
            int separador = decoded.indexOf(':')
            if (separador < 1) return null
            return [decoded.substring(0, separador), decoded.substring(separador + 1)] as String[]
        } catch (IllegalArgumentException ignored) {
            return null
        }
    }

    private static boolean credenciaisValidas(String email, String senha) {
        Candidato candidato = new CandidatoService().buscarEmailCandidatoParaAutorizacao(email)
        Empresa empresa = new EmpresaService().buscarEmailEmpresaParaAutorizacao(email)
        Recrutador recrutador = new RecrutadorService().buscarPorEmail(email)

        List<String> senhasEncontradas = []
        if (candidato != null) senhasEncontradas.add(candidato.senha)
        if (empresa != null) senhasEncontradas.add(empresa.senha)
        if (recrutador != null && recrutador.ativo == true) {
            senhasEncontradas.add(recrutador.senha)
        }
        return senhasEncontradas.size() == 1 && senhasEncontradas.first() == senha
    }
}
