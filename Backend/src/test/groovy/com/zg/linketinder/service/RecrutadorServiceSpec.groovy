package com.zg.linketinder.service

import com.zg.linketinder.dao.interfaces.RecrutadorDAO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO
import spock.lang.Specification

import java.time.LocalDate

class RecrutadorServiceSpec extends Specification {
    RecrutadorDAO dao = Mock()
    RecrutadorService service = new RecrutadorService(dao)

    def "normaliza e cadastra recrutador ativo vinculado a empresa"() {
        given:
        Recrutador recrutador = novoRecrutador()

        when:
        service.cadastrar(recrutador)

        then:
        1 * dao.buscarPorEmail('Recruiter@Example.com') >> null
        1 * dao.existeCpf('123.456.789-09') >> false
        1 * dao.salvar({ Recrutador salvo ->
            salvo.empresa.id == 5 &&
                    salvo.area == 'TI' &&
                    salvo.email.valor == 'recruiter@example.com' &&
                    salvo.ativo &&
                    salvo.senha == 'senha-definida-pela-regra'
        })
    }

    def "rejeita e-mail corporativo duplicado sem persistir"() {
        given:
        Recrutador recrutador = novoRecrutador()

        when:
        service.cadastrar(recrutador)

        then:
        1 * dao.buscarPorEmail('Recruiter@Example.com') >> new Recrutador()
        0 * dao.existeCpf(_)
        0 * dao.salvar(_)
        thrown(IllegalArgumentException)
    }

    def "rejeita área ausente antes de consultar ou persistir"() {
        given:
        Recrutador recrutador = novoRecrutador(area: ' ')

        when:
        service.cadastrar(recrutador)

        then:
        0 * dao._
        thrown(IllegalArgumentException)
    }

    def "preserva a primeira avaliação e propaga o resultado do DAO"() {
        when:
        boolean registrada = service.registrarAvaliacao(1, 2, 3, false)

        then:
        1 * dao.registrarAvaliacao(1, 2, 3, false) >> false
        !registrada
    }

    def "não aceita avaliação sem decisão booleana"() {
        when:
        service.registrarAvaliacao(1, 2, 3, null)

        then:
        0 * dao._
        thrown(IllegalArgumentException)
    }

    def "desativação lógica exige recrutador e empresa válidos"() {
        when:
        service.desativar(7, 5)

        then:
        1 * dao.desativar(7, 5) >> false
        thrown(IllegalArgumentException)
    }

    private static Recrutador novoRecrutador(Map overrides = [:]) {
        Map valores = [
                empresa: new Empresa(id: 5),
                nome: 'Recrutadora de TI',
                area: ' ti ',
                descricao: 'Seleção de engenharia',
                email: new EmailVO('Recruiter@Example.com'),
                cpf: new CpfVO('123.456.789-09'),
                dataNascimento: LocalDate.of(1990, 1, 1),
                senha: 'senha-definida-pela-regra'
        ] + overrides
        return new Recrutador(valores)
    }
}
