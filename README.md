## Autor
Desenvolvido por Túlio

## Tecnologias Utilizadas
* **Groovy:** Linguagem principal do projeto (Lógica, Orientação a Objetos, GroovyBeans, Tipagem Dinâmica)
* **Gradle:** Ferramenta de build e automação

## Comentários
O projeto foi desenvolvido focando na entrega do Produto Mínimo Viável (MVP) solicitado, priorizando a organização estrutural, aplicação de conceitos de Orientação a Objetos e tratamento de erros de interação. Algumas decisões da implementação foram:

* **Organização do projeto:** O código foi separado em camadas (Model, Service, Util e CLI), garantindo que cada pacote tenha uma responsabilidade única e facilitando a manutenção.
* **Dados em Memória:** Para validar os dados do MVP rapidamente, os Services foram inicializados com 5 empresas e 5 candidatos pré-cadastrados, utilizando o construtor de mapas nativo do Groovy.

## Execução pela IDE
1. Abra o projeto no IntelliJ IDEA.
2. Aguarde o Gradle sincronizar o projeto.
3. Localize a classe Main.groovy dentro do pacote com.zg.linketinder.
4. Clique no botão Run ao lado do método main.
5. Siga as instruções do menu interativo no terminal.

## Execução via Gradle
1. Abra o terminal na pasta raiz do projeto.
2. Execute o comando correspondente ao seu sistema: ./gradlew run

Observação: O projeto utiliza **Groovy 5.0.8** e a ferramenta de build Gradle **9.7.0**.