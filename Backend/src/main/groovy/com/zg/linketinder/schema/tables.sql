CREATE DATABASE linketinder;
DROP DATABASE linketinder;

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

