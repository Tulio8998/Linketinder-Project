# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.
Este formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e o projeto adota [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Adicionado
- Spec funcional da feature Recrutador em `docs/specs/recrutador/spec.md`.
- Plano de implementação incremental da feature Recrutador em
  `docs/specs/recrutador/plano.md`.
- Migration PostgreSQL incremental para Recrutador, áreas de vagas e autoria de
  avaliações em `schema/update_recrutador.sql`, com cópia sincronizada no backend.
- Teste SQL isolado para a migration de Recrutador em
  `schema/tests/update_recrutador_test.sql`.
- Backend da feature Recrutador: cadastro e desativação lógica, filtro por área,
  autoria imutável da primeira avaliação, histórico e contato pós-match.
- Endpoints sensíveis agora exigem identidade autenticada; perfis e vagas
  anônimos omitem dados pessoais e aceitam IDs numéricos explícitos.
- Testes Spock de Services, Controllers e integração PostgreSQL em schema
  temporário isolado.
- Frontend integrado aos endpoints REST para cadastro e login de candidatos e
  empresas, gestão de recrutadores, triagem anônima, avaliações e contato após
  match; credenciais Basic Auth permanecem somente em memória.
- Cliente HTTP TypeScript centralizado em `Frontend/src/api/api.ts`; serviços
  assíncronos cobrem também competências, vagas, curtidas, matches e
  recrutadores sem usar `localStorage`.
- Dashboard do recrutador e gestão de recrutadores no painel da empresa,
  reutilizando a estrutura visual existente sem alterações nos arquivos CSS.

### Modificado
- Simplificados os endpoints de vagas, triagem e curtida para trafegar IDs
  numéricos diretamente em vez de tokens opacos temporários.
- Consulta anônima de vaga por ID retorna apenas o DTO público, sem identidade
  da empresa.
- Formulário de vaga passou a exigir área; IDs locais são atribuídos a vagas e
  candidatos legados para relacionar avaliações do MVP.
- Vistas pré-match de candidato e recrutador omitem localização e identidade da
  outra parte na interface.
- Filtro HTTP Basic valida e-mail/senha contra o banco, instala o `Principal`
  validado e habilita CORS para o frontend local em `http://localhost:5173`.
- Avaliações positivas e negativas do candidato agora são enviadas à API e
  persistidas por vaga, junto às avaliações do recrutador.

### Corrigido
- Nenhuma alteração registrada.

### Removido
- Stores temporários de tokens opacos para vagas e avaliações de recrutador.

## [2.1.0] - 2026-11-10

### Adicionado
- Controllers e DTOs para separar o transporte e a interface da lógica de negócio.
- Testes unitários de controllers com Spock e mocks.
- Persistência PostgreSQL com DAOs, CRUDs relacionais e consultas SQL organizadas por entidade.
- Interface web em TypeScript com cadastro, login, dashboards, vagas, competências e fluxo de curtidas/matches.

### Modificado
- Menus da CLI desacoplados dos serviços e da persistência.
- Arquitetura organizada em camadas, com Services, DAOs, Models e Value Objects.
- Fluxo de curtidas para identificar interesse mútuo na mesma vaga e calcular afinidade por competências.

### Corrigido
- Validações de Value Objects e regras de cadastro para evitar dados nulos e duplicidades de CPF, CNPJ e e-mail.
- Isolamento dos testes de serviço e persistência por meio de mocks e configuração específica de banco de testes.

### Removido
- Acesso direto dos menus da CLI à camada de persistência e aos serviços.
