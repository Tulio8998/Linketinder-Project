package com.zg.linketinder.api

import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ReviewTokenStore {
    private static final long TOKEN_TTL_MILLIS = 10 * 60 * 1000L
    private static final SecureRandom SECURE_RANDOM = new SecureRandom()
    private final ConcurrentMap<String, ReviewContext> tokens = new ConcurrentHashMap<>()

    String criarTokenVaga(Integer recrutadorId, Integer vagaId) {
        return criarToken(new ReviewContext(recrutadorId, vagaId, null, System.currentTimeMillis() + TOKEN_TTL_MILLIS))
    }

    String criarTokenCandidato(Integer recrutadorId, Integer vagaId, Integer candidatoId) {
        return criarToken(new ReviewContext(recrutadorId, vagaId, candidatoId,
                System.currentTimeMillis() + TOKEN_TTL_MILLIS))
    }

    ReviewContext buscar(String token, Integer recrutadorId) {
        if (token == null || recrutadorId == null) return null
        ReviewContext context = tokens.get(token)
        if (context == null) return null
        if (context.expiraEm < System.currentTimeMillis()) {
            tokens.remove(token, context)
            return null
        }
        return context.recrutadorId == recrutadorId ? context : null
    }

    void remover(String token, ReviewContext context) {
        if (token != null && context != null) tokens.remove(token, context)
    }

    private String criarToken(ReviewContext context) {
        removerExpirados()
        byte[] random = new byte[32]
        SECURE_RANDOM.nextBytes(random)
        String token = Base64.urlEncoder.withoutPadding().encodeToString(random)
        tokens.put(token, context)
        return token
    }

    private void removerExpirados() {
        if (tokens.size() < 1024) return
        long agora = System.currentTimeMillis()
        tokens.entrySet().removeIf { it.value.expiraEm < agora }
    }

    static class ReviewContext {
        final Integer recrutadorId
        final Integer vagaId
        final Integer candidatoId
        final long expiraEm

        ReviewContext(Integer recrutadorId, Integer vagaId, Integer candidatoId, long expiraEm) {
            this.recrutadorId = recrutadorId
            this.vagaId = vagaId
            this.candidatoId = candidatoId
            this.expiraEm = expiraEm
        }

        boolean isVagaToken() { candidatoId == null }
    }
}
