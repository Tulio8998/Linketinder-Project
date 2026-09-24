package com.zg.linketinder.dao

import com.zg.linketinder.model.Empresa

interface EmpresaDAO {
    void salvarEmpresa(Empresa empresa)
    void atualizarEmpresa(Empresa empresa)
    void excluirEmpresa(Integer id)
    List<Empresa> listarEmpresa()
    Empresa buscarIdEmpresa(Integer id)
    Empresa buscarCnpjEmpresa(String cnpj)
    Empresa buscarEmailEmpresa(String email)
}