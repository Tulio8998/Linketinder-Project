package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.model.Empresa

interface EmpresaDAO extends CrudDAO<Empresa> {
    Empresa buscarCnpjEmpresa(String cnpj)
    Empresa buscarEmailEmpresa(String email)
}