<div align="center">

# SupportFlow

### Sistema web de gerenciamento de chamados

Cadastro de usuários, autenticação e gerenciamento de chamados em uma aplicação web integrada a uma API REST.

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-API-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Banco_de_dados-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Autenticação-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrações-CC0200?style=flat-square)

</div>

---

## Visão geral do projeto

O **SupportFlow** é uma aplicação web que permite ao usuário criar uma conta, realizar login e gerenciar seus próprios chamados. O projeto aplica conceitos de desenvolvimento backend, API REST, autenticação, persistência de dados e integração entre frontend e backend.

 Funcionalidades

- Cadastro de usuários.
- Login com autenticação JWT.
- Criação de chamados com título e descrição.
- Consulta dos chamados do usuário autenticado.
- Exclusão de chamados.
- Persistência dos registros no PostgreSQL.

## Como a aplicação funciona

1. O usuário realiza o cadastro e efetua login.
2. O backend valida as credenciais e fornece o token JWT.
3. O frontend envia o token nas requisições protegidas.
4. O usuário cria, consulta ou exclui seus chamados.
5. Os dados são persistidos no PostgreSQL.

## Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem do backend |
| Spring Boot | Implementação da API REST |
| Spring Security | Segurança e controle de acesso |
| JWT | Autenticação por token |
| PostgreSQL | Banco de dados relacional |
| Flyway | Migrações versionadas do banco |
| Maven | Dependências e execução dos testes |
| HTML, CSS e JavaScript | Interface web |

## Modelo de dados

O banco possui duas tabelas principais:

- **`usuarios`**: armazena nome, e-mail, hash da senha, data de criação e perfil de acesso.
- **`chamados`**: armazena título, descrição, data de criação e o identificador do usuário responsável.

Cada chamado referencia um usuário por meio da chave estrangeira `chamados.user_id`. O esquema final está documentado em [`db.sql`](db.sql), e as migrações do Flyway estão em `supportflow/src/main/resources/db/migration/`.

## Executar localmente

### Pré-requisitos

- JDK 21.
- PostgreSQL em execução.
- Python 3.
- Navegador web.

### 1. Criar o banco

Crie no PostgreSQL um banco chamado `supportflow`. A configuração local esperada pelo projeto utiliza a porta `5433` e o usuário `postgres`; ajuste conforme seu ambiente.

### 2. Configurar as variáveis de ambiente

No PowerShell, configure as variáveis antes de iniciar o backend. Substitua o valor de exemplo pela senha local do PostgreSQL:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5433/supportflow"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "SUA_SENHA_LOCAL"
```

Não coloque a senha real no README nem a versione no Git.

### 3. Iniciar o backend

Na raiz do repositório:

```powershell
cd .\supportflow
.\mvnw.cmd spring-boot:run
```

O backend fica disponível, por padrão, em `http://localhost:8080`. Mantenha o terminal aberto.

### 4. Iniciar o frontend

Abra outro terminal na raiz do repositório:

```powershell
cd .\frontend
python -m http.server 5500
```

Acesse `http://localhost:5500` no navegador. O frontend, o backend e o PostgreSQL devem permanecer em execução.

## Endpoints principais

| Método | Endpoint | Finalidade |
|---|---|---|
| `POST` | `/users/create` | Cadastro de usuário |
| `POST` | `/auth/login` | Autenticação |
| `POST` | `/tickets` | Criar chamado |
| `GET` | `/tickets` | Consultar chamados |
| `DELETE` | `/tickets/{id}` | Excluir chamado |

As operações de chamados exigem autenticação. O frontend envia o token JWT nas requisições protegidas.

## Testes

Na raiz do repositório, execute:

```powershell
cd .\supportflow
.\mvnw.cmd test
```

Uma execução bem-sucedida termina com `BUILD SUCCESS`.

## Estrutura do repositório

```text
supportflow/
├── README.md
├── db.sql
├── docs/
│   ├── arquitetura.md
│   └── images/
│       └── supportflow-visao-geral.png
├── frontend/
│   └── js/
└── supportflow/
    ├── mvnw.cmd
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   └── resources/
        │       ├── application.yaml
        │       └── db/migration/
        └── test/
```

## Documentação complementar

- [`docs/arquitetura.md`](docs/arquitetura.md) — visão geral da arquitetura e do fluxo principal.
- [`db.sql`](db.sql) — referência do esquema final do banco de dados.
- `supportflow/src/main/resources/db/migration/` — migrações versionadas do Flyway.

---

<div align="center">

**SupportFlow — organizar e gerenciar chamados em uma aplicação web.**

Projeto acadêmico de desenvolvimento de software.

</div>
