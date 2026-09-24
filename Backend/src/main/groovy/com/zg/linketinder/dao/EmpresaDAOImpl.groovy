package com.zg.linketinder.dao

import com.zg.linketinder.model.Candidato
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class EmpresaDAOImpl implements EmpresaDAO{

    @Override
    void salvarEmpresa(Empresa empresa) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                INSERT INTO empresas (cnpj, nome, email, senha, pais, cidade, estado,
                cep, descricao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            '''
        try {
            sql.executeInsert(query, [empresa.cnpj, empresa.nome, empresa.email, empresa.senha,
                                      empresa.pais, empresa.cidade, empresa.estado, empresa.cep, empresa.descricao])
        } catch (Exception e) {
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void atualizarEmpresa(Empresa empresa) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                UPDATE empresas 
                SET cnpj = ?, nome = ?, email = ?, senha = ?, pais = ?, estado = ?, cidade = ?, cep = ?, descricao = ? 
                WHERE id = ?
            '''
        try {
            sql.executeUpdate(query, [empresa.cnpj, empresa.nome, empresa.email, empresa.senha,
                                      empresa.pais, empresa.cidade, empresa.estado, empresa.cep, empresa.descricao, empresa.id])
        } catch (Exception e) {
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirEmpresa(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false

        def query = '''
                DELETE FROM empresas WHERE id = ?
            '''
        try {
            sql.executeUpdate(query, [id])
        } catch (Exception e) {
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<Empresa> listarEmpresa() {
        Sql sql = ConexaoDB.getConexao()
        List<Empresa> lista = []
        def query = '''
                SELECT * FROM empresas
            '''

        try {
            sql.eachRow(query) { row ->
                lista.add(new Empresa(id: row.id, cnpj: row.cnpj, nome: row.nome,
                        email: row.email, senha: row.senha, pais: row.pais, estado: row.estado,
                        cidade: row.cidade, cep: row.cep, descricao: row.descricao))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Empresa buscarIdEmpresa(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM empresas WHERE id = ?
            '''

        try {
            def row = sql.firstRow(query, [id])
            if (row) {
                return new Empresa(id: row.id, cnpj: row.cnpj, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, descricao: row.descricao
                )
            }
        } finally {
            sql.close()
        }
    }

    @Override
    Empresa buscarCnpjEmpresa(String cnpj) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM empresas WHERE cnpj = ?
            '''

        try {
            def row = sql.firstRow(query, [cnpj])
            if (row) {
                return new Empresa(id: row.id, cnpj: row.cnpj, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, descricao: row.descricao
                )
            }
        } finally {
            sql.close()
        }
    }

    @Override
    Empresa buscarEmailEmpresa(String email) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
                SELECT * FROM empresas WHERE email = ?
            '''

        try {
            def row = sql.firstRow(query, [email])
            if (row) {
                return new Empresa(id: row.id, cnpj: row.cnpj, nome: row.nome, email: row.email,
                        senha: row.senha, pais: row.pais, estado: row.estado, cidade: row.cidade,
                        cep: row.cep, descricao: row.descricao
                )
            }
        } finally {
            sql.close()
        }
    }
}