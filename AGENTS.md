# Linketinder — instruções do projeto

## 1. Visão geral e stack

Linketinder é uma aplicação de recrutamento que aproxima candidatos e empresas por meio de vagas, competências e curtidas. O repositório contém:

- `Backend/`: backend em Groovy 5, construído com Gradle. A aplicação oferece uma CLI e uma API REST com Tomcat embutido, Jakarta Servlets, Jackson e acesso JDBC/PostgreSQL via Groovy SQL.
- `Frontend/`: interface em TypeScript sem framework, com manipulação direta do DOM, Vite, HTML e CSS. ApexCharts é usado para visualizações.
- `schema/`: scripts SQL e modelos de dados do PostgreSQL. Há cópias dos scripts também em `Backend/src/main/groovy/com/zg/linketinder/schema/`.

O backend segue camadas: Servlet/Controller e DTO para transporte HTTP, Services para regras de negócio, Models/VOs para o domínio, DAOs para persistência e classes `Sql` para consultas. Preserve essa separação e os padrões já usados ao implementar funcionalidades.

## 2. Domínio e relacionamentos

- **Candidato**: pessoa que busca vagas; possui CPF, data de nascimento, perfil e competências.
- **Empresa**: organização que publica vagas; possui CNPJ e perfil.
- **Vaga**: pertence a uma empresa (`vagas.id_empresa`) e possui descrição, localização e competências exigidas.
- **Competência**: catálogo compartilhado de habilidades.
- **Curtidas**: avaliações vinculadas a candidato e vaga, registradas separadamente conforme o ator.
- **Match**: interesse mútuo entre candidato e empresa para uma vaga específica.

Relacionamentos no PostgreSQL:

- Empresa 1:N Vagas.
- Candidato N:N Competências, por `candidatos_competencias`.
- Vaga N:N Competências, por `vagas_competencias`.
- `curtidas_candidato` associa candidato e vaga, com um estado `curtiu` e unicidade por par.
- `curtidas_empresa` associa empresa, candidato e vaga, com um estado `curtiu` e unicidade pela combinação.

O modelo atual não possui entidade/tabela de formação acadêmica. Se essa capacidade for adicionada, a informação de formação permanece sujeita à regra de anonimato abaixo.

## 3. Regras centrais de curtida e match

- Um candidato pode curtir ou passar uma vaga; a avaliação é persistida em `curtidas_candidato`.
- A empresa dona da vaga pode curtir ou passar um candidato para essa vaga; a avaliação é persistida em `curtidas_empresa`. Não permita que uma empresa avalie candidatos em vagas de outra empresa.
- Curtidas são independentes e podem ser atualizadas. `curtiu = FALSE` representa uma avaliação negativa, não um match.
- O match acontece somente quando candidato e empresa dão `curtiu = TRUE` para o mesmo candidato e a mesma vaga. A empresa participante deve ser a proprietária dessa vaga.
- O match é calculado pelas consultas SQL que cruzam as duas tabelas de curtidas; não há tabela persistente de matches no esquema atual.
- Afinidade de competências é uma métrica de compatibilidade e não substitui nem cria um match.

## 4. Restrição obrigatória: anonimato antes do match

Esta regra prevalece sobre conveniência da interface e deve ser seguida em toda feature, rota, consulta, DTO, serialização e visualização:

- Antes do match, perfis de candidatos e empresas podem revelar somente competências, formações (quando disponíveis) e descrição. Mantenha a identidade e os dados pessoais ocultos para ambas as partes.
- Nome, e-mail, CPF, CNPJ, contato, endereço e qualquer outro dado que identifique a pessoa ou empresa só podem ser revelados após o match. Não exponha esses dados em respostas JSON, consultas, logs acessíveis ao usuário, metadados, atributos HTML ou identificadores estáveis que permitam descobrir a identidade.
- Oculte também a identidade da empresa nas vagas exibidas antes do match; mostre apenas o conteúdo permitido e necessário para a avaliação anônima.
- A autorização e a filtragem devem ocorrer no backend, não apenas por ocultação visual no frontend. Verifique todos os caminhos de leitura, inclusive listagens, busca por ID, curtidas, afinidade e matches.
- Após um match confirmado, revele os dados somente às partes daquele match. Não use a existência de uma curtida unilateral como autorização.
- Crie ou atualize testes para comprovar o formato anônimo antes do match e a revelação somente depois do match.

Não presuma que a implementação existente já satisfaz essa política: há consultas e endpoints que serializam Models ou selecionam campos pessoais. Revise e filtre cada resposta envolvida antes de reutilizá-los.

## 5. Banco de dados: scripts e execução

Os scripts de referência ficam em `schema/tables.sql` (DDL) e `schema/inserts.sql` (dados de demonstração); cópias existem dentro de `Backend/src/main/groovy/com/zg/linketinder/schema/`. Mantenha as cópias sincronizadas se alterar ambas.

**Atenção:** `tables.sql` contém `CREATE DATABASE` e `DROP DATABASE`; não execute o arquivo inteiro sem revisar/omitir essas instruções e confirmar o banco de destino. `inserts.sql` contém `TRUNCATE ... CASCADE` e depende da ordem e dos dados inseridos; use-o somente em banco local descartável, depois de revisar o conteúdo. Nunca rode esses scripts em produção.

Para configurar um PostgreSQL local, crie o banco e execute o DDL revisado (sem os comandos de criação/exclusão de banco):

```sh
createdb -U postgres linketinder
psql -v ON_ERROR_STOP=1 -U postgres -d linketinder -f schema/tables.sql
```

Opcionalmente, carregue dados de demonstração somente após revisar o seed:

```sh
psql -v ON_ERROR_STOP=1 -U postgres -d linketinder -f schema/inserts.sql
```

O backend usa por padrão `jdbc:postgresql://localhost:5432/linketinder`, usuário e senha `postgres`. A conexão de produção pode ser configurada com as propriedades Java `db.url`, `db.user`, `db.password` e `db.driver`. Os testes de integração usam `linketinder_test` via `PostgresTestFactory` e esperam PostgreSQL local com usuário/senha `postgres`; prepare esse banco com o DDL antes de executar a suíte completa.

## 6. Como executar a aplicação e os testes

Use o wrapper Gradle a partir de `Backend/`:

```sh
./gradlew run       # CLI
./gradlew runApi    # API REST em http://localhost:8080
./gradlew test      # testes Spock (JUnit Platform)
```

Os testes ficam em `Backend/src/test/groovy/`; `DaoIntegrationSpec` e seus testes derivados precisam do banco `linketinder_test`. Os testes unitários de Services e Controllers usam Spock e mocks.

Use npm a partir de `Frontend/`:

```sh
npm install
npm run dev         # servidor Vite
npm run build       # verificação TypeScript e build de produção
```

O `package.json` do frontend não define script de testes automatizados; não invente um comando de testes sem adicionar e documentar a ferramenta correspondente.
