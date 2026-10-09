## Autor
Desenvolvido por Túlio

## Tecnologias Utilizadas
* **Groovy:** Linguagem principal do projeto (Lógica, Orientação a Objetos, GroovyBeans, Tipagem Dinâmica)
* **Gradle:** Ferramenta de build e automação
* **Spock Framework:** Utilizado para a cobertura de testes unitários e desenvolvimento orientado a testes (TDD).
* **TypeScript:** Linguagem principal do frontend, garantindo tipagem estática, contratos de dados (Models) e regras de manipulação na interface.
* **Vite:** Ferramenta de build e servidor frontend.
* **ApexCharts:** Biblioteca de visualização de dados utilizada para a plotagem dos gráficos interativos de competências.
* **PostgreSQL:** Sistema de gerenciamento de banco de dados relacional (SGBD) utilizado para a persistência física, consultas e garantia da integridade dos dados.
* **brModelo:** Ferramenta utilizada para a modelagem conceitual e lógica do banco (criação do Diagrama e Modelo Entidade-Relacionamento).

## Comentários
O projeto foi desenvolvido focando na entrega do Produto Mínimo Viável (MVP) solicitado, priorizando a organização estrutural, aplicação de conceitos de Orientação a Objetos e tratamento de erros de interação. Algumas decisões da implementação foram:

* **Organização do projeto:** O código foi separado em camadas (Model, Service, Util e CLI), garantindo que cada pacote tenha uma responsabilidade única e facilitando a manutenção.
* **Dados em Memória:** Para validar os dados do MVP rapidamente, os Services foram inicializados com 5 empresas e 5 candidatos pré-cadastrados, utilizando o construtor de mapas nativo do Groovy.
### Versão 1.1.0
* **Sistema de Vagas:** Implementação do cadastro e listagem de vagas vinculadas às empresas.
* **Sistema de Curtidas:** Adição da lógica de relacionamento. Agora, candidatos podem curtir vagas, e empresas podem curtir candidatos. Quando o interesse é mútuo, o sistema registra o evento de Match.
* **Refatoração Arquitetural:** O projeto ganhou uma nova camada de Repository para isolar a persistência separando da camada de Service.

### Versão 1.2.0

* **Cobertura de Testes Unitários:** Implementação de testes automatizados utilizando o Spock Framework para as camadas de Model, Repository e Service de Empresa e Candidato.
* **TDD:** Desenvolvimento guiado por testes para garantir a confiabilidade da inserção de dados no sistema.
* **Validações de Regras de Negócio:** Inclusão de proteção contra Edge Cases na camada de Service, bloqueando o cadastro de usuários nulos e prevenindo a duplicidade de CPFs, CNPJs e e-mails.
* **Isolamento com Mocks:** Aplicação de Mocking nos testes de serviço para isolar a comunicação com a camada de persistência.

### Versão 1.3.0

* **Interface Frontend:** Construção da interface visual do sistema utilizando manipulação direta do DOM com Vanilla TypeScript, focada na experiência do usuário
* **Telas e Dashboards Dinâmicos:** Criação dos fluxos de Login, Cadastro (Candidato e Empresa) e Dashboards interativos para o gerenciamento de perfis e de vagas.
* **Regra de Negócio Visual:** Implementação do bloqueio de identidade, candidatos e vagas são exibidos de forma anônima e os nomes reais só são revelados após a interação e confirmação na aba de Matches.
* **Visualização de Dados Gráfica:** Integração para renderização de gráficos de barras interativos exibindo a métrica e contagem de candidatos separados por competência.

### Versão 1.4.0

* **Arquitetura e Persistência:** Refatoração completa da camada de serviços (Candidato, Empresa, Vaga e Curtida) para o formato de classes.
* **Índice de Afinidade:** Criação de compatibilidade que cruza dados e exibe dinamicamente a porcentagem (%) de "Match" nos cards de vagas para os candidatos, e na triagem de perfis para as empresas.
* **Sistema de Interações (Curtir/Passar):** Implementação definitiva da lógica de aprovação e rejeição, salvando o histórico de avaliações no banco local para impedir a renderização duplicada de candidatos e vagas já processados.

### Versão 1.5.0

* **Modelagem de Dados (DER/MER):** Criação do Diagrama Entidade-Relacionamento e arquitetura lógica do banco de dados utilizando o software brModelo, estruturando as entidades de Candidatos, Empresas, Vagas e Competências.
* **Banco de Dados Relacional:** Implementação da estrutura física em PostgreSQL.
* **Scripts SQL:** Desenvolvimento dos arquivos de estruturação da base de dados (DDL) e população inicial para testes com candidatos e empresas fictícias (DML).

### Versão 1.6.0
* **Integração com Banco de Dados via JDBC:** Conexão nativa da aplicação backend em Groovy com o PostgreSQL utilizando a biblioteca groovy.sql.Sql, garantindo a persistência real dos dados.
* **Implementação do Padrão DAO:** Refatoração da arquitetura com a criação da camada Data Access Object (DAO), isolando a lógica de acesso a dados (queries e inserts SQL) das regras de negócio (Services) e da interface de linha de comando (CLI).
* **CRUDs Relacionais Completos:** Desenvolvimento das operações de Inserção, Leitura, Atualização e Exclusão integradas ao banco para as 4 entidades principais (Candidatos, Empresas, Vagas e Competências), incluindo o gerenciamento via SQL dos relacionamentos 1:N (Empresas-Vagas) e N:N (Competências com Vagas e Candidatos).

### Versão 1.7.0

* **Aplicação dos Princípios SOLID:** Refatoração da arquitetura com foco em SRP, OCP, LSP, ISP e DIP, incluindo interfaces específicas, classes base abstratas e injeção de dependência por construtor.
* **Camada Base Genérica (Template Method):** Criação de `CrudDAO` e `CrudDAOImpl` para concentrar o CRUD repetitivo, eliminando `try/catch/finally` duplicados. DAOs concretos apenas informam SQL e mapeamento.
* **Camada de Serviço Genérica:** Criação de `CrudService`, que centraliza o fluxo de salvar, atualizar, excluir e listar, delegando validações para `validarAntesSalvar` e `validarAntesAtualizar`.
* **Configuração Externalizada:** Remoção de credenciais do `ConexaoDB`, com leitura via `System.getProperty` e fallback para valores padrão.
* **Conexão Compartilhada:** O `ConexaoDB` agora mantém uma única instância de `Sql`, com `resetar()` e `criar()` para permitir troca de banco em testes.
* **SQL Centralizado:** Queries movidas para classes `Sql` por entidade, removendo SQL dos DAOs.
* **Testes de Integração:** Criação do banco `linketinder_test` e da classe `DaoIntegrationSpec`, que limpa as tabelas antes de cada teste.
* **Testes Unitários com Mock:** Injeção de dependência por construtor nos Services, permitindo `Mock()` do Spock para isolar a persistência.
* **DRY no Frontend:** Extração de `shared.ts` para eliminar duplicação de `configSkills`, `pointSkills`, `abrirPainelCard` e manipulação de skills.
* **Constantes Compartilhadas:** Criação de `ts/util/constants.ts` para `ESTADOS` e `PAISES`, eliminando arrays duplicados.
* **Validação com Regex:** Introdução de `ts/util/validators.ts` com regex para e-mail, CPF, CNPJ e CEP nos fluxos de cadastro.
* **ViewPanel Unificado:** `viewVagaCand` passa a delegar para `viewPanel`, eliminando duplicação.
* **Template Reutilizável no Frontend:** `addVaga` e `editVaga` compartilham a função `templateEditavel`, eliminando HTML duplicado.

### Versão 1.8.0
- **Consolidação de Padrões de Projeto (Design Patterns):** Refinamento da arquitetura do sistema com a aplicação e formalização de diversos padrões conceituados de engenharia de software:
    - **Factory Method:** Implementação do `DatabaseFactory` (com `PostgresFactory` e `PostgresTestFactory`) para encapsular a lógica de criação e injeção de conexões de banco de dados, facilitando a transição fluida entre ambientes de produção e teste.
    - **Singleton:** Refinamento do gerenciador `ConexaoDB`, garantindo que a aplicação compartilhe uma única instância centralizada de conexão e transação com o banco de dados durante todo o ciclo de vida da execução.
    - **Template Method:** Aplicação contínua na camada base genérica (`CrudDAOImpl` e `CrudService`), onde o esqueleto dos algoritmos de CRUD é definido na classe pai, delegando apenas os detalhes específicos (como queries SQL e mapeamento de dados) para as subclasses.
    - **Dependency Injection (Injeção de Dependência):** Utilizada fortemente na transição das classes de Teste (`DaoIntegrationSpec`) e Services, injetando o comportamento esperado (Mocks ou Factories) via construtor para isolar contextos e facilitar os testes unitários e de integração.
    - **Data Access Object (DAO):** Padrão arquitetural mantido e aprimorado para isolar completamente a camada de persistência física (SQL/JDBC) das regras de negócio.
- **Value Objects (VOs) (Domain-Driven Design):** Introdução de objetos de valor ricos e imutáveis (`CpfVO`, `CnpjVO`, `EmailVO`) para encapsular a formatação e as regras de negócio vitais, garantindo auto-validação desde o momento da instanciação.
- **Encapsulamento Estrutural (Endereço):** Criação do VO `Endereco` para agrupar atributos de localização (país, estado, cidade, CEP), removendo a repetição excessiva (Code Smell) desses campos das entidades `Pessoa`, `Candidato`, `Empresa` e `Vaga`.
- **Adaptação de DAOs:** Refatoração nos parâmetros de Inserção e Atualização dos DAOs para descompactar os atributos dos VOs (ex: `candidato.cpf.valor`) corretamente na base de dados relacional e remontá-los de forma transparente na leitura.
- **Refatoração de Testes (DaoIntegrationSpec):** Atualização da suíte de testes de integração para utilizar a Injeção de Dependência da Factory (`PostgresTestFactory`) direto no Singleton `ConexaoDB`, garantindo que os DAOs e o ambiente de teste rodem exatamente sobre a mesma transação, permitindo o rollback seguro entre os testes.

## Execução pela IDE
1. Abra o projeto no IntelliJ IDEA.
2. Aguarde o Gradle sincronizar o projeto.
3. Localize a classe Main.groovy dentro do pacote com.zg.linketinder.
4. Clique no botão Run ao lado do método main.
5. Siga as instruções do menu interativo no terminal.

## Execução via Gradle
1. Abra o terminal na pasta Backend.
2. Execute o comando correspondente ao seu sistema: ./gradlew run

## Execução de Interface
1. Abra o terminal na pasta Frontend.
2. Execute o comando: npm i
3. E depois execute o comando: npm run dev
4. Entre no link para ver a interface: `http://localhost:5173/`

Observação: O projeto utiliza **Groovy 5.0.8** e a ferramenta de build Gradle **9.7.0**.