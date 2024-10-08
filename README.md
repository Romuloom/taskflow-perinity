
# Taskflow-Perinity

## Descrição

Taskflow-Perinity é uma API RESTful desenvolvida em Java usando o framework Spring Boot. A aplicação permite o gerenciamento de tarefas e pessoas, permitindo operações CRUD em ambos os recursos, além de funcionalidades adicionais como alocação de pessoas a tarefas, cálculo de horas trabalhadas e listagem de departamentos.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.3**
- **Spring Data JPA**
- **PostgreSQL**
- **H2 Database** (para testes)
- **Lombok**
- **JUnit 5**
- **Mockito**

## Configuração do Projeto

### Dependências

O projeto utiliza as seguintes dependências principais:

- `spring-boot-starter-data-jpa`: Para integração com o JPA/Hibernate.
- `spring-boot-starter-web`: Para criar a API REST.
- `spring-boot-devtools`: Ferramentas de desenvolvimento para o Spring Boot.
- `postgresql`: Driver do PostgreSQL.
- `lombok`: Para redução de boilerplate de código.
- `spring-boot-starter-test`: Inclui JUnit, Mockito, etc., para testes.
- `mockito-core`: Para criar mocks em testes unitários.
- `h2`: Banco de dados em memória utilizado nos testes.

### Configuração do Banco de Dados

O projeto está configurado para utilizar o PostgreSQL em ambiente de produção e o H2 em memória para testes.

#### **PostgreSQL**

No arquivo `application.properties`, a configuração do PostgreSQL está definida como:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/taskflowdb}
spring.datasource.username=${DATABASE_USERNAME:your_username}
spring.datasource.password=${DATABASE_PASSWORD:your_password}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Certifique-se de substituir `your_username` e `your_password` pelos valores corretos do seu banco de dados PostgreSQL.

#### **H2 Database (Para Testes)**

Para usar o H2 durante os testes, descomente as linhas correspondentes no `application.properties`:

```properties
# Configuração H2 para testes (descomente para usar)
# spring.h2.console.enabled=true
# spring.datasource.driverClassName=org.h2.Driver
# spring.datasource.url=jdbc:h2:mem:taskflowdb
# spring.datasource.username=sa
# spring.datasource.password=password
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Como Rodar a Aplicação

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/seu-usuario/taskflow-perinity.git
   cd taskflow-perinity
   ```

2. **Configure o banco de dados:**

   Certifique-se de que o PostgreSQL esteja rodando e configurado corretamente.

3. **Compile e rode a aplicação:**

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. **A aplicação estará disponível em:**

   ```
   http://localhost:8080
   ```

### Como Rodar os Testes

Os testes estão organizados em diferentes pacotes, conforme mostrado na estrutura do projeto.

- Para rodar todos os testes, utilize o comando:

  ```bash
  ./mvnw test
  ```

Os testes estão localizados no diretório `src/test/java/com/perinity/taskflow_perinity/` e incluem:

- **Controller Tests**: Testes para os controladores REST (`PessoaController`, `TarefaController`).
- **Model Tests**: Testes para as entidades (`Pessoa`, `Tarefa`).
- **Repository Tests**: Testes para os repositórios (`PessoaRepository`, `TarefaRepository`).
- **Service Tests**: Testes para os serviços (`PessoaService`, `TarefaService`).

### Estrutura do Projeto

```plaintext
src/
│
├── main/
│   ├── java/com/perinity/taskflow_perinity/
│   │   ├── controller/
│   │   │   ├── PessoaController.java
│   │   │   └── TarefaController.java
│   │   ├── model/
│   │   │   ├── Pessoa.java
│   │   │   └── Tarefa.java
│   │   ├── repository/
│   │   │   ├── PessoaRepository.java
│   │   │   └── TarefaRepository.java
│   │   ├── service/
│   │   │   ├── PessoaService.java
│   │   │   ├── TarefaService.java
│   │   │   └── TaskflowPerinityApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/perinity/taskflow_perinity/
    │   ├── controller/
    │   │   ├── PessoaControllerTest.java
    │   │   └── TarefaControllerTest.java
    │   ├── model/
    │   │   ├── PessoaTest.java
    │   │   └── TarefaTest.java
    │   ├── repository/
    │   │   ├── PessoaRepositoryTest.java
    │   │   └── TarefaRepositoryTest.java
    │   └── service/
    │       ├── PessoaServiceTest.java
    │       └── TarefaServiceTest.java
    └── resources/
        └── application-test.properties
```

### Considerações Finais

Este projeto foi desenvolvido para facilitar o gerenciamento de tarefas e equipes. A arquitetura é modular, utilizando as melhores práticas de desenvolvimento com o Spring Boot, o que torna a aplicação escalável e fácil de manter.
