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
- Interface local de recrutador com cadastro/desativação pela empresa, triagem
  anônima por área, histórico de autoria e matches com contato pós-match
  (demonstração local com dados sintéticos; sem integração REST).

### Modificado
- Simplificados os endpoints de vagas, triagem e curtida para trafegar IDs
  numéricos diretamente em vez de tokens opacos temporários.
- Consulta anônima de vaga por ID retorna apenas o DTO público, sem identidade
  da empresa.
- Formulário de vaga passou a exigir área; IDs locais são atribuídos a vagas e
  candidatos legados para relacionar avaliações do MVP.
- Vistas pré-match de candidato e recrutador omitem localização e identidade da
  outra parte.

### Corrigido
- Nenhuma alteração registrada.

### Removido
- Stores temporários de tokens opacos para vagas e avaliações de recrutador.

## [1.9.0] - 2026-11-10

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
