package com.zg.linketinder.dao

import com.zg.linketinder.dao.interfaces.RecrutadorDAO
import com.zg.linketinder.dto.AvaliacaoRecrutadorDTO
import com.zg.linketinder.dto.CandidatoAnonimoDTO
import com.zg.linketinder.dto.MatchRecrutadorDTO
import com.zg.linketinder.dto.VagaResumoDTO
import com.zg.linketinder.factory.ConexaoDB
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.sql.RecrutadorSql
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO

class RecrutadorDAOImpl implements RecrutadorDAO {

    @Override
    void salvar(Recrutador recrutador) {
        ConexaoDB.emTransacao { sql ->
            def row = sql.firstRow(RecrutadorSql.INSERIR, [
                    recrutador.empresa.id, recrutador.nome, recrutador.area,
                    recrutador.descricao, recrutador.email.valor, recrutador.cpf.valor,
                    recrutador.dataNascimento, recrutador.senha
            ])
            recrutador.id = row.id as Integer
            recrutador.ativo = true
        }
    }

    @Override
    Recrutador buscarPorId(Integer id) {
        return ConexaoDB.consultar { sql ->
            def row = sql.firstRow(RecrutadorSql.BUSCAR_POR_ID, [id])
            row == null ? null : mapearRecrutador(row)
        }
    }

    @Override
    Recrutador buscarPorEmail(String email) {
        return ConexaoDB.consultar { sql ->
            def row = sql.firstRow(RecrutadorSql.BUSCAR_POR_EMAIL, [email])
            row == null ? null : mapearRecrutador(row)
        }
    }

    @Override
    boolean existeCpf(String cpf) {
        return ConexaoDB.consultar { sql ->
            sql.firstRow(RecrutadorSql.EXISTE_CPF, [cpf]).existe as Boolean
        }
    }

    @Override
    List<Recrutador> listarPorEmpresa(Integer empresaId) {
        return ConexaoDB.consultar { sql ->
            sql.rows(RecrutadorSql.LISTAR_POR_EMPRESA, [empresaId]).collect { row ->
                new Recrutador(
                        id: row.id as Integer,
                        empresa: new Empresa(id: row.id_empresa as Integer),
                        nome: row.nome,
                        area: row.area,
                        descricao: row.descricao,
                        email: new EmailVO(row.email),
                        ativo: row.ativo as Boolean
                )
            }
        }
    }

    @Override
    boolean desativar(Integer recrutadorId, Integer empresaId) {
        return ConexaoDB.emTransacao { sql ->
            sql.executeUpdate(RecrutadorSql.DESATIVAR, [recrutadorId, empresaId]) == 1
        }
    }

    @Override
    List<VagaResumoDTO> listarVagasDaArea(Integer recrutadorId) {
        return ConexaoDB.consultar { sql ->
            agruparResumosVaga(sql.rows(RecrutadorSql.LISTAR_VAGAS_DA_AREA, [recrutadorId]))
        }
    }

    @Override
    List<CandidatoAnonimoDTO> listarCandidatosAnonimos(Integer recrutadorId, Integer vagaId) {
        return ConexaoDB.consultar { sql ->
            Map<Integer, CandidatoAnonimoDTO> perfis = [:]
            sql.rows(RecrutadorSql.LISTAR_CANDIDATOS_ANONIMOS, [recrutadorId, vagaId]).each { row ->
                Integer id = row.id as Integer
                if (!perfis.containsKey(id)) {
                    perfis[id] = new CandidatoAnonimoDTO(
                            id: id,
                            descricao: row.descricao,
                            competencias: [],
                            formacoes: []
                    )
                }
                if (row.nome_competencia != null) {
                    perfis[id].competencias.add(row.nome_competencia as String)
                }
            }
            perfis.values().toList()
        }
    }

    @Override
    boolean registrarAvaliacao(Integer recrutadorId, Integer candidatoId, Integer vagaId, Boolean curtiu) {
        return ConexaoDB.emTransacao { sql ->
            !sql.rows(RecrutadorSql.REGISTRAR_AVALIACAO, [
                    candidatoId, curtiu, recrutadorId, vagaId
            ]).isEmpty()
        }
    }

    @Override
    List<AvaliacaoRecrutadorDTO> listarHistorico(Integer recrutadorId) {
        return ConexaoDB.consultar { sql ->
            Map<Integer, AvaliacaoRecrutadorDTO> avaliacoes = [:]
            sql.rows(RecrutadorSql.LISTAR_HISTORICO, [recrutadorId]).each { row ->
                Integer id = row.id as Integer
                if (!avaliacoes.containsKey(id)) {
                    avaliacoes[id] = new AvaliacaoRecrutadorDTO(
                            autor: row.nome_autor,
                            vaga: row.nome_vaga,
                            area: row.area,
                            descricaoCandidato: row.descricao_candidato,
                            competencias: [],
                            curtiu: row.curtiu as Boolean
                    )
                }
                if (row.nome_competencia != null) {
                    avaliacoes[id].competencias.add(row.nome_competencia as String)
                }
            }
            avaliacoes.values().toList()
        }
    }

    @Override
    List<MatchRecrutadorDTO> listarMatchesCandidato(Integer candidatoId) {
        return ConexaoDB.consultar { sql ->
            sql.rows(RecrutadorSql.LISTAR_MATCHES_CANDIDATO, [candidatoId]).collect { row ->
                new MatchRecrutadorDTO(
                        nomeRecrutador: row.nome_recrutador,
                        emailRecrutador: row.email_recrutador,
                        nomeEmpresa: row.nome_empresa,
                        emailEmpresa: row.email_empresa,
                        cnpjEmpresa: row.cnpj_empresa,
                        descricaoEmpresa: row.descricao_empresa,
                        paisEmpresa: row.pais_empresa,
                        estadoEmpresa: row.estado_empresa,
                        cidadeEmpresa: row.cidade_empresa,
                        cepEmpresa: row.cep_empresa,
                        nomeVaga: row.nome_vaga
                )
            }
        }
    }

    private Recrutador mapearRecrutador(def row) {
        return new Recrutador(
                id: row.id as Integer,
                empresa: new Empresa(id: row.id_empresa as Integer),
                nome: row.nome,
                area: row.area,
                descricao: row.descricao,
                email: row.email == null ? null : new EmailVO(row.email),
                cpf: row.cpf == null ? null : new CpfVO(row.cpf),
                dataNascimento: row.data_nascimento?.toLocalDate(),
                senha: row.senha,
                ativo: row.ativo as Boolean
        )
    }

    private List<VagaResumoDTO> agruparResumosVaga(List rows) {
        Map<Integer, VagaResumoDTO> vagas = [:]
        rows.each { row ->
            Integer id = row.id as Integer
            if (!vagas.containsKey(id)) {
                vagas[id] = new VagaResumoDTO(
                        id: id,
                        nome: row.nome,
                        descricao: row.descricao,
                        area: row.area,
                        competencias: []
                )
            }
            if (row.nome_competencia != null) {
                vagas[id].competencias.add(row.nome_competencia as String)
            }
        }
        return vagas.values().toList()
    }
}
