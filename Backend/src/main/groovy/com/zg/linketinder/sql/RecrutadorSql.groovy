package com.zg.linketinder.sql

class RecrutadorSql {
    static final String INSERIR = '''
        INSERT INTO recrutadores (
            id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha, ativo
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, TRUE)
        RETURNING id
    '''

    static final String BUSCAR_POR_ID = '''
        SELECT r.id, r.id_empresa, r.nome, r.area, r.descricao, r.email, r.cpf,
               r.data_nascimento, r.senha, r.ativo
        FROM recrutadores r
        WHERE r.id = ?
    '''

    static final String BUSCAR_POR_EMAIL = '''
        SELECT r.id, r.id_empresa, r.nome, r.area, r.descricao, r.email, r.cpf,
               r.data_nascimento, r.senha, r.ativo
        FROM recrutadores r
        WHERE LOWER(r.email) = LOWER(?)
    '''

    static final String LISTAR_POR_EMPRESA = '''
        SELECT r.id, r.id_empresa, r.nome, r.area, r.descricao, r.email, r.ativo
        FROM recrutadores r
        WHERE r.id_empresa = ?
        ORDER BY r.nome, r.id
    '''

    static final String EXISTE_CPF = '''
        SELECT EXISTS (
            SELECT 1 FROM recrutadores WHERE cpf = ?
        ) AS existe
    '''

    static final String DESATIVAR = '''
        UPDATE recrutadores
        SET ativo = FALSE
        WHERE id = ? AND id_empresa = ? AND ativo = TRUE
    '''

    static final String LISTAR_VAGAS_DA_AREA = '''
        SELECT v.id, v.nome, v.descricao, v.area,
               cm.nome AS nome_competencia
        FROM recrutadores r
        INNER JOIN vagas v
            ON v.id_empresa = r.id_empresa AND v.area = r.area
        LEFT JOIN vagas_competencias vc ON vc.id_vaga = v.id
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
        WHERE r.id = ? AND r.ativo = TRUE
        ORDER BY v.id, cm.nome
    '''

    static final String LISTAR_CANDIDATOS_ANONIMOS = '''
        SELECT c.id, c.descricao, cm.nome AS nome_competencia
        FROM recrutadores r
        INNER JOIN vagas v
            ON v.id_empresa = r.id_empresa AND v.area = r.area
        CROSS JOIN candidatos c
        LEFT JOIN candidatos_competencias cc ON cc.id_candidato = c.id
        LEFT JOIN competencias cm ON cm.id = cc.id_competencia
        WHERE r.id = ? AND r.ativo = TRUE AND v.id = ?
          AND NOT EXISTS (
              SELECT 1
              FROM curtidas_empresa ce
              WHERE ce.id_empresa = r.id_empresa
                AND ce.id_candidato = c.id
                AND ce.id_vaga = v.id
          )
        ORDER BY c.id, cm.nome
    '''

    static final String REGISTRAR_AVALIACAO = '''
        INSERT INTO curtidas_empresa (
            id_empresa, id_candidato, id_vaga, curtiu, id_recrutador
        )
        SELECT r.id_empresa, ?, v.id, ?, r.id
        FROM recrutadores r
        INNER JOIN vagas v
            ON v.id_empresa = r.id_empresa AND v.area = r.area
        WHERE r.id = ? AND r.ativo = TRUE AND v.id = ?
        ON CONFLICT (id_empresa, id_candidato, id_vaga) DO NOTHING
        RETURNING id
    '''

    static final String LISTAR_HISTORICO = '''
        SELECT re.nome AS nome_autor, v.nome AS nome_vaga, v.area,
               c.descricao AS descricao_candidato, cm.nome AS nome_competencia,
               ce.curtiu, ce.id
        FROM recrutadores solicitante
        INNER JOIN vagas v
            ON v.id_empresa = solicitante.id_empresa AND v.area = solicitante.area
        INNER JOIN curtidas_empresa ce
            ON ce.id_empresa = solicitante.id_empresa AND ce.id_vaga = v.id
        LEFT JOIN recrutadores re ON re.id = ce.id_recrutador
        INNER JOIN candidatos c ON c.id = ce.id_candidato
        LEFT JOIN candidatos_competencias cc ON cc.id_candidato = c.id
        LEFT JOIN competencias cm ON cm.id = cc.id_competencia
        WHERE solicitante.id = ? AND solicitante.ativo = TRUE
        ORDER BY ce.id, cm.nome
    '''

    static final String LISTAR_MATCHES_CANDIDATO = '''
        SELECT re.nome AS nome_recrutador, re.email AS email_recrutador,
               e.nome AS nome_empresa, e.email AS email_empresa, e.cnpj AS cnpj_empresa,
               e.descricao AS descricao_empresa, e.pais AS pais_empresa,
               e.estado AS estado_empresa, e.cidade AS cidade_empresa, e.cep AS cep_empresa,
               v.nome AS nome_vaga, ce.id
        FROM curtidas_candidato cc
        INNER JOIN curtidas_empresa ce
            ON ce.id_candidato = cc.id_candidato
           AND ce.id_vaga = cc.id_vaga
           AND ce.curtiu = TRUE
        INNER JOIN vagas v ON v.id = cc.id_vaga AND v.id_empresa = ce.id_empresa
        INNER JOIN empresas e ON e.id = v.id_empresa
        LEFT JOIN recrutadores re ON re.id = ce.id_recrutador
        WHERE cc.id_candidato = ? AND cc.curtiu = TRUE
        ORDER BY ce.id
    '''
}
