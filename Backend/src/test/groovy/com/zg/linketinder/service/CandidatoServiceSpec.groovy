
package com.zg.linketinder.service

import com.zg.linketinder.dao.CandidatoDAO
import com.zg.linketinder.model.Candidato
import spock.lang.Specification

class CandidatoServiceSpec extends Specification {

    CandidatoDAO candidatoDAO = Mock()
    CandidatoService candidatoService = new CandidatoService(candidatoDAO)

    def "deve validar antes de salvar"() {
        given: "um candidato com dados inválidos"
        Candidato candidato = new Candidato(
                cpf: '',
                nome: '',
                email: '',
                senha: '',
                competencias: []
        )

        when: "tento salvar o candidato"
        candidatoService.salvarCandidato(candidato)

        then: "a validação impede o cadastro"
        thrown(Exception)
        0 * candidatoDAO.salvarCandidato(_)
    }

    def "deve buscar cpf candidato"() {
        given: "um candidato cadastrado"
        String cpf = '111.111.111-11'
        Candidato candidato = new Candidato(
                cpf: cpf,
                nome: 'Tulio',
                email: 't@email.com'
        )

        when: "busco o candidato pelo CPF"
        Candidato resultado = candidatoService.buscarCpfCandidato(cpf)

        then: "o candidato é retornado"
        1 * candidatoDAO.buscarCpfCandidato(cpf) >> candidato
        resultado == candidato
    }

    def "deve buscar email candidato"() {
        given: "um candidato cadastrado"
        String email = 't@email.com'
        Candidato candidato = new Candidato(
                cpf: '111.111.111-11',
                nome: 'Tulio',
                email: email
        )

        when: "busco o candidato pelo e-mail"
        Candidato resultado = candidatoService.buscarEmailCandidato(email)

        then: "o candidato é retornado"
        1 * candidatoDAO.buscarEmailCandidato(email) >> candidato
        resultado == candidato
    }
}