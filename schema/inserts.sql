INSERT INTO candidatos (cpf, nome, data_nascimento, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('111.111.111-11', 'Túlio Vilela Lopes', '2004-10-01', 'tulio@gmail.com', '12345678', 'Brasil',
        'Minas Gerais', 'Belo Horizonte', '11111-11', 'Minha descricao de candidato é...');

INSERT INTO candidatos (cpf, nome, data_nascimento, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('222.222.222-22', 'João Silva', '2001-06-20', 'joao@gmail.com', '12345678', 'Brasil',
        'Minas Gerais', 'João Monlevade', '22222-22', 'Minha descricao de candidato é...');

INSERT INTO candidatos (cpf, nome, data_nascimento, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('333.333.333-33', 'Maria Souza', '2003-03-10', 'maria@gmail.com', '12345678', 'Brasil',
        'São Paulo', 'São Paulo', '33333-33', 'Minha descricao de candidato é...');

INSERT INTO candidatos (cpf, nome, data_nascimento, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('444.444.444-44', 'Carlos Oliveira', '1997-09-05', 'carlos@gmail.com', '12345678', 'Brasil',
        'Rio de Janeiro', 'Rio de Janeiro', '44444-44', 'Minha descricao de candidato é...');

INSERT INTO candidatos (cpf, nome, data_nascimento, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('555.555.555-55', 'Ana Santos', '1999-12-18', 'ana@gmail.com', '12345678', 'Brasil',
        'Minas Gerais', 'Uberlândia', '55555-55', 'Minha descricao de candidato é...');

SELECT * FROM candidatos;






INSERT INTO empresas (cnpj, nome, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('11.111.111/1111-11', 'ACZG','aczg@gmail.com', '12345678', 'Brasil',
        'Minas Gerais', 'Belo Horizonte', '11111-11', 'Minha descricao de empresa é...');

INSERT INTO empresas (cnpj, nome, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('22.222.222/2222-22', 'Tech Solutions', 'techsolutions@gmail.com', '12345678', 'Brasil',
        'São Paulo', 'São Paulo', '22222-22', 'Minha descricao de empresa é...');

INSERT INTO empresas (cnpj, nome, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('33.333.333/3333-33', 'Dev Corp', 'devcorp@gmail.com', '12345678', 'Brasil',
        'Minas Gerais', 'Belo Horizonte', '33333-33', 'Minha descricao de empresa é...');

INSERT INTO empresas (cnpj, nome, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('44.444.444/4444-44', 'DataTech', 'datatech@gmail.com', '12345678', 'Brasil',
        'Rio de Janeiro', 'Rio de Janeiro', '44444-44', 'Minha descricao de empresa é....');

INSERT INTO empresas (cnpj, nome, email, senha, pais, estado, cidade, cep, descricao)
VALUES ('55.555.555/5555-55', 'Web Solutions', 'websolutions@gmail.com', '12345678', 'Brasil',
        'Paraná', 'Curitiba', '55555-55', 'Minha descricao de empresa é...');

SELECT * FROM empresas;





INSERT INTO competencias (nome) VALUES ('Java');
INSERT INTO competencias (nome) VALUES ('Groovy');
INSERT INTO competencias (nome) VALUES ('Python');
INSERT INTO competencias (nome) VALUES ('HTML');
INSERT INTO competencias (nome) VALUES ('CSS');
INSERT INTO competencias (nome) VALUES ('JavaScript');
INSERT INTO competencias (nome) VALUES ('TypeScript');
INSERT INTO competencias (nome) VALUES ('C++');
INSERT INTO competencias (nome) VALUES ('C');
INSERT INTO competencias (nome) VALUES ('C#');
INSERT INTO competencias (nome) VALUES ('JUnit');
INSERT INTO competencias (nome) VALUES ('Spock');
INSERT INTO competencias (nome) VALUES ('Scrum');
INSERT INTO competencias (nome) VALUES ('Kanbam');
INSERT INTO competencias (nome) VALUES ('Linux');
INSERT INTO competencias (nome) VALUES ('Docker');
INSERT INTO competencias (nome) VALUES ('Postgres');
INSERT INTO competencias (nome) VALUES ('Git/GitHub');
INSERT INTO competencias (nome) VALUES ('Angular');

TRUNCATE TABLE candidatos, empresas, competencias, curtidas_candidato, curtidas_empresa RESTART IDENTITY CASCADE;

SELECT * FROM competencias;




INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES
    ('Desenvolvedor Backend', 'Desenvolver backend', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 1);
INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES
    ('Desenvolvedor Frontend', 'Desenvolver frontend', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 2);
INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES
    ('DevOps', 'Desenvolver infraestrura', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 3);
INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES
    ('Cybersecurity', 'Desenvolver backend', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 4);
INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES
    ('Desenvolvedor Fullstack', 'Desenvolver interfaces e regras de négocio', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 5);
SELECT * FROM vagas;





INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 1);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 2);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 3);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 4);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 5);

INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (2, 6);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (2, 7);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (2, 8);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (2, 9);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (2, 10);

INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (3, 11);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (3, 12);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (3, 13);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (3, 14);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (3, 15);

INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (4, 16);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (4, 17);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (4, 18);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (4, 19);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (4, 15);

INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (5, 10);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (5, 12);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (5, 3);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (5, 14);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (5, 9);


SELECT * FROM candidatos_competencias;





INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 6);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 7);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 8);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 9);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 10);

INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (2, 1);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (2, 2);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (2, 3);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (2, 4);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (2, 5);

INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (3, 11);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (3, 12);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (3, 13);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (3, 14);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (3, 15);

INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (4, 16);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (4, 17);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (4, 18);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (4, 19);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (4, 5);

INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (5, 3);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (5, 7);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (5, 16);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (5, 9);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (5, 15);

SELECT * FROM vagas_competencias;





INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu) VALUES (1, 1, TRUE);

SELECT * FROM curtidas_candidato;



INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu) VALUES (1, 1, 1, TRUE);

SELECT * FROM curtidas_empresa;

SELECT ct.id, '***.***.***-**' AS cpf, 'anonimo' || ct.id AS nome, 'oculto@email.com' AS email,
       ct.senha, ct.pais, ct.estado, ct.cidade, ct.cep, ct.data_nascimento, ct.descricao,
       cm.id AS id_competencia, cm.nome AS nome_competencia
FROM candidatos AS ct
         LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
         LEFT JOIN competencias cm ON cm.id = c.id_competencia;


SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade,
       e.id AS id_empresa, e.nome AS nome_empresa,
       cm.id AS id_competencia, cm.nome AS nome_competencia
FROM vagas v
         INNER JOIN empresas e ON v.id_empresa = e.id
         LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
         LEFT JOIN competencias cm ON cm.id = vc.id_competencia



SELECT v.id, v.nome AS nome_vaga, v.descricao, v.pais, v.estado, v.cidade,
       e.id AS id_empresa, 'anonimo' || e.id AS nome_empresa,
       cm.id AS id_competencia, cm.nome AS nome_competencia
FROM vagas v
         INNER JOIN empresas e ON v.id_empresa = e.id
         LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
         LEFT JOIN competencias cm ON cm.id = vc.id_competencia

SELECT ct.*, cm.id AS id_competencia, cm.nome AS nome_competencia FROM candidatos ct
                                                                           LEFT JOIN candidatos_competencias c ON ct.id = c.id_candidato
                                                                           LEFT JOIN competencias cm ON cm.id = c.id_competencia
WHERE ct.id = 3;


SELECT v.*, cm.id AS id_competencia, cm.nome AS nome_competencia FROM vagas v
                                                                          LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
                                                                          LEFT JOIN competencias cm ON cm.id = vc.id_competencia
WHERE v.id = 3;


SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade, v.id_empresa AS id_empresa, cm.id AS id_competencia, cm.nome AS nome_competencia
FROM vagas v
         LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
         LEFT JOIN competencias cm ON cm.id = vc.id_competencia
WHERE v.id_empresa = 3 AND v.id = 3;


SELECT v.id, v.nome, v.descricao, v.pais, v.estado, v.cidade, v.id_empresa AS id_empresa, cm.id AS id_competencia, cm.nome AS nome_competencia
FROM vagas v
         LEFT JOIN vagas_competencias vc ON v.id = vc.id_vaga
         LEFT JOIN competencias cm ON cm.id = vc.id_competencia
WHERE v.id_empresa = 3 AND v.id ;