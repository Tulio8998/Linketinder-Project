-- Migration incremental para a feature Recrutador.
-- Aplicar com: psql -1 -v ON_ERROR_STOP=1 -d linketinder -f schema/update_recrutador.sql
-- Não executar o DDL base: ele contém CREATE DATABASE e DROP DATABASE.

CREATE TABLE IF NOT EXISTS recrutadores (
    id SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    nome VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    email VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    data_nascimento DATE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_recrutadores_empresa
        FOREIGN KEY (id_empresa)
        REFERENCES empresas(id)
        ON DELETE RESTRICT,
    CONSTRAINT uq_recrutadores_email UNIQUE (email),
    CONSTRAINT uq_recrutadores_cpf UNIQUE (cpf),
    CONSTRAINT uq_recrutadores_id_empresa
        UNIQUE (id, id_empresa)
);

ALTER TABLE vagas
    ADD COLUMN IF NOT EXISTS area VARCHAR(100);

-- Estes são os únicos títulos legados definidos no seed do projeto; todos
-- pertencem à área de TI. Antes de implantar, inclua um UPDATE explícito por ID
-- para cada outro título legado; nunca atribua uma área genérica como fallback.
UPDATE vagas
SET area = 'TI'
WHERE area IS NULL
  AND nome IN (
      'Desenvolvedor Backend',
      'Desenvolvedor Frontend',
      'DevOps',
      'Cybersecurity',
      'Desenvolvedor Fullstack'
  );

DO $$
DECLARE
    vagas_sem_area TEXT;
BEGIN
    SELECT string_agg(id::TEXT, ', ' ORDER BY id)
    INTO vagas_sem_area
    FROM vagas
    WHERE area IS NULL;

    IF vagas_sem_area IS NOT NULL THEN
        RAISE EXCEPTION
            'Migration interrompida: informe uma area aprovada para as vagas legadas de id % antes de tornar vagas.area obrigatoria.',
            vagas_sem_area;
    END IF;
END
$$;

ALTER TABLE vagas
    ALTER COLUMN area SET NOT NULL;

ALTER TABLE curtidas_empresa
    ADD COLUMN IF NOT EXISTS id_recrutador INTEGER;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_curtidas_empresa_recrutador_empresa'
          AND conrelid = 'curtidas_empresa'::REGCLASS
    ) THEN
        ALTER TABLE curtidas_empresa
            ADD CONSTRAINT fk_curtidas_empresa_recrutador_empresa
            FOREIGN KEY (id_recrutador, id_empresa)
            REFERENCES recrutadores (id, id_empresa)
            ON DELETE RESTRICT;
    END IF;
END
$$;
