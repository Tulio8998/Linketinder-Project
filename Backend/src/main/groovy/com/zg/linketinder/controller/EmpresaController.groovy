package com.zg.linketinder.controller

import com.zg.linketinder.dto.EmpresaDTO
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.service.EmpresaService
import com.zg.linketinder.vo.CnpjVO
import com.zg.linketinder.vo.EmailVO

class EmpresaController {
    EmpresaService empresaService = new EmpresaService()

    String cadastrarEmpresa(EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = new Empresa(
                    cnpj: new CnpjVO(empresaDTO.cnpj),
                    nome: empresaDTO.nome,
                    email: new EmailVO(empresaDTO.email),
                    senha: empresaDTO.senha,
                    descricao: empresaDTO.descricao,
                    endereco: new Endereco(
                            pais: empresaDTO.pais,
                            estado: empresaDTO.estado,
                            cidade: empresaDTO.cidade,
                            cep: empresaDTO.cep
                    )
            )
            empresaService.salvar(empresa)
            return "Empresa cadastrada com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro no cadastro: ${e.message}"
        }
    }

    String atualizarEmpresa(Integer id, EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = new Empresa(
                    cnpj: new CnpjVO(empresaDTO.cnpj),
                    nome: empresaDTO.nome,
                    email: new EmailVO(empresaDTO.email),
                    senha: empresaDTO.senha,
                    descricao: empresaDTO.descricao,
                    endereco: new Endereco(
                            pais: empresaDTO.pais,
                            estado: empresaDTO.estado,
                            cidade: empresaDTO.cidade,
                            cep: empresaDTO.cep
                    )
            )
            empresaService.atualizar(id, empresa)
            return "Empresa atualizada com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na atualização: ${e.message}"
        }
    }

    String excluirEmpresa(Integer id) {
        try {
            empresaService.excluir(id)
            return "Empresa excluída com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na exclusão: ${e.message}"
        }
    }

    List<Empresa> listarEmpresas() {
        return empresaService.listarTodos()
    }

    Empresa buscarPorId(Integer id) {
        return empresaService.buscarPorId(id)
    }

    Empresa buscarPorCnpj(String cnpj) {
        return empresaService.buscarCnpjEmpresa(cnpj)
    }

    Empresa buscarPorEmail(String email) {
        return empresaService.buscarEmailEmpresa(email)
    }
}