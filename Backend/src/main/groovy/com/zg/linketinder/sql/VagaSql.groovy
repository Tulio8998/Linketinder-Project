package com.zg.linketinder.sql

class VagaSql {
    static final String INSERIR = '''
        INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa)
        VALUES (?, ?, ?, ?, ?, ?)
    '''

    static final String ATUALIZAR = '''
        UPDATE vagas SET nome = ?, descricao = ?, pais = ?, estado = ?, cidade = ?
        WHERE id = ?
    '''

    static final String EXCLUIR = 'DELETE FROM vagas WHERE id = ? AND id_empresa = ?'

    static final String LISTAR_TODAS = '''
        SELECT v.id, v.nome AS nome_vaga, v.descricao, v.pais, v.estado, v.cidade,
               e.id AS id_empresa, 'anonimo' || e.id AS nome_empresa,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM vagas v
        INNER JOIN empresas e ON v.id_empresa = e.id
        LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
    '''

    static final String LISTAR_POR_EMPRESA = '''
        SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,
               e.id AS id_empresa, e.nome AS nome_empresa,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM vagas v
        INNER JOIN empresas e ON v.id_empresa = e.id
        LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
        WHERE v.id_empresa = ?
    '''

    static final String BUSCAR_POR_ID_E_EMPRESA = '''
        SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,
               v.id_empresa AS id_empresa, e.nome AS nome_empresa,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM vagas v
        INNER JOIN empresas e ON v.id_empresa = e.id
        LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
        WHERE v.id_empresa = ? AND v.id = ?
    '''

    static final String BUSCAR_POR_NOME_E_EMPRESA = '''
        SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,
               v.id_empresa AS id_empresa, e.nome AS nome_empresa,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM vagas v
        INNER JOIN empresas e ON v.id_empresa = e.id
        LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
        WHERE v.id_empresa = ? AND v.nome = ?
    '''

    static final String BUSCAR_POR_ID = '''
        SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,
               v.id_empresa AS id_empresa, 'anonimo' || e.id AS nome_empresa,
               cm.id AS id_competencia, cm.nome AS nome_competencia
        FROM vagas v
        LEFT JOIN empresas e ON v.id_empresa = e.id
        LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
        LEFT JOIN competencias cm ON cm.id = vc.id_competencia
        WHERE v.id = ?
    '''

    static final String INSERIR_COMPETENCIA = '''
        INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (?, ?)
    '''
}