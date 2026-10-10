package com.zg.linketinder.sql

class CandidatoSql {
    static final String INSERIR = '''
        INSERT INTO candidatos (cpf, nome, email, senha, pais, estado, cidade, cep, data_nascimento, descricao)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    '''

    static final String ATUALIZAR = '''
        UPDATE candidatos
        SET cpf = ?, nome = ?, email = ?, senha = ?, pais = ?, estado = ?, cidade = ?, cep = ?, data_nascimento = ?, descricao = ?
        WHERE id = ?
    '''

    static final String EXCLUIR = 'DELETE FROM candidatos WHERE id = ?'

    static final String BUSCAR_POR_ID = '''
        SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM candidatos ct
        LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
        LEFT JOIN competencias cm ON cm.id = c.id_competencia
        WHERE ct.id = ?
    '''

    static final String BUSCAR_POR_EMAIL = '''
        SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM candidatos ct
        LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
        LEFT JOIN competencias cm ON cm.id = c.id_competencia
        WHERE ct.email = ?
    '''

    static final String BUSCAR_POR_CPF = '''
        SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM candidatos ct
        LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
        LEFT JOIN competencias cm ON cm.id = c.id_competencia
        WHERE ct.cpf = ?
    '''

    static final String LISTAR_TODOS = '''
        SELECT ct.id, ct.cpf, ct.nome, ct.email, ct.senha, ct.pais, ct.estado, ct.cidade, ct.cep,
               ct.data_nascimento, ct.descricao, cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM candidatos ct
        LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
        LEFT JOIN competencias cm ON cm.id = c.id_competencia
    '''

    static final String LISTAR_POR_EMPRESA = '''
        SELECT ct.id, '000.000.000-00' AS cpf, 'anonimo' || ct.id AS nome, 'anonimo@email.com' AS email,
               ct.senha, ct.pais, ct.estado, ct.cidade, ct.cep, ct.data_nascimento, ct.descricao,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM candidatos ct
        INNER JOIN curtidas_candidato cc ON cc.id_candidato = ct.id
        INNER JOIN vagas v ON v.id = cc.id_vaga
        LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
        LEFT JOIN competencias cm ON cm.id = c.id_competencia
        WHERE v.id_empresa = ? AND cc.curtiu = TRUE
    '''

    static final String INSERIR_COMPETENCIA = '''
        INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (?, ?)
    '''

    static final String EXCLUIR_COMPETENCIAS = '''
        DELETE FROM candidatos_competencias WHERE id_candidato = ?
    '''
}