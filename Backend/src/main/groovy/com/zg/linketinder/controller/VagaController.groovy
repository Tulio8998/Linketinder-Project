package com.zg.linketinder.controller

import com.zg.linketinder.dto.VagaDTO
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.VagaService

class VagaController {
    VagaService vagaService = new VagaService()

    String cadastrarVaga(VagaDTO vagaDTO) {
        try {
            Vaga vaga = new Vaga(
                    nome: vagaDTO.nome,
                    descricao: vagaDTO.descricao,
                    empresa: vagaDTO.empresa,
                    endereco: new Endereco(
                            pais: vagaDTO.pais,
                            estado: vagaDTO.estado,
                            cidade: vagaDTO.cidade
                    )
            )
            vagaService.salvar(vaga)
            return "Vaga cadastrada com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro no cadastro de vaga: ${e.message}"
        }
    }

    String atualizarVaga(Integer id, VagaDTO vagaDTO) {
        try {
            Vaga vaga = new Vaga(
                    id: id,
                    nome: vagaDTO.nome,
                    descricao: vagaDTO.descricao,
                    empresa: vagaDTO.empresa,
                    endereco: new Endereco(
                            pais: vagaDTO.pais,
                            estado: vagaDTO.estado,
                            cidade: vagaDTO.cidade
                    )
            )
            vagaService.atualizar(id, vaga)
            return "Vaga atualizada com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na atualização da vaga: ${e.message}"
        }
    }

    String excluirVaga(Integer idEmpresa, Integer idVaga) {
        try {
            vagaService.excluirVagaDaEmpresa(idEmpresa, idVaga)
            return "Vaga excluída com sucesso!"
        } catch (IllegalArgumentException e) {
            return "Erro na exclusão da vaga: ${e.message}"
        }
    }

    List<Vaga> listarTodas() {
        return vagaService.listarTodos()
    }

    List<Vaga> listarVagasEmpresa(Integer idEmpresa) {
        return vagaService.listarVagaEmpresa(idEmpresa)
    }

    Vaga buscarPorId(Integer id) {
        return vagaService.buscarPorId(id)
    }

    Vaga buscarIdVagaEmpresa(Integer idEmpresa, Integer idVaga) {
        return vagaService.buscarIdVagaEmpresa(idEmpresa, idVaga)
    }

    Vaga buscarNomeVagaEmpresa(Integer idEmpresa, String nome) {
        return vagaService.buscarNomeVagaEmpresa(idEmpresa, nome)
    }

    Vaga buscarNomeVaga(String nome) {
        return vagaService.buscarNomeVaga(nome)
    }
}