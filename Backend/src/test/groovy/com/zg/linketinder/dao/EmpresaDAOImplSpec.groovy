package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.vo.CnpjVO
import com.zg.linketinder.vo.EmailVO
import spock.lang.Specification

import java.time.LocalDate

class EmpresaDAOImplSpec extends DaoIntegrationSpec {
    EmpresaDAO empresaDAO = new EmpresaDAOImpl()

    def setup() {
        limparBanco()
    }

    def "deve buscar o cnpj da empresa"() {
        given: "uma empresa salvo"
        def empresa = criarEmpresa('11.111.111/1111-11', 'ACZG', 'a@email.com')
        empresaDAO.salvar(empresa)

        when: "busco pelo cnpj"
        def encontrado = empresaDAO.buscarCnpjEmpresa('11.111.111/1111-11')

        then: "encontra a empresa"
        encontrado != null
        encontrado.nome == 'ACZG'
    }

    def "deve buscar email da empresa"() {
        given: "uma empresa salvo"
        def empresa = criarEmpresa('11.111.111/1111-11', 'ACZG', 'a@email.com')
        empresaDAO.salvar(empresa)

        when: "busco pelo email"
        def encontrado = empresaDAO.buscarEmailEmpresa('a@email.com')

        then: "encontra a empresa"
        encontrado != null
        encontrado.nome == 'ACZG'
    }

    private Empresa criarEmpresa(String cnpj, String nome, String email) {
        return new Empresa(
                cnpj: new CnpjVO(cnpj),
                nome: nome,
                email: new EmailVO(email),
                senha: '12345678',
                endereco: new Endereco(
                    pais: 'Brasil',
                    estado: 'MG',
                    cidade: 'Belo Horizonte',
                    cep: '00000-000',
                ),
                descricao: 'Descricao',
        )
    }
}
