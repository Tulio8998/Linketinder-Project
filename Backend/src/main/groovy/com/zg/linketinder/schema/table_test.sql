CREATE DATABASE linketinder_test;
postgresgrDROP DATABASE linketinder;

CREATE TABLE candidatos(
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL CHECK (data_nascimento <= CURRENT_DATE - INTERVAL '18 years' AND data_nascimento >= CURRENT_DATE - INTERVAL '115 years'),
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) CHECK(LENGTH(senha) >= 6) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    estado VARCHAR(60) NOT NULL,
    cidade VARCHAR(60) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    descricao TEXT NOT NULL
);

SELECT * FROM candidatos;
SELECT * FROM empresas;


CREATE TABLE empresas(
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(100) CHECK(LENGTH(senha) >= 6) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    estado VARCHAR(60) NOT NULL,
    cidade VARCHAR(60) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    descricao TEXT NOT NULL
);

CREATE TABLE competencias(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE vagas(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    pais VARCHAR(50) NOT NULL,
    estado VARCHAR(60) NOT NULL,
    cidade VARCHAR(60) NOT NULL,
    id_empresa INT REFERENCES empresas(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE candidatos_competencias(
    id_candidato INT REFERENCES candidatos(id) ON DELETE CASCADE ,
    id_competencia INT REFERENCES competencias(id) ON DELETE CASCADE,
    PRIMARY KEY (id_candidato, id_competencia)
);

CREATE TABLE vagas_competencias(
    id_vaga INT REFERENCES vagas(id) ON DELETE CASCADE,
    id_competencia INT REFERENCES competencias(id) ON DELETE CASCADE,
    PRIMARY KEY (id_vaga, id_competencia)
);

CREATE TABLE curtidas_candidato(
    id SERIAL PRIMARY KEY,
    id_candidato INT REFERENCES candidatos(id) ON DELETE CASCADE NOT NULL,
    id_vaga INT REFERENCES vagas(id) ON DELETE CASCADE NOT NULL,
    curtiu BOOLEAN DEFAULT FALSE,
    UNIQUE (id_candidato, id_vaga)
);

CREATE TABLE curtidas_empresa(
    id SERIAL PRIMARY KEY,
    id_empresa INT REFERENCES empresas(id) ON DELETE CASCADE NOT NULL,
    id_candidato INT REFERENCES candidatos(id) ON DELETE CASCADE NOT NULL,
    id_vaga INT REFERENCES vagas(id) ON DELETE CASCADE NOT NULL,
    curtiu BOOLEAN DEFAULT FALSE,
    UNIQUE (id_empresa, id_candidato, id_vaga)
);




-- 1. Cria a tabela de recrutadores
CREATE TABLE recrutadores (
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
        FOREIGN KEY (id_empresa) REFERENCES empresas(id) ON DELETE RESTRICT,
    CONSTRAINT uq_recrutadores_email UNIQUE (email),
    CONSTRAINT uq_recrutadores_cpf UNIQUE (cpf),
    CONSTRAINT uq_recrutadores_id_empresa UNIQUE (id, id_empresa)
);

-- 2. Adiciona a coluna area na tabela vagas
ALTER TABLE vagas ADD COLUMN area VARCHAR(100);

-- 3. Atualiza as vagas legadas do seu banco para 'TI' para não quebrar a restrição NOT NULL
UPDATE vagas SET area = 'TI' WHERE area IS NULL;

-- 4. Torna a coluna area obrigatória
ALTER TABLE vagas ALTER COLUMN area SET NOT NULL;

-- 5. Adiciona o id_recrutador na curtidas_empresa para rastrear a autoria
ALTER TABLE curtidas_empresa ADD COLUMN id_recrutador INTEGER;

-- 6. Cria a Foreign Key amarrando o recrutador à empresa correta na curtida
ALTER TABLE curtidas_empresa
    ADD CONSTRAINT fk_curtidas_empresa_recrutador_empresa
    FOREIGN KEY (id_recrutador, id_empresa)
    REFERENCES recrutadores (id, id_empresa)
    ON DELETE RESTRICT;