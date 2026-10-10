package com.zg.linketinder.service

import com.zg.linketinder.dao.EmpresaDAO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.vo.CnpjVO
import com.zg.linketinder.vo.EmailVO
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {

    EmpresaDAO empresaDAO = Mock()
    EmpresaService empresaService = new EmpresaService(empresaDAO)

    def "deve validar antes de salvar"() {
        when: "tento instanciar e salvar a empresa com dados inválidos"
        Empresa empresa = new Empresa(
                cnpj: new CnpjVO(''),
                nome: '',
                email: new EmailVO(''),
                senha: ''
        )
        empresaService.salvar(empresa)

        then: "a validação impede o cadastro"
        thrown(IllegalArgumentException)
        0 * empresaDAO.salvar(_)
    }

    def "deve buscar cnpj da empresa"() {
        given: "uma empresa cadastrada"
        String cnpj = '12.345.678/0001-90'
        Empresa empresa = new Empresa(
                cnpj: new CnpjVO(cnpj),
                nome: 'Empresa Teste',
                email: new EmailVO('empresa@email.com')
        )

        when: "busco a empresa pelo CNPJ"
        Empresa resultado = empresaService.buscarCnpjEmpresa(cnpj)

        then: "a empresa é retornada"
        1 * empresaDAO.buscarCnpjEmpresa(cnpj) >> empresa
        resultado == empresa
    }

    def "deve buscar email da empresa"() {
        given: "uma empresa cadastrada"
        String email = 'empresa@email.com'
        Empresa empresa = new Empresa(
                cnpj: new CnpjVO('12.345.678/0001-90'),
                nome: 'Empresa Teste',
                email: new EmailVO(email)
        )

        when: "busco a empresa pelo e-mail"
        Empresa resultado = empresaService.buscarEmailEmpresa(email)

        then: "a empresa é retornada"
        1 * empresaDAO.buscarEmailEmpresa(email) >> empresa
        resultado == empresa
    }
}