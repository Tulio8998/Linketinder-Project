package com.zg.linketinder.api

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

class AuthFilterSpec extends Specification {
    def "responde ao preflight CORS do frontend local"() {
        given:
        AuthFilter filter = new AuthFilter()
        HttpServletRequest request = Mock()
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * request.getMethod() >> 'OPTIONS'
        1 * request.getHeader('Origin') >> 'http://localhost:5173'
        1 * response.setHeader('Access-Control-Allow-Origin', 'http://localhost:5173')
        1 * response.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        1 * response.setHeader('Access-Control-Allow-Headers', 'Authorization, Content-Type')
        1 * response.setStatus(HttpServletResponse.SC_NO_CONTENT)
        0 * chain._
    }

    def "rejeita cabeçalho Basic malformado sem invocar o controller"() {
        given:
        AuthFilter filter = new AuthFilter()
        HttpServletRequest request = Mock()
        HttpServletResponse response = Mock()
        FilterChain chain = Mock()

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * request.getMethod() >> 'GET'
        1 * request.getHeader('Origin') >> null
        1 * request.getHeader('Authorization') >> 'Basic YWJj'
        1 * response.setHeader('WWW-Authenticate', 'Basic realm="Linketinder"')
        1 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 'Credenciais inválidas')
        0 * chain._
    }

}
