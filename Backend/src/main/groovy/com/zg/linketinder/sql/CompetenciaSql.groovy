package com.zg.linketinder.sql

class CompetenciaSql {
    static final String INSERIR = 'INSERT INTO competencias (nome) VALUES (?)'
    static final String ATUALIZAR = 'UPDATE competencias SET nome = ? WHERE id = ?'
    static final String EXCLUIR = 'DELETE FROM competencias WHERE id = ?'
    static final String LISTAR_TODAS = 'SELECT * FROM competencias'
    static final String BUSCAR_POR_ID = 'SELECT * FROM competencias WHERE id = ?'

    static final String BUSCAR_POR_NOME = 'SELECT * FROM competencias WHERE nome = ?'

    static final String INSERIR_CANDIDATO = '''
        INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (?, ?)
    '''
    static final String EXCLUIR_CANDIDATO = '''
        DELETE FROM candidatos_competencias WHERE id_candidato = ? AND id_competencia = ?
    '''
    static final String LISTAR_POR_CANDIDATO = '''
        SELECT c.* FROM competencias c
        INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
        WHERE cc.id_candidato = ?
    '''
    static final String BUSCAR_POR_ID_E_CANDIDATO = '''
        SELECT c.* FROM competencias c
        INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
        WHERE cc.id_candidato = ? AND c.id = ?
    '''
    static final String BUSCAR_POR_NOME_E_CANDIDATO = '''
        SELECT c.* FROM competencias c
        INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
        WHERE cc.id_candidato = ? AND c.nome = ?
    '''

    static final String INSERIR_VAGA = '''
        INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (?, ?)
    '''
    static final String EXCLUIR_VAGA = '''
        DELETE FROM vagas_competencias WHERE id_vaga = ? AND id_competencia = ?
    '''
    static final String LISTAR_POR_VAGA = '''
        SELECT c.* FROM competencias c
        INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
        WHERE vc.id_vaga = ?
    '''
    static final String BUSCAR_POR_ID_E_VAGA = '''
        SELECT c.* FROM competencias c
        INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
        WHERE vc.id_vaga = ? AND c.id = ?
    '''
    static final String BUSCAR_POR_NOME_E_VAGA = '''
        SELECT c.* FROM competencias c
        INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
        WHERE vc.id_vaga = ? AND c.nome = ?
    '''
}