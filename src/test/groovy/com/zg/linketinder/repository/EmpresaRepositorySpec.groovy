package com.zg.linketinder.repository

import com.zg.linketinder.model.Empresa
import spock.lang.Specification

class EmpresaRepositorySpec extends Specification {

    def "deve adicionar uma nova empresa ao array"() {
        given: "um repository de empresas"
        def repository = new EmpresaRepository()

        and: "uma nova empresa"
        def empresa = new Empresa(
                cnpj: "55.555.555/5555-55",
                pais: "Brasil",
                nome: "Nova Empresa",
                email: "nova@teste.com",
                estado: "MG",
                cep: "55555-555",
                descricao: "Nova empresa"
        )
        def tamanhoInicial = repository.listarTodos().size()

        when: "uma nova empresa é adicionada ao repository"
        repository.adicionarEmpresa(empresa)

        then: "o tamanho da lista deve aumentar em um"
        repository.listarTodos().size() == tamanhoInicial + 1
    }

    def "deve listar todas as empresas"() {
        given: "um repository de empresas"
        def repository = new EmpresaRepository()

        when: "todas as empresas são solicitadas"
        def empresas = repository.listarTodos()

        then: "deve retornar as cinco empresas cadastradas"
        empresas.size() == 5
    }
}