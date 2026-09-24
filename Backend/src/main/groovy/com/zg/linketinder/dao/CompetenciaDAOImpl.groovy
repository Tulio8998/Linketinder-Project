package com.zg.linketinder.dao

import com.zg.linketinder.model.Competencia
import com.zg.linketinder.util.ConexaoDB
import groovy.sql.Sql

class CompetenciaDAOImpl implements CompetenciaDAO {
    @Override
    List<Competencia> listarCompetencia() {
        Sql sql = ConexaoDB.getConexao()
        List<Competencia> lista = []
        def query = 'SELECT * FROM competencias'
        try {
            sql.eachRow(query) { row ->
                lista.add(new Competencia(id: row.id, nome: row.nome))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarIdCompetencia(Integer id) {
        Sql sql = ConexaoDB.getConexao()
        def query = 'SELECT * FROM competencias WHERE id = ?'
        try {
            def row = sql.firstRow(query, [id])
            return row ? new Competencia(id: row.id, nome: row.nome) : null
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarNomeCompetencia(String nome) {
        Sql sql = ConexaoDB.getConexao()
        def query = 'SELECT * FROM competencias WHERE nome = ?'
        try {
            def row = sql.firstRow(query, [nome])
            return row ? new Competencia(id: row.id, nome: row.nome) : null
        } finally {
            sql.close()
        }
    }

    @Override
    void salvaCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false
        def query = 'INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (?, ?)'
        try {
            sql.executeInsert(query, [id_candidato, id_competencia])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false
        def query = 'DELETE FROM candidatos_competencias WHERE id_candidato = ? AND id_competencia = ?'
        try {
            sql.executeUpdate(query, [id_candidato, id_competencia])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<Competencia> listarCompetenciasCandidato(Integer id_candidato) {
        Sql sql = ConexaoDB.getConexao()
        List<Competencia> lista = []
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
            WHERE cc.id_candidato = ?
        '''
        try {
            sql.eachRow(query, [id_candidato]) { row ->
                lista.add(new Competencia(id: row.id, nome: row.nome))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarIdCompetenciaCandidato(Integer id_candidato, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
            WHERE cc.id_candidato = ? AND c.id = ?
        '''
        try {
            def row = sql.firstRow(query, [id_candidato, id_competencia])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarNomeCompetenciaCandidato(Integer id_candidato, String nome) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN candidatos_competencias cc ON c.id = cc.id_competencia
            WHERE cc.id_candidato = ? AND c.nome = ?
        '''
        try {
            def row = sql.firstRow(query, [id_candidato, nome])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    void salvaCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false
        def query = 'INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (?, ?)'
        try {
            sql.executeInsert(query, [id_vaga, id_competencia])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    void excluirCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        sql.connection.autoCommit = false
        def query = 'DELETE FROM vagas_competencias WHERE id_vaga = ? AND id_competencia = ?'
        try {
            sql.executeUpdate(query, [id_vaga, id_competencia])
            sql.commit()
        } catch (Exception e) {
            sql.rollback()
            throw e
        } finally {
            sql.close()
        }
    }

    @Override
    List<Competencia> listarCompetenciasVaga(Integer id_vaga) {
        Sql sql = ConexaoDB.getConexao()
        List<Competencia> lista = []
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
            WHERE vc.id_vaga = ?
        '''
        try {
            sql.eachRow(query, [id_vaga]) { row ->
                lista.add(new Competencia(id: row.id, nome: row.nome))
            }
            return lista
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarIdCompetenciaVaga(Integer id_vaga, Integer id_competencia) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
            WHERE vc.id_vaga = ? AND c.id = ?
        '''
        try {
            def row = sql.firstRow(query, [id_vaga, id_competencia])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }

    @Override
    Competencia buscarNomeCompetenciaVaga(Integer id_vaga, String nome) {
        Sql sql = ConexaoDB.getConexao()
        def query = '''
            SELECT c.* FROM competencias c
            INNER JOIN vagas_competencias vc ON c.id = vc.id_competencia
            WHERE vc.id_vaga = ? AND c.nome = ?
        '''
        try {
            def row = sql.firstRow(query, [id_vaga, nome])
            if (row) {
                return new Competencia(id: row.id, nome: row.nome)
            }
            return null
        } finally {
            sql.close()
        }
    }
}