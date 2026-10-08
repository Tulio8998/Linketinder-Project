package com.zg.linketinder.sql

class CurtidaSql {
    static final String INSERIR_CANDIDATO = '''
        INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu)
        VALUES (?, ?, ?)
        ON CONFLICT (id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
    '''

    static final String INSERIR_EMPRESA = '''
        INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (id_empresa, id_candidato, id_vaga) DO UPDATE SET curtiu = EXCLUDED.curtiu
    '''

    static final String LISTAR_CURTIDAS_CANDIDATO = '''
        SELECT cc.id, cc.curtiu,
               c.id AS id_candidato, c.nome AS nome_candidato,
               v.id AS id_vaga, v.nome AS nome_vaga
        FROM curtidas_candidato cc
        INNER JOIN candidatos c ON cc.id_candidato = c.id
        INNER JOIN vagas v ON cc.id_vaga = v.id
    '''

    static final String LISTAR_CURTIDAS_EMPRESA = '''
        SELECT ce.id, ce.curtiu,
               e.id AS id_empresa, e.nome AS nome_empresa,
               c.id AS id_candidato, c.nome AS nome_candidato,
               v.id AS id_vaga, v.nome AS nome_vaga
        FROM curtidas_empresa ce
        INNER JOIN empresas e ON ce.id_empresa = e.id
        INNER JOIN candidatos c ON ce.id_candidato = c.id
        INNER JOIN vagas v ON ce.id_vaga = v.id
    '''

    static final String LISTAR_MATCHES_EMPRESA = '''
        SELECT c.id AS id_candidato, c.nome AS nome_candidato, c.email AS email_candidato,
               e.id AS id_empresa, e.nome AS nome_empresa,
               v.id AS id_vaga, v.nome AS nome_vaga
        FROM curtidas_candidato cc
        INNER JOIN curtidas_empresa ce
            ON cc.id_candidato = ce.id_candidato AND cc.id_vaga = ce.id_vaga
        INNER JOIN candidatos c ON cc.id_candidato = c.id
        INNER JOIN empresas e ON ce.id_empresa = e.id
        INNER JOIN vagas v ON cc.id_vaga = v.id
        WHERE cc.curtiu = TRUE AND ce.curtiu = TRUE AND e.id = ?
    '''

    static final String LISTAR_MATCHES_CANDIDATO = '''
        SELECT c.id AS id_candidato, c.nome AS nome_candidato, c.email AS email_candidato,
               e.id AS id_empresa, e.nome AS nome_empresa,
               v.id AS id_vaga, v.nome AS nome_vaga
        FROM curtidas_candidato cc
        INNER JOIN curtidas_empresa ce
            ON cc.id_candidato = ce.id_candidato AND cc.id_vaga = ce.id_vaga
        INNER JOIN candidatos c ON cc.id_candidato = c.id
        INNER JOIN empresas e ON ce.id_empresa = e.id
        INNER JOIN vagas v ON cc.id_vaga = v.id
        WHERE cc.curtiu = TRUE AND ce.curtiu = TRUE AND c.id = ?
    '''
}