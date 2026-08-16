package com.zg.linketinder.service

import com.zg.linketinder.model.Empresa
import com.zg.linketinder.repository.EmpresaRepository

class EmpresaService {
    EmpresaRepository empresaRepository = new EmpresaRepository()

    EmpresaService() {
        empresaRepository.adicionarEmpresa( new Empresa(
                cnpj: "00.000.000/0000-00",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Zero Glosa",
        ))
        empresaRepository.adicionarEmpresa(new Empresa(
                cnpj: "11.111.111/1111-11",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "11111-111",
                descricao: "Zero Glosa",
        ))
        empresaRepository.adicionarEmpresa(new Empresa(
                cnpj: "22.222.222/2222-22",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "22222-222",
                descricao: "Zero Glosa",
        ))
        empresaRepository.adicionarEmpresa(new Empresa(
                cnpj: "33.333.333/3333-33",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "33333-333",
                descricao: "Zero Glosa",
        ))
        empresaRepository.adicionarEmpresa(new Empresa(
                cnpj: "44.444.444/4444-44",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "44444-444",
                descricao: "Zero Glosa",
        ))
    }

    def listarTodos(){
        return empresaRepository.listarTodos();
    }

    def adicionarEmpresa(Empresa empresa) {
        empresaRepository.adicionarEmpresa(empresa)
    }
}
