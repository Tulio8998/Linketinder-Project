package com.zg.linketinder.service

import com.zg.linketinder.dao.base.CrudDAO
import com.zg.linketinder.dao.VagaDAO
import com.zg.linketinder.dao.VagaDAOImpl
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.service.base.CrudService

class VagaService extends CrudService<Vaga> {

    private VagaDAO vagaDAO = new VagaDAOImpl()

    @Override
    protected CrudDAO<Vaga> getDao() { return vagaDAO }

    @Override
    protected void validarAntesSalvar(Vaga vaga) {
        if (vagaDAO.buscarNomeVagaEmpresa(vaga.empresa.id, vaga.nome) != null) {
            throw new IllegalArgumentException("A empresa ja possui uma vaga cadastrada com esse nome: ${vaga.nome}")
        }
    }

    void excluirVagaDaEmpresa(Integer id_empresa, Integer id_vaga) {
        try {
            if (vagaDAO.buscarIdVagaEmpresa(id_empresa, id_vaga)) vagaDAO.excluirVaga(id_empresa, id_vaga)
            else throw new IllegalArgumentException("Vaga nao pertence a esta empresa ou nao existe")
        } catch (Exception e) { throw new IllegalArgumentException("Erro ao excluir: ${e.message}") }
    }

    List<Vaga> listarVagaEmpresa(Integer id_empresa) {
        try { return vagaDAO.listarVagaEmpresa(id_empresa) }
        catch (Exception e) { println "Erro ao listar: ${e.message}"; return [] }
    }

    Vaga buscarIdVagaEmpresa(Integer id_empresa, Integer id_vaga) {
        try { return vagaDAO.buscarIdVagaEmpresa(id_empresa, id_vaga) }
        catch (Exception e) { println "Erro ao buscar: ${e.message}"; return null }
    }

    Vaga buscarNomeVagaEmpresa(Integer id_empresa, String nome) {
        try { return vagaDAO.buscarNomeVagaEmpresa(id_empresa, nome) }
        catch (Exception e) { println "Erro ao buscar: ${e.message}"; return null }
    }

    Vaga buscarNomeVaga(String nome) {
        try { return vagaDAO.buscarNomeVaga(nome) }
        catch (Exception e) { println "Erro ao buscar: ${e.message}"; return null }
    }
}