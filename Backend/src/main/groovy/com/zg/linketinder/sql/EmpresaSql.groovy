package com.zg.linketinder.sql

class EmpresaSql {
    static final String INSERIR = '''
        INSERT INTO empresas (cnpj, nome, email, senha, pais, cidade, estado, cep, descricao)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    '''

    static final String ATUALIZAR = '''
        UPDATE empresas
        SET cnpj = ?, nome = ?, email = ?, senha = ?, pais = ?, estado = ?, cidade = ?, cep = ?, descricao = ?
        WHERE id = ?
    '''

    static final String EXCLUIR = 'DELETE FROM empresas WHERE id = ?'

    static final String BUSCAR_POR_ID = 'SELECT * FROM empresas WHERE id = ?'

    static final String BUSCAR_POR_CNPJ = 'SELECT * FROM empresas WHERE cnpj = ?'

    static final String BUSCAR_POR_EMAIL = 'SELECT * FROM empresas WHERE email = ?'

    static final String LISTAR_TODOS = 'SELECT * FROM empresas'
}