# Tech Challenge: API de Revenda de Veículos

Este projeto implementa uma API RESTful para uma plataforma de revenda de veículos, desenvolvida como parte do Tech Challenge da Pós-Graduação em Arquitetura de Software. A solução é baseada em uma arquitetura de microserviços, garantindo a separação de responsabilidades e escalabilidade.

## Arquitetura da Solução

A aplicação é dividida em dois microserviços independentes:

1.  **Serviço de Autenticação (`auth-service`)**: Responsável pelo cadastro e login de usuários. Ele opera de forma isolada para garantir que os dados sensíveis dos clientes estejam separados das transações comerciais, emitindo tokens JWT para autorização.
2.  **API de Veículos (`vehicle-api`)**: Responsável por toda a lógica de negócio relacionada aos veículos, incluindo cadastro, edição, listagem e o processo de compra. Este serviço valida os tokens JWT fornecidos pelo `auth-service` para proteger seus endpoints.

Ambos os serviços são containerizados com Docker e orquestrados via Docker Compose, utilizando um banco de dados PostgreSQL compartilhado, mas com schemas separados para manter o isolamento.

## Tecnologias Utilizadas

-   **Linguagem**: Java 21
-   **Framework**: Spring Boot 3
-   **Segurança**: Spring Security 6 (Autenticação baseada em JWT)
-   **Banco de Dados**: PostgreSQL
-   **Gerenciador de Dependências**: Maven
-   **Containerização**: Docker e Docker Compose

## Pré-requisitos

-   Docker e Docker Compose
-   Um cliente de API (Postman, Insomnia, etc.) para testes.

## Como Executar a Aplicação Localmente

1.  **Clone o Repositório**:
    ```bash
    git clone <url-do-seu-repositorio>
    cd <nome-do-repositorio>
    ```

2.  **Construa e Inicie os Contêineres**:
    Na pasta raiz do projeto, execute o seguinte comando. Ele irá construir as imagens Docker, baixar o PostgreSQL e iniciar todos os serviços.
    ```bash
    docker-compose up --build
    ```

3.  **Verifique a Execução**:
    Após a inicialização, os serviços estarão disponíveis nos seguintes endereços:
    -   **API de Veículos**: `http://localhost:8080`
    -   **Serviço de Autenticação**: `http://localhost:8081`

## Documentação da API

A seguir, a documentação detalhada de todos os endpoints disponíveis.

---

### 1. Serviço de Autenticação (`auth-service`)

**URL Base**: `http://localhost:8081`

Este serviço gerencia a identidade dos usuários.

#### **`POST /auth/register`**

Registra um novo usuário no sistema.

-   **Descrição**: Cria uma nova conta de usuário. O e-mail deve ser único.
-   **Corpo da Requisição** (`application/json`):
    ```json
    {
      "name": "Nome do Usuário",
      "email": "usuario@exemplo.com",
      "password": "senhaSegura123"
    }
    ```
-   **Resposta de Sucesso (200 OK)**:
    ```json
    "User registered successfully!"
    ```
-   **Respostas de Erro**:
    -   `400 Bad Request`: Se o e-mail já estiver em uso ou se os dados forem inválidos.
        ```json
        "Error: Email is already in use!"
        ```

---

#### **`POST /auth/login`**

Autentica um usuário e retorna um token de acesso JWT.

-   **Descrição**: Valida as credenciais do usuário e, se corretas, gera um token JWT com validade de 1 hora.
-   **Corpo da Requisição** (`application/json`):
    ```json
    {
      "email": "usuario@exemplo.com",
      "password": "senhaSegura123"
    }
    ```
-   **Resposta de Sucesso (200 OK)**:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvQGV4ZW1wbG8uY29tIiwiaWF0IjoxNzI4NzQyMzcwLCJleHAiOjE3Mjg3NDU5NzB9.abcdefg..."
    }
    ```
-   **Respostas de Erro**:
    -   `401 Unauthorized`: Se as credenciais (e-mail ou senha) estiverem incorretas.

---

### 2. API de Veículos (`vehicle-api`)

**URL Base**: `http://localhost:8080`

Este serviço gerencia o inventário de veículos. Para endpoints protegidos, é necessário enviar o token JWT no cabeçalho `Authorization`.

**Formato do Cabeçalho de Autenticação**:
`Authorization: Bearer <seu_token_jwt>`

---

#### **`GET /vehicles/forsale`**

Lista todos os veículos disponíveis para venda.

-   **Descrição**: Retorna uma lista de todos os veículos com status `DISPONIVEL`, ordenada do mais barato para o mais caro.
-   **Autenticação**: Não requerida.
-   **Resposta de Sucesso (200 OK)**:
    ```json
    [
      {
        "id": 2,
        "marca": "VW",
        "modelo": "Gol",
        "ano": 2018,
        "cor": "Branco",
        "preco": 35000.0,
        "status": "DISPONIVEL",
        "compradorId": null
      },
      {
        "id": 1,
        "marca": "Fiat",
        "modelo": "Uno",
        "ano": 2010,
        "cor": "Prata",
        "preco": 15000.0,
        "status": "DISPONIVEL",
        "compradorId": null
      }
    ]
    ```

---

#### **`GET /vehicles/sold`**

Lista todos os veículos que já foram vendidos.

-   **Descrição**: Retorna uma lista de todos os veículos com status `VENDIDO`, ordenada do mais barato para o mais caro.
-   **Autenticação**: Não requerida.
-   **Resposta de Sucesso (200 OK)**:
    ```json
    [
      {
        "id": 3,
        "marca": "Chevrolet",
        "modelo": "Onix",
        "ano": 2020,
        "cor": "Vermelho",
        "preco": 50000.0,
        "status": "VENDIDO",
        "compradorId": "comprador@exemplo.com"
      }
    ]
    ```

---

#### **`POST /vehicles`**

Cadastra um novo veículo no sistema.

-   **Descrição**: Adiciona um novo veículo ao inventário. O status inicial será sempre `DISPONIVEL`.
-   **Autenticação**: **Requerida**.
-   **Corpo da Requisição** (`application/json`):
    ```json
    {
      "marca": "Ford",
      "modelo": "Ka",
      "ano": 2019,
      "cor": "Preto",
      "preco": 42000.00
    }
    ```
-   **Resposta de Sucesso (201 Created)**:
    ```json
    {
      "id": 4,
      "marca": "Ford",
      "modelo": "Ka",
      "ano": 2019,
      "cor": "Preto",
      "preco": 42000.0,
      "status": "DISPONIVEL",
      "compradorId": null
    }
    ```

---

#### **`PUT /vehicles/{id}`**

Atualiza os dados de um veículo existente.

-   **Descrição**: Permite editar as informações de um veículo específico.
-   **Autenticação**: **Requerida**.
-   **Parâmetros de URL**:
    -   `id` (Long): O ID do veículo a ser atualizado.
-   **Corpo da Requisição** (`application/json`):
    ```json
    {
      "marca": "Ford",
      "modelo": "Ka",
      "ano": 2019,
      "cor": "Preto Fosco",
      "preco": 41500.00
    }
    ```
-   **Resposta de Sucesso (200 OK)**: Retorna o objeto do veículo com os dados atualizados.
-   **Respostas de Erro**:
    -   `404 Not Found`: Se o veículo com o ID fornecido não for encontrado.

---

#### **`POST /vehicles/{id}/buy`**

Realiza a compra de um veículo.

-   **Descrição**: Altera o status de um veículo para `VENDIDO` e associa o e-mail do comprador (extraído do token JWT) ao veículo.
-   **Autenticação**: **Requerida**.
-   **Parâmetros de URL**:
    -   `id` (Long): O ID do veículo a ser comprado.
-   **Resposta de Sucesso (200 OK)**:
    ```json
    {
      "id": 4,
      "marca": "Ford",
      "modelo": "Ka",
      "ano": 2019,
      "cor": "Preto Fosco",
      "preco": 41500.0,
      "status": "VENDIDO",
      "compradorId": "usuario.logado@exemplo.com"
    }
    ```
-   **Respostas de Erro**:
    -   `404 Not Found`: Se o veículo com o ID fornecido não for encontrado.
    -   `400 Bad Request`: Se o veículo já tiver sido vendido.
        ```json
        "Vehicle is already sold."
        ```