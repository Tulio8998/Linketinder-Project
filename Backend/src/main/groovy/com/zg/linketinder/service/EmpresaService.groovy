package com.zg.linketinder.service

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.repository.EmpresaRepository

class EmpresaService {
    EmpresaRepository empresaRepository = new EmpresaRepository()

    def listarTodos(){
        return empresaRepository.listarTodos();
    }

    def adicionarEmpresa(Empresa empresa) {
        validarEmpresa(empresa)
        empresaRepository.adicionarEmpresa(empresa)
    }

    def validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("A empresa nao pode ser nulo")
        }
        validarCnpj(empresa)
        validarEmail(empresa)
    }

    def validarCnpj(Empresa empresa) {
        def cnpjExist = empresaRepository.listarTodos().find{
            it.cnpj == empresa.cnpj
        }
        if (cnpjExist) {
            throw new IllegalArgumentException("Cnpj ja cadastrado")
        }
    }

    def validarEmail(Empresa empresa) {
        def emailExist = empresaRepository.listarTodos().find {
            it.email == empresa.email
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }
}
