\set ON_ERROR_STOP on

DO $$
BEGIN
    IF current_database() <> 'linketinder_test' THEN
        RAISE EXCEPTION
            'Este teste só pode ser executado no banco linketinder_test; banco atual: %',
            current_database();
    END IF;

    IF to_regnamespace('recrutador_migration_test') IS NOT NULL THEN
        RAISE EXCEPTION
            'O schema recrutador_migration_test já existe; não será removido nem reutilizado.';
    END IF;
END
$$;

BEGIN;

CREATE SCHEMA recrutador_migration_test;
SET LOCAL search_path TO recrutador_migration_test, public;

CREATE TABLE empresas (
    id SERIAL PRIMARY KEY
);

CREATE TABLE vagas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    id_empresa INTEGER NOT NULL REFERENCES empresas(id)
);

CREATE TABLE curtidas_empresa (
    id SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL REFERENCES empresas(id),
    id_candidato INTEGER NOT NULL,
    id_vaga INTEGER NOT NULL REFERENCES vagas(id),
    curtiu BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (id_empresa, id_candidato, id_vaga)
);

INSERT INTO empresas (id) VALUES (1), (2);

INSERT INTO vagas (nome, id_empresa) VALUES
    ('Desenvolvedor Backend', 1),
    ('Desenvolvedor Frontend', 1),
    ('DevOps', 1),
    ('Cybersecurity', 2),
    ('Desenvolvedor Fullstack', 2);

INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu)
VALUES (1, 10, 1, TRUE);

\ir ../update_recrutador.sql
\ir ../update_recrutador.sql

DO $$
DECLARE
    recrutador_id INTEGER;
BEGIN
    IF (SELECT count(*) FROM recrutadores) <> 0 THEN
        RAISE EXCEPTION 'A migration não deve criar recrutadores fictícios.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'vagas'
          AND column_name = 'area'
          AND is_nullable <> 'NO'
    ) THEN
        RAISE EXCEPTION 'vagas.area deve ser NOT NULL após o backfill.';
    END IF;

    IF EXISTS (SELECT 1 FROM vagas WHERE area <> 'TI') THEN
        RAISE EXCEPTION 'Os títulos de vaga conhecidos devem ser classificados como TI.';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'recrutadores'
          AND column_name = 'ativo'
          AND column_default = 'true'
    ) THEN
        RAISE EXCEPTION 'recrutadores.ativo deve ter default TRUE.';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'fk_recrutadores_empresa'
          AND conrelid = 'recrutadores'::REGCLASS
          AND contype = 'f'
    ) OR NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'uq_recrutadores_email'
          AND conrelid = 'recrutadores'::REGCLASS
          AND contype = 'u'
    ) OR NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'uq_recrutadores_cpf'
          AND conrelid = 'recrutadores'::REGCLASS
          AND contype = 'u'
    ) THEN
        RAISE EXCEPTION 'A tabela recrutadores deve impor FK de empresa e unicidade de e-mail/CPF.';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM curtidas_empresa
        WHERE id_empresa = 1
          AND id_candidato = 10
          AND id_vaga = 1
          AND curtiu IS TRUE
          AND id_recrutador IS NULL
    ) THEN
        RAISE EXCEPTION 'A avaliação legada deve permanecer intacta e sem autoria inventada.';
    END IF;

    INSERT INTO recrutadores (
        id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha
    ) VALUES (
        1, 'Recrutador Teste', 'TI', 'Perfil de teste',
        'recrutador@example.test', '000.000.000-00',
        DATE '1990-01-01', 'senha-teste'
    )
    RETURNING id INTO recrutador_id;

    INSERT INTO recrutadores (
        id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha
    ) VALUES (
        2, 'Outro Recrutador', 'TI', 'Outro perfil de teste',
        'outro@example.test', '111.111.111-11',
        DATE '1991-01-01', 'outra-senha'
    );

    BEGIN
        INSERT INTO recrutadores (
            id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha
        ) VALUES (
            2, 'E-mail duplicado', 'TI', 'Teste de unicidade',
            'recrutador@example.test', '222.222.222-22',
            DATE '1992-01-01', 'senha-teste'
        );
        RAISE EXCEPTION 'E-mail de recrutador deve ser único.';
    EXCEPTION
        WHEN unique_violation THEN
            NULL;
    END;

    BEGIN
        INSERT INTO recrutadores (
            id_empresa, nome, area, descricao, email, cpf, data_nascimento, senha
        ) VALUES (
            2, 'CPF duplicado', 'TI', 'Teste de unicidade',
            'cpf-duplicado@example.test', '000.000.000-00',
            DATE '1992-01-01', 'senha-teste'
        );
        RAISE EXCEPTION 'CPF de recrutador deve ser único.';
    EXCEPTION
        WHEN unique_violation THEN
            NULL;
    END;

    INSERT INTO curtidas_empresa (
        id_empresa, id_candidato, id_vaga, curtiu, id_recrutador
    ) VALUES (1, 11, 1, FALSE, recrutador_id);

    BEGIN
        INSERT INTO curtidas_empresa (
            id_empresa, id_candidato, id_vaga, curtiu, id_recrutador
        ) VALUES (1, 11, 1, TRUE, recrutador_id);
        RAISE EXCEPTION 'A unicidade por empresa/candidato/vaga deve impedir uma segunda avaliação.';
    EXCEPTION
        WHEN unique_violation THEN
            NULL;
    END;

    IF NOT EXISTS (
        SELECT 1
        FROM curtidas_empresa
        WHERE id_empresa = 1
          AND id_candidato = 11
          AND id_vaga = 1
          AND curtiu IS FALSE
          AND id_recrutador = recrutador_id
    ) THEN
        RAISE EXCEPTION 'A primeira avaliação e sua autoria devem permanecer imutáveis.';
    END IF;

    BEGIN
        INSERT INTO curtidas_empresa (
            id_empresa, id_candidato, id_vaga, curtiu, id_recrutador
        ) VALUES (1, 12, 1, TRUE, recrutador_id + 2);
        RAISE EXCEPTION 'A avaliação não pode referenciar recrutador inexistente.';
    EXCEPTION
        WHEN foreign_key_violation THEN
            NULL;
    END;

    BEGIN
        INSERT INTO curtidas_empresa (
            id_empresa, id_candidato, id_vaga, curtiu, id_recrutador
        ) VALUES (1, 13, 1, TRUE, recrutador_id + 1);
        RAISE EXCEPTION 'A avaliação não pode referenciar recrutador de outra empresa.';
    EXCEPTION
        WHEN foreign_key_violation THEN
            NULL;
    END;

    BEGIN
        DELETE FROM recrutadores WHERE id = recrutador_id;
        RAISE EXCEPTION 'Não se pode apagar recrutador com autoria histórica.';
    EXCEPTION
        WHEN restrict_violation THEN
            NULL;
    END;

    IF NOT EXISTS (
        SELECT 1 FROM recrutadores WHERE id = recrutador_id AND ativo IS TRUE
    ) THEN
        RAISE EXCEPTION 'A tentativa de exclusão não pode remover o recrutador autor.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM pg_views
        WHERE schemaname = current_schema()
    ) THEN
        RAISE EXCEPTION 'A migration não deve criar views que possam expor dados sensíveis.';
    END IF;
END
$$;

ROLLBACK;
