# Feature: Recrutador

> **Estado do rascunho: integração implementada; validação ponta a ponta
> pendente.** O frontend usa os endpoints REST e Basic Auth, mas a API não
> permaneceu acessível durante a tentativa de execução local para comprovar o
> fluxo completo pelo navegador.

Closes #<NUMERO_DA_ISSUE>

## O que foi feito

- Migration incremental para recrutadores, área da vaga e autoria das
  avaliações, sem alterar intencionalmente os scripts base.
- Backend Groovy com cadastro/desativação lógica de recrutadores, filtro de
  vagas e candidatos por empresa/área, primeira avaliação imutável, histórico
  e consulta de contato após match.
- DTOs de triagem anônima e de vaga pública; APIs de listagem e avaliação
  trafegam IDs numéricos em vez de tokens opacos.
- Frontend TypeScript integrado via `fetch` para cadastro/login, vagas,
  gerenciamento de recrutadores, triagem anônima, avaliações e contato após
  match. O frontend não usa `localStorage`; credenciais Basic permanecem em
  memória.
- Filtro Basic valida e-mail/senha no banco, instala o `Principal` validado e
  habilita CORS para `http://localhost:5173`.

## Como testar

1. Prepare o PostgreSQL local conforme o DDL e a migration da feature. Use um
   banco descartável para o teste manual; os testes de integração criam schema
   temporário no banco `linketinder_test`.
2. Execute a suíte backend:

   ```sh
   cd Backend
   ./gradlew test --rerun-tasks
   ```

3. Compile o frontend:

   ```sh
   cd Frontend
   npm install
   npm run build
   ```

4. Em terminais separados, execute `cd Backend && ./gradlew runApi` e
   `cd Frontend && npm run dev`. No navegador, cadastre uma empresa e uma vaga,
   cadastre um candidato, crie um recrutador pela empresa, entre como
   recrutador, faça a triagem/avaliação da vaga e entre como candidato para
   curtir a mesma vaga e conferir o contato do match.
5. Confirme no PostgreSQL de teste as linhas gravadas em `recrutadores`,
   `curtidas_empresa` e `curtidas_candidato`. Não use dados reais ou produção.

## Anonimato e privacidade

- Os testes `LegacyApiPrivacySpec` e `RecrutadorControllerSpec` verificam que
  os DTOs públicos/pré-match não serializam identidade da empresa/candidato,
  CPF, e-mail ou senha; o contato do recrutador só aparece na consulta de match
  do candidato autenticado.
- A triagem usa `CandidatoAnonimoDTO` e exibe somente descrição, formação
  disponível e competências; resumos de vaga não expõem a empresa.
- A identidade do recrutador e da empresa só é exibida no painel de matches do
  candidato autenticado após like mútuo na mesma vaga. CPF, nascimento e senha
  do recrutador não fazem parte do DTO de match.
- O filtro valida credenciais Basic. Como o projeto usa HTTP sem TLS e ainda
  mantém senhas em texto puro no banco conforme a decisão do MVP, execute
  somente em ambiente local descartável.
- O endpoint REST foi coberto por testes de autenticação/privacidade e as
  camadas de avaliação/match por testes PostgreSQL. O fluxo navegador→API ativo
  não foi comprovado nesta execução.

## Critérios de aceite

- [x] Migration incremental, relacionamento de autoria e soft delete
  preservam o histórico.
- [x] Backend filtra vagas por empresa/área e registra a primeira avaliação
  sem sobrescrita.
- [x] DTOs anônimos do backend omitem dados pessoais pré-match e a consulta de
  match limita contato ao candidato participante autenticado.
- [x] Frontend consome os endpoints REST com credenciais Basic validadas e
  grava
  cadastro/avaliações no PostgreSQL.
- [ ] Teste ponta a ponta comprova o fluxo completo e o anonimato com o
  frontend integrado à API.

## Validações adicionais

- `cd Backend && ./gradlew test --rerun-tasks`: passou.
- `cd Frontend && npm run build`: passou (TypeScript e Vite).
- Tentativa de execução da API não permaneceu acessível em `localhost:8080`;
  por isso o teste manual ponta a ponta não foi marcado como concluído.
