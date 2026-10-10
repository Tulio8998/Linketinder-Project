package com.zg.linketinder.controller

import com.zg.linketinder.dto.CandidatoDTO
import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.service.CandidatoService
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO

class CandidatoController {
    CandidatoService candidatoService = new CandidatoService()

    String cadastrarCandidato(CandidatoDTO candidatoDTO) {
        try {
            Candidato candidato = new Candidato(
                    cpf: new CpfVO(candidatoDTO.cpf),
                    nome: candidatoDTO.nome,
                    email: new EmailVO(candidatoDTO.email),
                    senha: candidatoDTO.senha,
                    idade: candidatoDTO.idade,
                    dataNascimento: candidatoDTO.dataNascimento,
                    descricao: candidatoDTO.descricao,
                    endereco: new Endereco(
                            pais: candidatoDTO.pais,
                            estado: candidatoDTO.estado,
                            cidade: candidatoDTO.cidade,
                            cep: candidatoDTO.cep
                    )
            )
            candidatoService.salvar(candidato)
            return "Candidato cadastrado com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro no cadastro: ${e.message}"
        }
    }

    String atualizarCandidato(Integer id, CandidatoDTO candidatoDTO) {
        try {
            Candidato candidato = new Candidato(
                    cpf: new CpfVO(candidatoDTO.cpf),
                    nome: candidatoDTO.nome,
                    email: new EmailVO(candidatoDTO.email),
                    senha: candidatoDTO.senha,
                    idade: candidatoDTO.idade,
                    dataNascimento: candidatoDTO.dataNascimento,
                    descricao: candidatoDTO.descricao,
                    endereco: new Endereco(
                            pais: candidatoDTO.pais,
                            estado: candidatoDTO.estado,
                            cidade: candidatoDTO.cidade,
                            cep: candidatoDTO.cep
                    )
            )
            candidatoService.atualizar(id, candidato)
            return "Candidato atualizado com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na atualização: ${e.message}"
        }
    }

    String excluirCandidato(Integer id) {
        try {
            candidatoService.excluir(id)
            return "Candidato excluído com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na exclusão: ${e.message}"
        }
    }

    List<Candidato> listarCandidatos() {
        return candidatoService.listarTodos()
    }

    Candidato buscarPorId(Integer id) {
        return candidatoService.buscarPorId(id)
    }

    Candidato buscarPorEmail(String email) {
        return candidatoService.buscarEmailCandidato(email)
    }

    Candidato buscarPorCpf(String cpf) {
        return candidatoService.buscarCpfCandidato(cpf)
    }

    List<Candidato> listarCandidatoEmpresa(Integer idEmpresa) {
        return candidatoService.listarCandidatoEmpresa(idEmpresa)
    }
}