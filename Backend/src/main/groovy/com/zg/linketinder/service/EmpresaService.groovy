package com.zg.linketinder.service

import com.zg.linketinder.dao.EmpresaDAO
import com.zg.linketinder.dao.EmpresaDAOImpl
import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.service.base.CrudService

class EmpresaService extends CrudService<Empresa> {

    private EmpresaDAO empresaDAO = new EmpresaDAOImpl()

    EmpresaService() {
        this(new EmpresaDAOImpl())
    }

    EmpresaService(EmpresaDAO empresaDAO) {
        this.empresaDAO = empresaDAO
    }

    @Override
    protected CrudDAO<Empresa> getDao() { return empresaDAO }

    @Override
    protected void validarAntesSalvar(Empresa empresa) {
        if (empresaDAO.buscarCnpjEmpresa(empresa.cnpj.valor)) throw new IllegalArgumentException("CNPJ ja cadastrado")
        if (empresaDAO.buscarEmailEmpresa(empresa.email.valor)) throw new IllegalArgumentException("Email ja cadastrado")
    }

    Empresa buscarCnpjEmpresa(String cnpj) {
        try { return empresaDAO.buscarCnpjEmpresa(cnpj) }
        catch (Exception e) { println "Nao tem empresa com esse cnpj: ${e.message}"; return null }
    }

    Empresa buscarEmailEmpresa(String email) {
        try { return empresaDAO.buscarEmailEmpresa(email) }
        catch (Exception e) { println "Nao tem empresas com esse email: ${e.message}"; return null }
    }
}