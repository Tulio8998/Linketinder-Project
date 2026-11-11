package com.zg.linketinder.service

import com.zg.linketinder.dao.interfaces.VagaDAO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import spock.lang.Specification

class VagaServiceSpec extends Specification {
    VagaDAO dao = Mock()
    VagaService service = new VagaService(dao)

    def "normaliza a área antes de persistir a vaga"() {
        given:
        Vaga vaga = new Vaga(
                nome: 'Engenharia',
                descricao: 'Serviços',
                area: ' ti ',
                empresa: new Empresa(id: 7)
        )

        when:
        service.salvar(vaga)

        then:
        1 * dao.buscarNomeVagaEmpresa(7, 'Engenharia') >> null
        1 * dao.salvar({ Vaga salva -> salva.area == 'TI' })
    }

    def "rejeita vaga sem área sem consultar ou persistir"() {
        given:
        Vaga vaga = new Vaga(
                nome: 'Engenharia',
                descricao: 'Serviços',
                area: ' ',
                empresa: new Empresa(id: 7)
        )

        when:
        service.salvar(vaga)

        then:
        0 * dao._
        thrown(IllegalArgumentException)
    }
}
