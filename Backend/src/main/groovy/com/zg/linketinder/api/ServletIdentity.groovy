package com.zg.linketinder.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class ServletIdentity {
    static String requirePrincipalEmail(HttpServletRequest request, HttpServletResponse response) {
        String email = request.userPrincipal?.name?.trim()
        if (email == null || email.isEmpty()) {
            JsonHelper.error(response, 401, 'Autenticação obrigatória')
            return null
        }
        return email
    }
}
