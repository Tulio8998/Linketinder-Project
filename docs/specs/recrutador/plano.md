# Plano de implementação — Recrutador

Plano derivado de [spec.md](./spec.md). Executar os grupos na ordem estrita:
**Banco de Dados → Backend e testes → Frontend**. Não iniciar um grupo antes de
concluir e validar o anterior. Este plano descreve trabalho futuro; não altera
o schema nem implementa a feature.

## Regras para todos os grupos

- Conservar `schema/tables.sql` e `schema/inserts.sql`; toda mudança de schema
  deve ser uma migration incremental em arquivo separado e sincronizada com a
  cópia em `Backend/src/main/groovy/com/zg/linketinder/schema/`.
- Não atribuir valores de `area` a vagas existentes sem mapeamento aprovado.
  O backfill deve classificar os dados legados antes de tornar a coluna
  obrigatória.
- Preservar avaliações existentes sem autor (`id_recrutador = NULL`) e não
  inventar autoria, excluir histórico, sobrescrever a primeira decisão ou
  remover recrutadores fisicamente.
- Em cada tarefa, completar o checklist de anonimato: antes do match, o
  recrutador só acessa competências, formação (se disponível) e descrição do
  candidato; depois do match, somente as partes recebem os dados autorizados.
  CPF, data de nascimento e senha do recrutador nunca são divulgados; senha
  também nunca vai para resposta ou log.
- Executar comandos a partir do diretório indicado. Testes de DAO usam
  `linketinder_test`; aplicar migrations somente nesse banco local de teste ou
  em outro banco descartável preparado para validação, nunca em produção.

## 1. Banco de Dados

### BD-1 — Criar migration incremental para recrutadores

- [x] Criar a tabela `recrutadores` com FK obrigatória para `empresas`, campos
  especificados, unicidade de CPF e e-mail corporativo e `ativo = TRUE` por
  padrão. Não configurar exclusão em cascata que apague avaliações ou autoria.
- **Arquivos afetados:**
  - `schema/update_recrutador.sql` (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/schema/update_recrutador.sql` (novo,
    cópia sincronizada)
- **Teste:** aplicar migration em banco local descartável preparado com o DDL
  base; consultar `information_schema` para confirmar colunas, FK, índices e
  default de `ativo`; confirmar que a exclusão de recrutador com avaliações não
  apaga registros relacionados.
- **Checklist de anonimato:**
  - [x] O schema conserva os campos sensíveis para uso interno, mas não cria
    view/consulta pública que exponha CPF, data de nascimento ou senha.
  - [x] FK e regras de retenção não permitem que exclusões removam autoria de
    avaliação.

### BD-2 — Adicionar área à vaga com migração de dados legados

- [x] Adicionar a coluna `vagas.area` e torná-la obrigatória conforme a spec.
  Antes de finalizar o `NOT NULL`, definir e aplicar mapeamento aprovado para
  todas as vagas existentes; não usar área genérica ou valor arbitrário como
  fallback. Não impor catálogo fechado, que está fora do escopo.
- **Arquivos afetados:**
  - `schema/update_recrutador.sql`
  - `Backend/src/main/groovy/com/zg/linketinder/schema/update_recrutador.sql`
- **Teste:** no banco local de teste, verificar que o backfill classifica todos
  os registros existentes e que `area` fica `NOT NULL`; validar rejeição de
  nova vaga sem área. Reexecutar a validação sem executar DDL base destrutivo.
- **Checklist de anonimato:**
  - [x] Área não revela identidade da empresa, recrutador ou candidato nas
    listagens anônimas de vagas.
  - [x] Nenhum dado pessoal é usado como critério de backfill ou publicado
    junto à área.

### BD-3 — Registrar autoria nas avaliações da empresa

- [x] Adicionar `curtidas_empresa.id_recrutador` como FK sem cascade. Permitir
  `NULL` somente para preservar avaliações históricas; manter a unicidade
  existente por empresa/candidato/vaga, sem criar segunda decisão por autor.
- **Arquivos afetados:**
  - `schema/update_recrutador.sql`
  - `Backend/src/main/groovy/com/zg/linketinder/schema/update_recrutador.sql`
- **Teste:** validar FK, nulabilidade histórica e unicidade; confirmar que linhas
  legadas continuam presentes e com autor nulo. Verificar que tentativa de
  atribuir recrutador de outra empresa é rejeitada pela validação de domínio e,
  quando viável, por integridade relacional.
- **Checklist de anonimato:**
  - [x] A coluna de autoria não é exposta em leituras anônimas pré-match.
  - [x] A migration não altera valores de curtida nem atribui autor a registros
    preexistentes.

## 2. Backend e testes (Spock)

### BE-1 — Adicionar modelos e contratos de transporte

- [x] Criar `Recrutador` com empresa, área, descrição, dados cadastrais e
  estado ativo; adicionar `area` a `Vaga` e referência do autor a
  `CurtidaEmpresa`. Criar DTOs de entrada/saída necessários, separando dados
  cadastrais de representações anônimas e de contato pós-match. Reutilizar os
  Value Objects existentes para e-mail/CPF ou criar apenas os que forem
  necessários, sem relaxar validações.
- **Arquivos afetados:**
  - `Backend/src/main/groovy/com/zg/linketinder/model/Recrutador.groovy` (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/model/Vaga.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/model/curtida/CurtidaEmpresa.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/dto/RecrutadorDTO.groovy` (novo)
  - DTOs anônimos e de match em `Backend/src/main/groovy/com/zg/linketinder/dto/`
    (novos, conforme contratos usados pelos controllers)
  - `Backend/src/main/groovy/com/zg/linketinder/vo/CpfVO.groovy` e
    `Backend/src/main/groovy/com/zg/linketinder/vo/EmailVO.groovy` (reutilizar;
    alterar somente se necessário)
- **Teste:** `cd Backend && ./gradlew test --tests '*Recrutador*Spec'`; incluir
  testes de construção/validação e serialização dos DTOs sem senha.
- **Checklist de anonimato:**
  - [x] DTO anônimo do candidato contém competências, formação (se disponível),
    descrição e ID numérico autorizado para as operações do MVP.
  - [x] DTO de contato pós-match contém nome/e-mail corporativo do autor e
    informações da empresa, excluindo CPF, data de nascimento e senha.

### BE-2 — Implementar SQL e DAOs de recrutadores e vagas por área

- [x] Implementar persistência de cadastro, listagem e desativação lógica de
  recrutadores, leitura de vagas limitadas à empresa e área, histórico de
  avaliações e match com o autor. Novas avaliações devem ser insert-only:
  conflito na chave empresa/candidato/vaga não pode atualizar `curtiu` nem
  `id_recrutador`. Leitura de matches legados sem autor não deve fabricar
  contato.
- **Arquivos afetados:**
  - `Backend/src/main/groovy/com/zg/linketinder/dao/interfaces/RecrutadorDAO.groovy`
    (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/dao/RecrutadorDAOImpl.groovy`
    (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/dao/interfaces/CurtidaDAO.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/dao/CurtidaDAOImpl.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/dao/VagaDAOImpl.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/sql/RecrutadorSql.groovy` (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/sql/CurtidaSql.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/sql/VagaSql.groovy`
  - `Backend/src/test/groovy/com/zg/linketinder/dao/RecrutadorDAOImplSpec.groovy`
    (novo)
  - `Backend/src/test/groovy/com/zg/linketinder/dao/DaoIntegrationSpec.groovy`
    e/ou `Backend/src/test/groovy/com/zg/linketinder/dao/RecrutadorDaoIntegrationSpec.groovy`
    (novo)
- **Teste:** `cd Backend && ./gradlew test --tests '*Recrutador*DAO*Spec'` e
  `cd Backend && ./gradlew test --tests '*Recrutador*IntegrationSpec'`, após
  aplicar migration no `linketinder_test` preparado com o DDL base.
- **Checklist de anonimato:**
  - [x] Consultas pré-match selecionam apenas campos anônimos permitidos e não
    identificadores estáveis.
  - [x] Consulta de match restringe candidato, empresa e vaga participantes e
    não retorna CPF, data de nascimento ou senha.
  - [x] Cenários legados sem `id_recrutador` permanecem sem contato atribuído.

### BE-3 — Implementar regras de negócio nos Services

- [x] Criar `RecrutadorService` para cadastro pela empresa e desativação lógica.
  Atualizar Services de vaga/curtida para validar recrutador ativo, vínculo à
  empresa dona da vaga e igualdade de área antes de listar/avaliar. Resolver
  concorrência pelo primeiro registro sem sobrescrever ação ou autoria; manter
  leituras de histórico por outros recrutadores ativos da mesma empresa e área.
- **Arquivos afetados:**
  - `Backend/src/main/groovy/com/zg/linketinder/service/RecrutadorService.groovy`
    (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/service/VagaService.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/service/CurtidaService.groovy`
  - `Backend/src/test/groovy/com/zg/linketinder/service/RecrutadorServiceSpec.groovy`
    (novo)
  - `Backend/src/test/groovy/com/zg/linketinder/service/VagaServiceSpec.groovy`
    (novo ou existente)
  - `Backend/src/test/groovy/com/zg/linketinder/service/CurtidaServiceSpec.groovy`
    (novo ou existente)
- **Teste:** `cd Backend && ./gradlew test --tests '*RecrutadorServiceSpec' --tests '*VagaServiceSpec' --tests '*CurtidaServiceSpec'`.
- **Checklist de anonimato:**
  - [x] Rejeitar leitura/avaliação fora da empresa/área ou por recrutador
    inativo sem revelar dados do candidato.
  - [x] Somente match mútuo para a mesma vaga libera contato, e somente às
    partes envolvidas.
  - [x] CPF, data de nascimento e senha não são incluídos nos retornos/logs dos
    Services.

### BE-4 — Expor operações HTTP e autorização no backend

- [x] Adicionar endpoints para a empresa cadastrar/listar/desativar recrutadores
  e para recrutadores elegíveis listar vagas/candidatos anônimos, consultar
  histórico e registrar avaliação. Atualizar consulta de match para devolver
  contato do autor somente ao candidato participante. Reutilizar DTOs explícitos
  e verificar autorização no backend; não conceder CRUD de vaga ao recrutador.
  Antes de vincular a operação à identidade do usuário, verificar o mecanismo
  de autenticação existente; se não houver identidade confiável na requisição,
  bloquear exposição/ação e registrar a decisão de autenticação necessária em
  vez de confiar em IDs recebidos sem autorização.
- **Arquivos afetados:**
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/RecrutadorController.groovy`
    (novo)
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/CurtidaController.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/VagaController.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/EmpresaController.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/CandidatoController.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/controller/ServeletController/CompetenciaController.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/api/ApiServer.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/api/ServletIdentity.groovy`
  - `Backend/src/main/groovy/com/zg/linketinder/api/ServletIdentity.groovy`
  - testes dos controllers em
    `Backend/src/test/groovy/com/zg/linketinder/controller/`
- **Teste:** `cd Backend && ./gradlew test --tests '*ControllerSpec' --tests '*LegacyApiPrivacySpec'`.
- **Checklist de anonimato:**
  - [x] Testar serialização de listagem, busca, avaliações, afinidade e match:
    nada pessoal antes do match; IDs numéricos são trafegados explicitamente.
  - [x] Confirmar que após match apenas candidato participante recebe nome e
    e-mail corporativo do autor e informações da empresa; CPF, data de
    nascimento e senha nunca são enviados.
  - [x] IDs de vaga/candidato não substituem a identidade e autorização do
    backend.

### BE-5 — Cobrir integração e regressões do backend

- [x] Completar a suíte de integração para cadastro, unicidade, filtro por área,
  pertencimento à empresa, primeira avaliação imutável, likes em vagas
  distintas, soft delete, histórico e matches legados/novos. Corrigir apenas
  incompatibilidades causadas pela migration e garantir que os fluxos antigos
  de candidato/empresa continuem válidos.
- **Arquivos afetados:**
  - `Backend/src/test/groovy/com/zg/linketinder/dao/`
  - `Backend/src/test/groovy/com/zg/linketinder/service/`
  - `Backend/src/test/groovy/com/zg/linketinder/controller/`
  - `Backend/src/test/groovy/com/zg/linketinder/dao/RecrutadorDaoIntegrationSpec.groovy`
- **Teste executado:** `cd Backend && ./gradlew test --tests '*ControllerSpec' --tests '*ServiceSpec' --tests '*RecrutadorDaoIntegrationSpec' --tests '*LegacyApiPrivacySpec'`.
- **Isolamento:** a integração usa schema aleatório temporário em
  `linketinder_test` e o remove ao terminar. A suíte completa não foi executada,
  pois os testes de DAO existentes limpam tabelas do schema público
  (`DaoIntegrationSpec.cleanupSpec()`); não se deve executá-los contra dados
  compartilhados.
- **Checklist de anonimato:**
  - [x] Testes automatizados confirmam perfil anônimo pré-match em todos os
    caminhos de leitura afetados.
  - [x] Testes confirmam revelação pós-match restrita aos participantes e
    ocultação permanente de CPF, data de nascimento e senha.

**Decisão de autenticação:** endpoints sensíveis exigem um `Principal` cujo
nome seja o e-mail cadastrado; sem ele, respondem `401` e não usam IDs
recebidos para autorizar. IDs numéricos substituem tokens opacos nas APIs de
listagem e avaliação, mas não substituem a validação de identidade e
autorização. A consulta pública de vagas retorna DTOs sem identidade da empresa.

## 3. Frontend

### FE-1 — Adicionar modelos e serviços TypeScript

- [ ] Criar tipos e chamadas HTTP para perfil de recrutador, criação/
  desativação pela empresa, vagas com área, triagem anônima, histórico,
  avaliações e contato pós-match. Alinhar contratos aos DTOs validados no
  backend antes de implementar telas.
- **Arquivos afetados:**
  - `Frontend/src/ts/models/Recrutador.ts` (novo)
  - `Frontend/src/ts/models/Vaga.ts`
  - `Frontend/src/ts/models/Curtida.ts`
  - `Frontend/src/ts/services/RecrutadorService.ts` (novo)
  - `Frontend/src/ts/services/VagaService.ts`
  - `Frontend/src/ts/services/CurtidaService.ts`
- **Teste:** `cd Frontend && npm run build`; validar chamadas contra API local
  funcional.
- **Checklist de anonimato:**
  - [ ] Tipos/serviços de triagem não modelam nem armazenam dados pessoais do
    candidato pré-match.
  - [ ] Tipo de contato pós-match não inclui CPF, data de nascimento ou senha;
    nenhuma chamada de API expõe senha.

### FE-2 — Adicionar gestão de recrutadores no painel da empresa

- [ ] Incluir formulário de cadastro e listagem de recrutadores da empresa,
  com ação de desativação lógica. A interface da empresa mantém suas funções
  de Gestor; não substituir exclusão lógica por remoção física.
- **Arquivos afetados:**
  - `Frontend/src/pages/empresaDash.ts`
  - `Frontend/src/components/empresa.ts`
  - `Frontend/src/components/shared.ts` (somente helpers reutilizáveis
    necessários)
  - `Frontend/src/styles/empresaDash.css`
  - `Frontend/src/ts/services/RecrutadorService.ts`
- **Teste:** `cd Frontend && npm run build`; validação manual no Vite com
  cadastro, validação de campos obrigatórios, e-mail/CPF duplicados e
  desativação.
- **Checklist de anonimato:**
  - [ ] CPF, data de nascimento e senha do recrutador não aparecem em listas,
    painéis ou respostas renderizadas.
  - [ ] O painel não exibe dados pessoais de candidatos sem match.

### FE-3 — Criar fluxo e tela de triagem do recrutador

- [ ] Criar entrada/fluxo de acesso para recrutador conforme mecanismo de
  identidade autenticada validado no backend; mostrar apenas vagas da empresa
  e área autorizadas, cards de candidato anônimos, ações curtir/passar e
  histórico imutável incluindo autor desativado. Não oferecer criação,
  edição ou exclusão de vagas.
- **Arquivos afetados:**
  - `Frontend/src/pages/recrutadorDash.ts` (novo)
  - `Frontend/src/components/recrutador.ts` (novo)
  - `Frontend/src/styles/recrutadorDash.css` (novo)
  - `Frontend/src/main.ts`
  - `Frontend/src/pages/signIn.ts`
  - `Frontend/src/ts/models/Recrutador.ts`
  - `Frontend/src/ts/services/RecrutadorService.ts`
  - `Frontend/src/ts/services/CurtidaService.ts`
- **Teste:** `cd Frontend && npm run build`; teste manual com recrutadores de
  áreas/empresas distintas, conta desativada e avaliações já existentes.
- **Checklist de anonimato:**
  - [ ] Cards e detalhes do candidato mostram somente competências, formação
    (se disponível) e descrição; identidade não fica em DOM, atributos ou
    metadados ocultos.
  - [ ] A vaga continua anônima para candidato e UI não mostra dados pessoais
    do recrutador antes do match.
  - [ ] Recrutador inativo não acessa a triagem nem registra nova avaliação.

### FE-4 — Integrar experiência de match e validar ponta a ponta

- [ ] Integrar a visualização de matches para o candidato apresentar o contato
  do recrutador autor e dados da empresa somente quando a API confirmar o match
  mútuo da mesma vaga. Executar regressão visual e verificar o fluxo completo
  junto ao backend.
- **Arquivos afetados:**
  - `Frontend/src/pages/candidatoDash.ts`
  - `Frontend/src/components/candidato.ts`
  - `Frontend/src/components/shared.ts`
  - `Frontend/src/styles/candidatoDash.css`
  - `Frontend/src/ts/models/Curtida.ts`
  - `Frontend/src/ts/services/CurtidaService.ts`
  - `Frontend/src/main.ts`
- **Teste:** `cd Frontend && npm run build`; teste manual com API e PostgreSQL
  locais: sem match, curtida unilateral, match de mesma vaga, match de outra
  vaga, autor desativado e match legado sem autor. Executar também
  `cd Backend && ./gradlew test`.
- **Checklist de anonimato:**
  - [ ] Antes do match, nem candidato nem recrutador recebe identidade da outra
    parte ou identificadores ocultos no DOM.
  - [ ] Depois do match, somente o candidato daquele match vê nome/e-mail do
    recrutador responsável e informações da empresa.
  - [ ] CPF, data de nascimento e senha nunca são exibidos antes ou depois do
    match; match legado sem autor não mostra contato inventado.

## Checklist de aceite da spec

- [ ] Cadastro de recrutador somente pela empresa; unicidade e vínculo 1:N
  validados.
- [ ] Vagas filtradas por empresa e área; recrutador sem permissões de CRUD de
  vagas.
- [ ] Primeira decisão por candidato/vaga preservada, autoria registrada,
  avaliações de vagas distintas independentes e histórico sem perda após
  soft delete.
- [ ] Match calculado por likes positivos das duas partes para a mesma vaga e
  contato do autor correto liberado ao candidato participante.
- [ ] **Princípio do Anonimato:** candidato anônimo para recrutador antes do
  match; identidade da empresa/recrutador anônima para candidato pré-match;
  dados pós-match limitados aos participantes; CPF, data de nascimento e senha
  do recrutador nunca expostos.
- [ ] Senha nunca retornada em JSON, HTML ou logs acessíveis ao usuário; risco
  de armazenamento em texto puro permanece documentado para revisão antes de
  produção.
- [ ] Migration incremental e cópias sincronizadas; scripts base intactos;
  backend e frontend validados com seus comandos documentados.
