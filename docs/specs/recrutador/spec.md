# Spec: Recrutador

## Contexto e objetivo

Atualmente a empresa é a única entidade que avalia candidatos para suas vagas.
Esta feature introduz o **Recrutador**, um colaborador vinculado a uma empresa,
para que a empresa possa distribuir a triagem por área de atuação sem transferir
ao recrutador a gestão das vagas.

O objetivo é permitir que a empresa cadastre e desative recrutadores; que cada
recrutador ativo analise somente vagas da sua empresa na sua área; e que suas
avaliações sejam registradas com autoria, preservadas para auditoria e usadas
no match. Após um match, o candidato recebe o contato do recrutador responsável
pela avaliação da vaga e informações da empresa.

## Modelo de dados

### Nova tabela `recrutadores`

Adicionar uma tabela relacionada a `empresas`:

| Campo | Regra |
|---|---|
| `id` | Chave primária. |
| `id_empresa` | FK obrigatória para `empresas.id`; cada recrutador pertence a uma única empresa e cada empresa pode ter vários recrutadores. |
| `nome` | Obrigatório. |
| `area` | Obrigatória; identifica a área de atuação usada para filtrar vagas. |
| `descricao` | Obrigatória; apresentação profissional do recrutador. |
| `email` | E-mail corporativo obrigatório e único; um e-mail de recrutador não pode pertencer a mais de uma empresa. |
| `cpf` | Obrigatório e único. Dado sensível, nunca divulgado a candidatos. |
| `data_nascimento` | Obrigatória. Dado sensível, nunca divulgado a candidatos. |
| `senha` | Obrigatória; persistida em texto puro conforme a decisão atual, com o risco de segurança registrado abaixo. Nunca deve ser retornada nem registrada em logs. |
| `ativo` | Booleano obrigatório, com valor inicial `TRUE`; desativação lógica usa `FALSE`. |

A remoção física de um recrutador não deve ocorrer pelo fluxo normal. A
desativação não apaga nem altera avaliações existentes, nem elimina a autoria
registrada. A referência histórica ao recrutador não pode usar exclusão em
cascata.

### Alteração em `vagas`

Adicionar `area` obrigatória à tabela `vagas`. A área da vaga deve ser
comparável à área do perfil do recrutador para determinar quais vagas ele pode
analisar. Os valores de área devem ser tratados de forma consistente em cadastro
e comparação. Uma taxonomia fechada de áreas não foi definida nesta spec.

### Alteração em `curtidas_empresa`

Adicionar `id_recrutador`, FK para `recrutadores.id`, para registrar qual
recrutador originou a avaliação da empresa para o candidato e a vaga.

- A avaliação continua vinculada à combinação empresa, candidato e vaga. A
  unicidade existente por essa combinação deve ser mantida: recrutadores
  diferentes não criam avaliações concorrentes para o mesmo candidato/vaga da
  empresa.
- A autoria deve pertencer à mesma empresa da vaga/avaliação. A elegibilidade
  da área e o vínculo com a empresa devem ser verificados no backend e
  protegidos na persistência quando possível.
- Novas avaliações devem ter recrutador responsável. Avaliações históricas sem
  autoria identificável permanecem preservadas com `id_recrutador` nulo; não
  atribuir autoria retroativamente por suposição.
- O conflito entre ações exige que a primeira avaliação seja imutável: a
  operação de gravar uma avaliação posterior não pode atualizar o valor de
  `curtiu` nem substituir `id_recrutador`.

## Regras de negócio

### Cadastro e vínculo

- Somente a empresa cadastra o recrutador e estabelece o vínculo. Não existe
  cadastro independente de recrutador nem solicitação de vínculo pelo próprio
  recrutador.
- O relacionamento é Empresa 1:N Recrutadores e Recrutador N:1 Empresa.
- Um recrutador não pode ser transferido implicitamente para outra empresa; o
  e-mail corporativo é único e o vínculo precisa permanecer inequívoco.
- A empresa mantém o nível Gestor: recrutadores não podem criar, editar ou
  excluir vagas.

### Acesso a vagas e avaliações

- Um recrutador ativo pode visualizar e analisar somente vagas pertencentes à
  sua empresa cujo `area` corresponda à `area` do seu perfil.
- As rotas autenticadas de perfil do recrutador e de vagas para triagem podem
  incluir `empresa.cnpj` da própria empresa vinculada para que o cliente
  compare os valores por igualdade estrita antes de renderizar vagas, carregar
  candidatos ou enviar avaliações. Esse campo não deve ser incluído nas
  listagens públicas de vagas; o backend continua sendo a autoridade e deve
  validar o vínculo recrutador/empresa/área em cada operação.
- “Gerenciar vagas”, neste contexto, significa triagem de candidatos e
  avaliações; não concede permissões de CRUD de vagas.
- A avaliação é por candidato **e por vaga**. O mesmo candidato pode receber
  `curtiu = FALSE` para uma vaga e `curtiu = TRUE` para outra.
- `curtiu = TRUE` expressa interesse positivo da empresa para aquela vaga;
  `curtiu = FALSE` expressa avaliação negativa e não produz match.
- O primeiro recrutador ativo autorizado que registrar uma avaliação para a
  combinação empresa/candidato/vaga determina o resultado. Avaliações
  posteriores de outros recrutadores não substituem a decisão nem a autoria.
- Recrutadores ativos da mesma empresa e área podem consultar o histórico de
  avaliações daquela área, inclusive avaliações feitas por recrutadores
  desativados. A consulta do histórico não permite alterar a decisão original.
- Apenas o recrutador responsável pela primeira avaliação fica associado ao
  match e pode ser apresentado como contato daquele match.

### Match e comunicação

O match ocorre somente quando:

1. o candidato avaliou positivamente a vaga (`curtidas_candidato.curtiu =
   TRUE`); e
2. a avaliação da empresa para o mesmo candidato e a mesma vaga é positiva
   (`curtidas_empresa.curtiu = TRUE`).

A avaliação da empresa deve ter sido registrada por um recrutador autorizado
para aquela vaga. Curtida unilateral, avaliação negativa, vaga de outra empresa
ou vaga fora da área do recrutador não produzem match. O match continua sendo
derivado das avaliações; esta feature não exige uma tabela persistente de
matches.

Após o match, o candidato recebe o nome e o e-mail corporativo do recrutador
responsável pela avaliação daquela vaga, além das informações da empresa. O
candidato é responsável por iniciar o contato com o recrutador.

Avaliações antigas sem `id_recrutador` não devem receber um recrutador inventado
para satisfazer a atribuição de contato. O comportamento de contato para matches
legados sem autoria identificável deve preservar os dados e não expor contato
de outra pessoa.

## Princípio do Anonimato

Esta regra deve ser aplicada no backend em consultas, autorização, DTOs e
serialização, e também respeitada pela interface. Ocultar dados somente na
interface não é suficiente.

### Antes do match

- O recrutador vê somente competências, formação (se disponível) e descrição
  do candidato.
- Não expor ao recrutador nome, e-mail, CPF, data de nascimento, endereço,
  contato ou outro identificador do candidato. A avaliação não pode devolver
  dados pessoais nem identificadores estáveis que permitam descobrir sua
  identidade.
- O candidato não recebe identidade ou contato do recrutador nem dados
  identificadores da empresa antes do match. A vaga deve continuar anônima
  conforme a regra geral do projeto.
- O CPF e a data de nascimento do recrutador nunca são exibidos ao candidato,
  independentemente da existência de match.
- Senha não é dado de perfil: nunca deve aparecer para candidato, empresa,
  recrutador, em resposta de API ou em log acessível ao usuário.

### Depois do match

- Somente o candidato e a empresa participantes daquele match podem acessar os
  dados liberados pelo match.
- O candidato recebe o nome e o e-mail corporativo do recrutador que originou
  a primeira avaliação positiva da empresa para aquela vaga, além das
  informações da empresa.
- CPF e data de nascimento do recrutador continuam ocultos, antes e depois do
  match. Senha também permanece sempre oculta.
- A existência de outra curtida ou match não autoriza acesso a dados de
  terceiros fora daquela relação candidato/empresa/vaga.
- A spec não autoriza divulgar ao recrutador dados pessoais do candidato que
  não estejam definidos como necessários; os dados mostrados durante a triagem
  permanecem limitados ao conjunto anônimo acima.

## Decisões tomadas

- A empresa cadastra recrutadores; não há auto cadastro. Uma empresa pode ter
  vários recrutadores, mas cada recrutador/e-mail corporativo está associado a
  uma única empresa.
- Recrutadores não criam, editam ou excluem vagas. Eles analisam vagas apenas
  na área do seu perfil e pertencentes à sua empresa.
- `area` será adicionada a `vagas` e ao perfil do recrutador para fazer essa
  filtragem.
- A curtida/dislike pertence ao par candidato/vaga e fica registrada como
  avaliação da empresa, com o recrutador autor identificado. Se houver conflito
  entre recrutadores na mesma vaga, prevalecem a primeira ação e sua autoria;
  a ação posterior não sobrescreve o histórico.
- O match depende de avaliações positivas do candidato e da empresa para a
  mesma vaga. Após o match, o candidato inicia o contato com o recrutador
  responsável e recebe o nome, o e-mail corporativo dele e informações da
  empresa.
- CPF e data de nascimento do recrutador nunca são divulgados a candidatos.
- Por decisão atual, a senha será armazenada em texto puro, sem framework
  externo. **Risco registrado:** essa opção expõe credenciais em caso de acesso
  indevido ao banco e não é recomendada para produção. A spec não transforma
  essa decisão em autorização para retornar ou registrar a senha em respostas,
  interfaces ou logs.
- Recrutadores são desativados logicamente (`ativo = FALSE`). Avaliações e
  autoria nunca são apagadas; outros recrutadores ativos da mesma empresa e
  área podem consultar o histórico.
- O modelo atual não possui formação acadêmica. A regra de anonimato continua
  aplicável caso formação seja adicionada nesta ou em outra feature.

## Critérios de aceite

### Modelo e cadastro

- **Dado** uma empresa existente, **quando** ela cadastrar um recrutador,
  **então** o recrutador deverá ser persistido vinculado a essa empresa com os
  campos obrigatórios e `ativo = TRUE`.
- **Dado** um recrutador, **quando** tentar cadastrá-lo sem empresa ou por um
  fluxo de auto cadastro, **então** a operação deverá ser rejeitada.
- **Dado** um e-mail corporativo ou CPF já cadastrado, **quando** uma empresa
  tentar reutilizá-lo, **então** o sistema deverá rejeitar a duplicidade sem
  criar um segundo vínculo.
- **Dado** uma vaga, **quando** o schema for atualizado, **então** a vaga
  deverá possuir `area` persistida e disponível para filtragem.

### Acesso e área

- **Dado** um recrutador ativo, **quando** listar vagas para triagem, **então**
  receberá apenas vagas da própria empresa com área igual à do seu perfil.
- **Dado** um recrutador inativo, **quando** tentar consultar ou avaliar vagas,
  **então** não poderá executar novas ações.
- **Dado** uma vaga de outra empresa ou de área diferente, **quando** um
  recrutador tentar acessá-la para triagem ou avaliação, **então** o backend
  deverá negar a operação sem revelar dados protegidos.
- **Dado** um recrutador, **quando** tentar criar, editar ou excluir uma vaga,
  **então** a operação não será permitida; essas ações continuam exclusivas da
  empresa no nível Gestor.

### Avaliações, autoria e match

- **Dado** um candidato que já recebeu avaliação de um recrutador para uma
  vaga, **quando** outro recrutador avaliar o mesmo candidato e vaga,
  **então** a primeira ação e sua autoria permanecerão inalteradas.
- **Dado** avaliações para duas vagas diferentes do mesmo candidato,
  **quando** uma for positiva e outra negativa, **então** cada resultado será
  mantido independentemente para sua respectiva vaga.
- **Dado** uma avaliação positiva da empresa registrada por recrutador
  autorizado, **quando** o candidato também curtir positivamente a mesma vaga,
  **então** haverá match atribuído ao recrutador da primeira avaliação.
- **Dado** uma avaliação negativa, unilateral ou relativa a outra vaga,
  **quando** o sistema verificar match, **então** não haverá match por essa
  avaliação.
- **Dado** uma avaliação legada sem recrutador responsável, **quando** ela for
  lida ou participar de um match legado, **então** seu histórico será
  preservado e nenhum recrutador será atribuído por suposição.
- **Dado** recrutadores ativos da mesma empresa e área, **quando** consultarem
  o histórico, **então** poderão ver avaliações de autores desativados sem
  alterar seus valores ou autoria.

### Princípio do Anonimato

- **Dado** um candidato sem match com a vaga, **quando** o recrutador listar,
  buscar ou abrir o perfil para triagem, **então** verá apenas competências,
  formação (se disponível) e descrição, sem nome, e-mail, CPF, data de
  nascimento, endereço, contato ou identificador que revele sua identidade.
- **Dado** uma vaga ainda sem match, **quando** o candidato visualizar a vaga,
  **então** não verá a identidade ou contato do recrutador nem a identidade da
  empresa.
- **Dado** curtidas unilaterais, **quando** uma parte consultar os dados da
  outra, **então** nenhuma identidade ou dado pessoal será liberado.
- **Dado** um match confirmado, **quando** o candidato participante acessar o
  contato, **então** verá o nome e e-mail corporativo do recrutador responsável
  e as informações da empresa, mas nunca o CPF ou a data de nascimento do
  recrutador.
- **Dado** qualquer resposta, log ou representação visual da feature,
  **quando** ela contiver dados de recrutador ou candidato, **então** não
  incluirá senhas e respeitará os limites de anonimato antes e depois do match.

### Desativação

- **Dado** um recrutador ativo com avaliações registradas, **quando** a empresa
  o desativar, **então** seu estado passará a `ativo = FALSE`, as avaliações
  continuarão persistidas com a autoria intacta e o recrutador não poderá
  registrar novas ações.

## Fora do escopo

- Cadastro independente, auto cadastro, convite por e-mail ou fluxo de
  aprovação de vínculo pelo recrutador.
- Permissões de criar, editar ou excluir vagas para recrutadores.
- Recrutador associado a mais de uma empresa ou transferência automática entre
  empresas.
- Definição de catálogo fechado, administração ou normalização avançada de
  áreas de atuação.
- Entidade/cadastro de formação acadêmica, não existente no modelo atual.
- Reautoria, remoção, sobrescrita ou migração especulativa de avaliações
  históricas sem recrutador identificável.
- Implementação de mecanismos criptográficos ou frameworks externos para
  armazenamento de senha nesta entrega; a decisão de texto puro fica registrada
  como risco a reavaliar antes de produção.
- Canal de mensagens dentro da aplicação. O candidato inicia contato usando
  os dados liberados após match.
