package com.zg.linketinder.api

import spock.lang.Specification

class AnonymousVagaTokenStoreSpec extends Specification {
    def "token de vaga é opaco e resolve somente o identificador interno no servidor"() {
        given:
        AnonymousVagaTokenStore store = AnonymousVagaTokenStore.INSTANCE

        when:
        String token = store.criar(483920)

        then:
        token.length() >= 40
        !token.contains('483920')
        store.resolver(token) == 483920
        store.resolver('483920') == null
    }
}
