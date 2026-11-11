package com.zg.linketinder.dao

import com.zg.linketinder.factory.ConexaoDB
import com.zg.linketinder.factory.DatabaseFactory
import com.zg.linketinder.factory.PostgresTestFactory
import com.zg.linketinder.model.AvaliacaoRecrutador
import com.zg.linketinder.model.Empresa
import com.zg.linketinder.model.PerfilCandidatoAnonimo
import com.zg.linketinder.model.Recrutador
import com.zg.linketinder.model.Vaga
import com.zg.linketinder.vo.CpfVO
import com.zg.linketinder.vo.EmailVO
import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class RecrutadorDaoIntegrationSpec extends Specification {
    @Shared
    String schema = "recrutador_test_${UUID.randomUUID().toString().replace('-', '')}"

    @Shared
    Sql adminSql

    private final RecrutadorDAOImpl dao = new RecrutadorDAOImpl()

    def setupSpec() {
        adminSql = Sql.newInstance(
                'jdbc:postgresql://localhost:5432/linketinder_test',
                'postgres',
                'postgres',
                'org.postgresql.Driver'
        )
        adminSql.execute('CREATE SCHEMA ' + schema)
        adminSql.execute('SET search_path TO ' + schema)
        criarEstruturaIsolada()
        ConexaoDB.setFactory(new DatabaseFactory() {
            @Override
            Sql criarConexao() {
                return Sql.newInstance(
                        "jdbc:postgresql://localhost:5432/linketinder_test?currentSchema=${schema}",
                        'postgres',
                        'postgres',
                        'org.postgresql.Driver'
                )
            }
        })
    }

    def cleanupSpec() {
        ConexaoDB.resetar()
        if (adminSql != null) {
            try {
                adminSql.execute('DROP SCHEMA IF EXISTS ' + schema + ' CASCADE')
            } finally {
                adminSql.close()
            }
        }
        ConexaoDB.setFactory(new PostgresTestFactory())
    }

    def setup() {
        Sql sql = ConexaoDB.getConexao()
        sql.execute('TRUNCATE TABLE curtidas_candidato, curtidas_empresa, vagas_competencias, ' +
                'candidatos_competencias, vagas, candidatos, recrutadores, competencias, empresas ' +
                'RESTART IDENTITY CASCADE')
        inserirFixture(sql)
    }

    def "persiste recrutador e permite localizar por email e CPF"() {
        given:
        Recrutador recrutador = new Recrutador(
                empresa: new Empresa(id: 1),
                nome: 'Recrutadora Nova',
                area: 'TI',
                descricao: 'Seleção de tecnologia',
                email: new EmailVO('nova@example.com'),
                cpf: new CpfVO('987.654.321-00'),
                dataNascimento: LocalDate.of(1992, 5, 6),
                senha: 'senha-em-texto-plano'
        )

        when:
        dao.salvar(recrutador)
        Recrutador recuperado = dao.buscarPorEmail('NOVA@example.com')

        then:
        recrutador.id != null
        recuperado.id == recrutador.id
        recuperado.empresa.id == 1
        recuperado.email.valor == 'nova@example.com'
        recuperado.cpf.valor == '987.654.321-00'
        recuperado.dataNascimento == LocalDate.of(1992, 5, 6)
        recuperado.senha == 'senha-em-texto-plano'
        dao.existeCpf('987.654.321-00')
    }

    def "limita vagas e perfis anonimos a empresa e área do recrutador"() {
        when:
        List<Vaga> vagas = dao.listarVagasDaArea(10)
        List<PerfilCandidatoAnonimo> perfis = dao.listarCandidatosAnonimos(10, 201)

        then:
        vagas*.id == [201, 204]
        vagas*.area.every { it == 'TI' }
        perfis*.idInterno == [301, 302]
        perfis.every { it.descricao && it.competencias }
    }

    def "primeira avaliação vence, mantém autoria após soft delete e libera contato só no match"() {
        when:
        boolean primeira = dao.registrarAvaliacao(10, 301, 201, true)
        boolean conflito = dao.registrarAvaliacao(13, 301, 201, false)
        List<AvaliacaoRecrutador> historico = dao.listarHistorico(13)
        ConexaoDB.getConexao().execute(
                'INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu) VALUES (301, 201, TRUE)')
        def matches = dao.listarMatchesCandidato(301)
        boolean desativado = dao.desativar(10, 1)
        List<AvaliacaoRecrutador> historicoDepoisDoDesligamento = dao.listarHistorico(13)
        boolean avaliacaoInativa = dao.registrarAvaliacao(10, 302, 201, true)

        then:
        primeira
        !conflito
        historico.size() == 1
        historico.first().autor == 'Recrutador TI'
        historico.first().curtiu
        matches.size() == 1
        matches.first().nomeRecrutador == 'Recrutador TI'
        matches.first().emailRecrutador == 'recrutador-ti@example.com'
        matches.first().nomeEmpresa == 'Empresa Um'
        !matches.first().properties.keySet().any {
            it.toLowerCase().contains('cpf') || it.toLowerCase().contains('nascimento') ||
                    it.toLowerCase().contains('senha')
        }
        desativado
        historicoDepoisDoDesligamento.first().autor == 'Recrutador TI'
        !avaliacaoInativa
    }

    def "permite avaliação distinta por vaga e rejeita área ou empresa alheias"() {
        when:
        boolean positiva = dao.registrarAvaliacao(10, 301, 201, true)
        boolean negativaEmOutraVaga = dao.registrarAvaliacao(10, 301, 204, false)
        boolean vagaDeOutraEmpresa = dao.registrarAvaliacao(10, 301, 203, true)
        boolean vagaDeOutraArea = dao.registrarAvaliacao(11, 301, 201, true)

        then:
        positiva
        negativaEmOutraVaga
        !vagaDeOutraEmpresa
        !vagaDeOutraArea
        ConexaoDB.getConexao().firstRow(
                'SELECT COUNT(*) AS total FROM curtidas_empresa WHERE id_candidato = 301').total == 2
    }

    def "DAO de curtidas exige recrutador ativo da empresa e área da vaga"() {
        given:
        CurtidaDAOImpl curtidaDAO = new CurtidaDAOImpl()

        when:
        boolean autorizada = curtidaDAO.salvarCurtidaEmpresa(1, 10, 302, 201, true)
        boolean areaIncorreta = curtidaDAO.salvarCurtidaEmpresa(1, 11, 301, 201, true)
        boolean empresaIncorreta = curtidaDAO.salvarCurtidaEmpresa(1, 12, 301, 201, true)
        def autoria = ConexaoDB.getConexao().firstRow(
                'SELECT id_recrutador FROM curtidas_empresa WHERE id_candidato = 302').id_recrutador

        then:
        autorizada
        !areaIncorreta
        !empresaIncorreta
        autoria == 10
    }

    def "match legado sem autor não fabrica contato de recrutador"() {
        when:
        Sql sql = ConexaoDB.getConexao()
        sql.execute('''
            INSERT INTO curtidas_empresa
                (id_empresa, id_candidato, id_vaga, curtiu, id_recrutador)
            VALUES (1, 301, 201, TRUE, NULL)
        ''')
        sql.execute('''
            INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu)
            VALUES (301, 201, TRUE)
        ''')
        def match = dao.listarMatchesCandidato(301).first()

        then:
        match.nomeRecrutador == null
        match.emailRecrutador == null
        match.nomeEmpresa == 'Empresa Um'
    }

    private void criarEstruturaIsolada() {
        executarStatements(adminSql, '''
            CREATE TABLE empresas (
                id SERIAL PRIMARY KEY, nome TEXT NOT NULL, email TEXT NOT NULL,
                cnpj TEXT NOT NULL, senha TEXT, descricao TEXT, pais TEXT,
                estado TEXT, cidade TEXT, cep TEXT
            );
            CREATE TABLE recrutadores (
                id SERIAL PRIMARY KEY, id_empresa INTEGER NOT NULL REFERENCES empresas(id),
                nome TEXT NOT NULL, area TEXT NOT NULL, descricao TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE, cpf TEXT NOT NULL UNIQUE,
                data_nascimento DATE NOT NULL, senha TEXT NOT NULL, ativo BOOLEAN NOT NULL DEFAULT TRUE,
                UNIQUE (id, id_empresa)
            );
            CREATE TABLE vagas (
                id SERIAL PRIMARY KEY, id_empresa INTEGER NOT NULL REFERENCES empresas(id),
                nome TEXT NOT NULL, descricao TEXT NOT NULL, area TEXT NOT NULL
            );
            CREATE TABLE candidatos (
                id SERIAL PRIMARY KEY, nome TEXT NOT NULL, email TEXT NOT NULL,
                cpf TEXT NOT NULL, data_nascimento DATE, descricao TEXT NOT NULL, senha TEXT
            );
            CREATE TABLE competencias (
                id SERIAL PRIMARY KEY, nome TEXT NOT NULL UNIQUE
            );
            CREATE TABLE candidatos_competencias (
                id_candidato INTEGER NOT NULL REFERENCES candidatos(id),
                id_competencia INTEGER NOT NULL REFERENCES competencias(id),
                PRIMARY KEY (id_candidato, id_competencia)
            );
            CREATE TABLE vagas_competencias (
                id_vaga INTEGER NOT NULL REFERENCES vagas(id),
                id_competencia INTEGER NOT NULL REFERENCES competencias(id),
                PRIMARY KEY (id_vaga, id_competencia)
            );
            CREATE TABLE curtidas_candidato (
                id SERIAL PRIMARY KEY, id_candidato INTEGER NOT NULL REFERENCES candidatos(id),
                id_vaga INTEGER NOT NULL REFERENCES vagas(id), curtiu BOOLEAN NOT NULL,
                UNIQUE (id_candidato, id_vaga)
            );
            CREATE TABLE curtidas_empresa (
                id SERIAL PRIMARY KEY, id_empresa INTEGER NOT NULL REFERENCES empresas(id),
                id_candidato INTEGER NOT NULL REFERENCES candidatos(id),
                id_vaga INTEGER NOT NULL REFERENCES vagas(id), curtiu BOOLEAN NOT NULL,
                id_recrutador INTEGER,
                UNIQUE (id_empresa, id_candidato, id_vaga),
                FOREIGN KEY (id_recrutador, id_empresa)
                    REFERENCES recrutadores(id, id_empresa) ON DELETE RESTRICT
            );
        ''')
    }

    private static void inserirFixture(Sql sql) {
        executarStatements(sql, """
            INSERT INTO empresas (id, nome, email, cnpj, descricao)
            VALUES
                (1, 'Empresa Um', 'empresa1@example.com', '12.345.678/0001-90', 'Empresa principal'),
                (2, 'Empresa Dois', 'empresa2@example.com', '98.765.432/0001-10', 'Outra empresa');
            INSERT INTO recrutadores
                (id, id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha)
            VALUES
                (10, 1, 'Recrutador TI', 'TI', 'Equipe de TI', 'recrutador-ti@example.com',
                    '111.111.111-11', '1990-01-01', 'senha'),
                (11, 1, 'Recrutador RH', 'RH', 'Equipe de RH', 'recrutador-rh@example.com',
                    '222.222.222-22', '1991-01-01', 'senha'),
                (12, 2, 'Recrutador Externo', 'TI', 'Outra empresa', 'externo@example.com',
                    '333.333.333-33', '1992-01-01', 'senha'),
                (13, 1, 'Segundo Recrutador TI', 'TI', 'Equipe de TI', 'segundo-ti@example.com',
                    '444.444.444-44', '1993-01-01', 'senha');
            INSERT INTO vagas (id, id_empresa, nome, descricao, area)
            VALUES
                (201, 1, 'Vaga TI 1', 'Desenvolvimento', 'TI'),
                (202, 1, 'Vaga RH', 'Pessoas', 'RH'),
                (203, 2, 'Vaga TI externa', 'Outra empresa', 'TI'),
                (204, 1, 'Vaga TI 2', 'Plataforma', 'TI');
            INSERT INTO candidatos (id, nome, email, cpf, descricao, senha)
            VALUES
                (301, 'Candidato Um', 'cand1@example.com', '555.555.555-55',
                    'Perfil candidato um', 'segredo'),
                (302, 'Candidato Dois', 'cand2@example.com', '666.666.666-66',
                    'Perfil candidato dois', 'segredo');
            INSERT INTO competencias (id, nome)
            VALUES (401, 'Groovy'), (402, 'PostgreSQL');
            INSERT INTO candidatos_competencias (id_candidato, id_competencia)
            VALUES (301, 401), (302, 402);
            INSERT INTO vagas_competencias (id_vaga, id_competencia)
            VALUES (201, 401), (204, 402);
            SELECT setval(pg_get_serial_sequence('recrutadores', 'id'), 100, FALSE);
        """)
    }

    private static void executarStatements(Sql sql, String script) {
        script.split(/;\s*(?:\r?\n|$)/)
                .collect { it.trim() }
                .findAll { !it.isEmpty() }
                .each { sql.execute(it) }
    }
}
