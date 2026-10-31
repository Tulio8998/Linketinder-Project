package com.zg.linketinder.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.http.HttpServletResponse

class JsonHelper {

    static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

    static <T> T read(java.io.InputStream input, Class<T> tipo) {
        return MAPPER.readValue(input, tipo)
    }

    static void respond(HttpServletResponse resp, int status, Object corpo) {
        resp.status = status
        resp.contentType = 'application/json;charset=UTF-8'
        MAPPER.writeValue(resp.outputStream, corpo)
    }

    static void error(HttpServletResponse resp, int status, String mensagem) {
        respond(resp, status, [status: status, message: mensagem])
    }
}