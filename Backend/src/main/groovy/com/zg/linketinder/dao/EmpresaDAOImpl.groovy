package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAOImpl
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Endereco
import com.zg.linketinder.sql.EmpresaSql
import com.zg.linketinder.factory.ConexaoDB
import com.zg.linketinder.vo.CnpjVO
import com.zg.linketinder.vo.EmailVO

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
                empresa.cnpj.valor, empresa.nome, empresa.email.valor, empresa.senha,
                empresa.endereco?.pais, empresa.endereco?.cidade, empresa.endereco?.estado, empresa.endereco?.cep, empresa.descricao
        ]
    }

    @Override
    protected List getParametrosAtualizar(Integer id, Empresa empresa) {
        return [
                empresa.cnpj.valor, empresa.nome, empresa.email.valor, empresa.senha,
                empresa.endereco?.pais, empresa.endereco?.estado, empresa.endereco?.cidade, empresa.endereco?.cep, empresa.descricao, id
        ]
    }

    @Override
    protected Empresa mapearResultado(def row) {
        return new Empresa(
                id: row.id,
                cnpj: new CnpjVO(row.cnpj),
                nome: row.nome,
                email: new EmailVO(row.email),
                senha: row.senha,
                endereco: new Endereco(
                    pais: row.pais,
                    estado: row.estado,
                    cidade: row.cidade,
                    cep: row.cep,
                ),
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