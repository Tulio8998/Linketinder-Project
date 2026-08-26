package com.zg.linketinder.repository

import com.zg.linketinder.model.Empresa

class EmpresaRepository {
    List<Empresa> empresas = []

    EmpresaRepository(){
        empresas.add( new Empresa(
                cnpj: "00.000.000/0000-00",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "00000-000",
                descricao: "Zero Glosa",
        ))
        empresas.add(new Empresa(
                cnpj: "11.111.111/1111-11",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "11111-111",
                descricao: "Zero Glosa",
        ))
        empresas.add(new Empresa(
                cnpj: "22.222.222/2222-22",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "22222-222",
                descricao: "Zero Glosa",
        ))
        empresas.add(new Empresa(
                cnpj: "33.333.333/3333-33",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "33333-333",
                descricao: "Zero Glosa",
        ))
        empresas.add(new Empresa(
                cnpj: "44.444.444/4444-44",
                pais: "Brasil",
                nome: "ZG",
                email: "teste@teste.com",
                estado: "MG",
                cep: "44444-444",
                descricao: "Zero Glosa",
        ))
    }

    def adicionarEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }

    def listarTodos() {
        return empresas
    }
}
