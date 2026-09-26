package com.zg.linketinder.service

import com.zg.linketinder.dao.EmpresaDAO
import com.zg.linketinder.dao.EmpresaDAOImpl
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.repository.EmpresaRepository

class EmpresaService {
    private EmpresaDAO empresaDAO = new EmpresaDAOImpl()

    List<Empresa> listarEmpresas(){
        try {
            return empresaDAO.listarEmpresa()
        } catch (Exception e) {
            println "Nao tem empresas para listar: ${e.message}"
            return []
        }
    }

    Empresa buscarIdEmpresa(Integer id) {
        try {
            return empresaDAO.buscarIdEmpresa(id)
        } catch (Exception e) {
            println "Nao tem empresas com esse id: ${e.message}"
            return null
        }
    }

    Empresa buscarCnpjEmpresa(String cnpj) {
        try {
            return empresaDAO.buscarCnpjEmpresa(cnpj)
        } catch (Exception e) {
            println "Nao tem empresa com esse cnpj: ${e.message}"
            return null
        }
    }

    Empresa buscarEmailEmpresa(String email) {
        try {
            return empresaDAO.buscarEmailEmpresa(email)
        } catch (Exception e) {
            println "Nao tem empresas com esse email: ${e.message}"
            return null
        }
    }

    void salvarEmpresa(Empresa empresa) {
        try {
            if (validarEmpresa(empresa)) {
                empresaDAO.salvarEmpresa(empresa)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar empresa: ${e.message}")
        }
    }

    boolean validarEmpresa(Empresa empresa) {
        if (!empresa) {
            throw new IllegalArgumentException("A empresa nao pode ser nulo")
        }
        validarCnpj(empresa)
        validarEmail(empresa)
        return true
    }

    void validarCnpj(Empresa empresa) {
        def cnpjExist = empresaDAO.buscarCnpjEmpresa(empresa.cnpj)
        if (cnpjExist) {
            throw new IllegalArgumentException("Cnpj ja cadastrado")
        }
    }

    void validarEmail(Empresa empresa) {
        def emailExist = empresaDAO.buscarEmailEmpresa(empresa.email)
        if (emailExist) {
            throw new IllegalArgumentException("Email ja cadastrado")
        }
    }

    void atualizarEmpresa(Integer id, Empresa empresa) {
        try {
            if ( empresa != null && buscarIdEmpresa(id) != null) {
                empresa.id = id
                empresaDAO.atualizarEmpresa(empresa)
            } else {
                throw new IllegalArgumentException("Empresa nao encontrado ou dados nulos")
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar empresa: ${e.message}")
        }
    }

    void excluirEmpresa(Integer id) {
        try {
            if (empresaDAO.buscarIdEmpresa(id)) {
                empresaDAO.excluirEmpresa(id)
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("O empresa nao pode ser nulo e deve ter um id valido: ${e.message}")
        }
    }
}
