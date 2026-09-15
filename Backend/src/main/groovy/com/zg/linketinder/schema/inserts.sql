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

--TRUNCATE TABLE candidatos, empresas, competencias, curtidas_candidato, curtidas_empresa RESTART IDENTITY CASCADE;

SELECT * FROM competencias;




INSERT INTO vagas (nome, descricao, pais, estado, cidade, id_empresa) VALUES 
('Desenvolvedor Backend', 'Desenvolver backend', 'Brasil', 'Minas Gerais', 'Belo Horizonte', 1);

SELECT * FROM vagas;





INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 1);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 2);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 3);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 4);
INSERT INTO candidatos_competencias (id_candidato, id_competencia) VALUES (1, 5);

SELECT * FROM candidatos_competencias;





INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 6);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 7);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 8);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 9);
INSERT INTO vagas_competencias (id_vaga, id_competencia) VALUES (1, 10);

SELECT * FROM vagas_competencias;





INSERT INTO curtidas_candidato (id_candidato, id_vaga, curtiu) VALUES (1, 1, TRUE);

SELECT * FROM curtidas_candidato;



INSERT INTO curtidas_empresa (id_empresa, id_candidato, id_vaga, curtiu) VALUES (1, 1, 1, TRUE);

SELECT * FROM curtidas_empresa;



