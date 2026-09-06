package com.zg.linketinder.service

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.repository.EmpresaRepository

class EmpresaService {
    EmpresaRepository empresaRepository = new EmpresaRepository()

    List<Empresa> listarTodos(){
        return empresaRepository.listarTodos();
    }

    void adicionarEmpresa(Empresa empresa) {
        validarEmpresa(empresa)
        empresaRepository.adicionarEmpresa(empresa)
    }

    void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("A empresa nao pode ser nulo")
        }
        validarCnpj(empresa)
        validarEmail(empresa)
    }

    void validarCnpj(Empresa empresa) {
        def cnpjExist = empresaRepository.listarTodos().find{
            it.cnpj == empresa.cnpj
        }
        if (cnpjExist) {
            throw new IllegalArgumentException("Cnpj ja cadastrado")
        }
    }

    void validarEmail(Empresa empresa) {
        def emailExist = empresaRepository.listarTodos().find {
            it.email == empresa.email
        }
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }
}
