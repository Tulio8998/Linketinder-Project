package com.zg.linketinder.api

import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class AnonymousVagaTokenStore {
    static final AnonymousVagaTokenStore INSTANCE = new AnonymousVagaTokenStore()

    private static final long TOKEN_TTL_MILLIS = 10 * 60 * 1000L
    private static final SecureRandom SECURE_RANDOM = new SecureRandom()
    private final ConcurrentMap<String, Entry> tokens = new ConcurrentHashMap<>()

    String criar(Integer vagaId) {
        if (vagaId == null || vagaId < 1) throw new IllegalArgumentException('Vaga inválida')
        removerExpirados()
        byte[] random = new byte[32]
        SECURE_RANDOM.nextBytes(random)
        String token = Base64.urlEncoder.withoutPadding().encodeToString(random)
        tokens.put(token, new Entry(vagaId, System.currentTimeMillis() + TOKEN_TTL_MILLIS))
        return token
    }

    private void removerExpirados() {
        if (tokens.size() < 1024) return
        long agora = System.currentTimeMillis()
        tokens.entrySet().removeIf { it.value.expiraEm < agora }
    }

    Integer resolver(String token) {
        if (token == null) return null
        Entry entry = tokens.get(token)
        if (entry == null) return null
        if (entry.expiraEm < System.currentTimeMillis()) {
            tokens.remove(token, entry)
            return null
        }
        return entry.vagaId
    }

    private static class Entry {
        final Integer vagaId
        final long expiraEm

        Entry(Integer vagaId, long expiraEm) {
            this.vagaId = vagaId
            this.expiraEm = expiraEm
        }
    }
}
