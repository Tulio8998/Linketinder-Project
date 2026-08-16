package com.zg.linketinder.repository

import com.zg.linketinder.model.Empresa

class EmpresaRepository {
    List<Empresa> empresas = []

    def adicionarEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }

    def listarTodos() {
        return empresas
    }
}
