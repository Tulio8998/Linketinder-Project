package com.zg.linketinder.dao

import com.zg.linketinder.dao.base.CrudDAOImpl
import com.zg.linketinder.model.Competencia
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.sql.VagaSql
import com.zg.linketinder.factory.ConexaoDB
import groovy.sql.Sql

class VagaDAOImpl extends CrudDAOImpl<Vaga> implements VagaDAO {

    @Override
    protected String getSqlInserir() { return VagaSql.INSERIR }

    @Override
    protected String getSqlAtualizar() { return VagaSql.ATUALIZAR }

    @Override
    protected String getSqlExcluir() { return 'DELETE FROM vagas WHERE id = ?' }

    @Override
    protected String getSqlBuscarPorId() { return VagaSql.BUSCAR_POR_ID }

    @Override
    protected String getSqlListarTodos() { return VagaSql.LISTAR_TODAS }

    @Override
    protected List getParametrosInserir(Vaga vaga) {
        return [vaga.nome, vaga.descricao, vaga.pais, vaga.estado, vaga.cidade, vaga.empresa.id]
    }

    @Override
    protected List getParametrosAtualizar(Integer id, Vaga vaga) {
        return [vaga.nome, vaga.descricao, vaga.pais, vaga.estado, vaga.cidade, id]
    }

    @Override
    protected Vaga mapearResultado(def row) {
        return mapearLinha(row)
    }

    @Override
    List<Vaga> listarTodos() {
        return ConexaoDB.consultar { Sql sql ->
            return agruparPorId(sql.rows(VagaSql.LISTAR_TODAS))
        }
    }

    @Override
    Vaga buscarPorId(Integer id) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(VagaSql.BUSCAR_POR_ID, [id]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    @Override
    void salvar(Vaga vaga) {
        ConexaoDB.emTransacao { Sql sql ->
            def chaves = sql.executeInsert(VagaSql.INSERIR, getParametrosInserir(vaga))
            long idVaga = chaves[0][0] as long
            salvarCompetencias(sql, idVaga, vaga.competencias)
            vaga.id = (int) idVaga
        }
    }

    @Override
    void excluirVaga(Integer idEmpresa, Integer idVaga) {
        ConexaoDB.emTransacao { Sql sql ->
            sql.executeUpdate(VagaSql.EXCLUIR, [idVaga, idEmpresa])
        }
    }

    @Override
    List<Vaga> listarVagaEmpresa(Integer idEmpresa) {
        return ConexaoDB.consultar { Sql sql ->
            return agruparPorId(sql.rows(VagaSql.LISTAR_POR_EMPRESA, [idEmpresa]))
        }
    }

    @Override
    Vaga buscarIdVagaEmpresa(Integer idEmpresa, Integer idVaga) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(VagaSql.BUSCAR_POR_ID_E_EMPRESA, [idEmpresa, idVaga]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    @Override
    Vaga buscarNomeVagaEmpresa(Integer idEmpresa, String nome) {
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(VagaSql.BUSCAR_POR_NOME_E_EMPRESA, [idEmpresa, nome]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    @Override
    Vaga buscarNomeVaga(String nome) {
        String query = '''
            SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,   
                   v.id_empresa AS id_empresa, e.nome AS nome_empresa, 
                   cm.id AS id_competencia, cm.nome AS nome_competencia 
            FROM vagas v
            LEFT JOIN empresas e ON v.id_empresa = e.id
            LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
            LEFT JOIN competencias cm ON cm.id = vc.id_competencia
            WHERE v.nome = ?
        '''
        return ConexaoDB.consultar { Sql sql ->
            def lista = agruparPorId(sql.rows(query, [nome]))
            return lista.isEmpty() ? null : lista.first()
        }
    }

    private List<Vaga> agruparPorId(List linhas) {
        if (!linhas || linhas.isEmpty()) return []
        Map<Integer, Vaga> mapa = [:]

        linhas.each { linha ->
            Integer id = (linha.id instanceof Number) ? ((Number) linha.id).intValue() : Integer.parseInt(linha.id.toString())
            if (!mapa.containsKey(id)) {
                mapa[id] = mapearLinha(linha)
            }
            if (linha.id_competencia != null) {
                Integer idComp = (linha.id_competencia instanceof Number) ?
                        ((Number) linha.id_competencia).intValue() : Integer.parseInt(linha.id_competencia.toString())
                mapa[id].competencias.add(new Competencia(id: idComp, nome: linha.nome_competencia))
            }
        }
        return mapa.values().toList()
    }

    private Vaga mapearLinha(linha) {
        Integer idEmp = linha.id_empresa != null ?
                ((linha.id_empresa instanceof Number) ? ((Number) linha.id_empresa).intValue() : Integer.parseInt(linha.id_empresa.toString())) : null

        String nomeVaga = linha.containsKey('nome_vaga') && linha.nome_vaga ? linha.nome_vaga : linha.nome
        String nomeEmpresa = linha.containsKey('nome_empresa') ? linha.nome_empresa : ""

        Empresa emp = new Empresa(id: idEmp, nome: nomeEmpresa)

        return new Vaga(
                id: (linha.id instanceof Number) ? ((Number) linha.id).intValue() : Integer.parseInt(linha.id.toString()),
                nome: nomeVaga,
                descricao: linha.descricao,
                pais: linha.pais,
                estado: linha.estado,
                cidade: linha.cidade,
                empresa: emp,
                competencias: []
        )
    }

    private void salvarCompetencias(Sql sql, long idVaga, List<Competencia> competencias) {
        if (!competencias) return
        competencias.each { competencia ->
            sql.executeInsert(VagaSql.INSERIR_COMPETENCIA, [idVaga, competencia.id])
        }
    }
}