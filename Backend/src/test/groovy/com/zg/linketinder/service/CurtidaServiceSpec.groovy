package com.zg.linketinder.service

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import spock.lang.Specification

class CurtidaServiceSpec extends Specification {
    def "empresa não pode avaliar diretamente sem autoria de recrutador"() {
        when:
        new CurtidaService().curtirVagaComoEmpresa(
                new Empresa(id: 1),
                new Candidato(id: 2),
                new Vaga(id: 3),
                true
        )

        then:
        IllegalArgumentException exception = thrown()
        exception.message.contains('recrutador')
    }
}
