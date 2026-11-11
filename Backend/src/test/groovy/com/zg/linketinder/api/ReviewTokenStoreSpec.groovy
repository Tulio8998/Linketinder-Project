package com.zg.linketinder.api

import spock.lang.Specification

class ReviewTokenStoreSpec extends Specification {
    def "tokens de triagem são opacos e vinculados ao recrutador"() {
        given:
        ReviewTokenStore store = new ReviewTokenStore()

        when:
        String token = store.criarTokenCandidato(4, 25, 897234)

        then:
        token.length() >= 40
        !token.contains('897234')
        store.buscar(token, 4)?.candidatoId == 897234
        store.buscar(token, 9) == null
    }

    def "token de vaga não pode ser usado como avaliação"() {
        given:
        ReviewTokenStore store = new ReviewTokenStore()

        when:
        String token = store.criarTokenVaga(4, 25)

        then:
        store.buscar(token, 4).isVagaToken()
        store.buscar(token, 4).candidatoId == null
    }
}
