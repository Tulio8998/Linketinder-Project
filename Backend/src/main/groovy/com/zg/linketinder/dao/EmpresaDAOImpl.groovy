package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAOImpl
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.sql.EmpresaSql
import com.zg.linketinder.factory.ConexaoDB

class EmpresaDAOImpl extends CrudDAOImpl<Empresa> implements EmpresaDAO {

    @Override
    protected String getSqlInserir() { return EmpresaSql.INSERIR }

    @Override
    protected String getSqlAtualizar() { return EmpresaSql.ATUALIZAR }

    @Override
    protected String getSqlExcluir() { return EmpresaSql.EXCLUIR }

    @Override
    protected String getSqlBuscarPorId() { return EmpresaSql.BUSCAR_POR_ID
    }
    @Override
    protected String getSqlListarTodos() { return EmpresaSql.LISTAR_TODOS }

    @Override
    protected List getParametrosInserir(Empresa empresa) {
        return [
                empresa.cnpj, empresa.nome, empresa.email, empresa.senha,
                empresa.pais, empresa.cidade, empresa.estado, empresa.cep, empresa.descricao
        ]
    }

    @Override
    protected List getParametrosAtualizar(Integer id, Empresa empresa) {
        return [
                empresa.cnpj, empresa.nome, empresa.email, empresa.senha,
                empresa.pais, empresa.estado, empresa.cidade, empresa.cep, empresa.descricao, id
        ]
    }

    @Override
    protected Empresa mapearResultado(def row) {
        return new Empresa(
                id: row.id,
                cnpj: row.cnpj,
                nome: row.nome,
                email: row.email,
                senha: row.senha,
                pais: row.pais,
                estado: row.estado,
                cidade: row.cidade,
                cep: row.cep,
                descricao: row.descricao
        )
    }

    @Override
    Empresa buscarCnpjEmpresa(String cnpj) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(EmpresaSql.BUSCAR_POR_CNPJ, [cnpj])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }

    @Override
    Empresa buscarEmailEmpresa(String email) {
        return ConexaoDB.consultar { sql ->
            def linhas = sql.rows(EmpresaSql.BUSCAR_POR_EMAIL, [email])
            return linhas.isEmpty() ? null : mapearResultado(linhas.first())
        }
    }
}