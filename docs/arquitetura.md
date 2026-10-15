# Arquitetura do SupportFlow

## 1. Visao geral

O SupportFlow e uma aplicacao web para gerenciamento de chamados. Usuarios podem se cadastrar, autenticar e gerenciar seus proprios chamados.

## 2. Tecnologias

- Java 21
- Spring Boot
- Spring Security
- JWT para autenticacao
- PostgreSQL
- Flyway para migracoes do banco de dados
- HTML, CSS e JavaScript no frontend
- Maven para gerenciamento e testes do backend

## 3. Componentes

### Frontend

Responsavel pelas telas de cadastro, login e gerenciamento de chamados. O frontend se comunica com o backend por requisicoes HTTP.

### Backend

Implementa a API, as regras de negocio, a autenticacao e o acesso aos dados.

### Banco de dados

O PostgreSQL armazena os usuarios e seus chamados.

### Migracoes

O Flyway executa as migracoes versionadas localizadas em:

`supportflow/src/main/resources/db/migration`

## 4. Modelo de dados

### usuarios

Armazena identificador, nome, email, hash da senha, data de criacao e perfil de acesso.

### chamados

Armazena identificador, usuario responsavel, titulo, descricao e data de criacao.

Cada chamado pertence a um usuario por meio da chave estrangeira `user_id`.

## 5. Fluxo principal

1. O usuario realiza o cadastro.
2. O usuario efetua login.
3. O backend valida as credenciais e fornece o token de autenticacao.
4. O frontend envia o token nas requisicoes protegidas.
5. O usuario cria, consulta e exclui seus proprios chamados.

## 6. Seguranca

As operacoes protegidas exigem autenticacao. As credenciais devem ser armazenadas com hash, nunca em texto puro. As configuracoes sensiveis, como a senha do banco, devem ser fornecidas por variaveis de ambiente.

## 7. Execucao e testes

As instrucoes de configuracao e execucao devem ser consultadas no README da raiz do repositorio.

Para executar os testes do backend, entre na pasta `supportflow` e execute:

`.\mvnw.cmd test`

## 8. Escopo

Esta fase contempla cadastro, autenticacao e gerenciamento basico de chamados. Docker Compose e pipeline CI/CD ficam para uma fase posterior.
