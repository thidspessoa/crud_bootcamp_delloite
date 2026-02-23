# 📄 CRUD Java Project

## 1️⃣ Descrição do Projeto
Projeto de exemplo de um **CRUD (Create, Read, Update, Delete)** em Java, que tem como objetivo praticar conceitos de **POO, MVC, camadas de serviço e persistência de dados**, utilizando um banco H2 em memória.  
O projeto foi desenvolvido para fins educacionais durante o bootcamp da Deloitte, demonstrando boas práticas de arquitetura em camadas.

---

## 2️⃣ Tecnologias Utilizadas
- **Java 17** – Linguagem principal
- **Maven** – Gerenciamento de dependências
- **H2 Database** – Banco de dados em memória
- **JDBC / JPA** – Persistência de dados
- **IDE IntelliJ IDEA** – Ambiente de desenvolvimento

---

## 3️⃣ Estrutura do Projeto

├─ /bin -> Classes compiladas
├─ /src
│ ├─ /br/com/deloittebt/crud
│ │ ├─ model/ -> Entidades do domínio
│ │ ├─ repository/ -> Classes de acesso a dados
│ │ ├─ service/ -> Lógica de negócio
│ └─ Main.java -> Ponto de entrada da aplicação
├─ cruddb.mv.db -> Banco H2 persistente
└─ pom.xml -> Configuração do Maven

---

## 4️⃣ Arquitetura e Conceitos
- **Camadas do projeto**:
    - **Model (Domínio):** Classes que representam entidades do sistema (`User`).
    - **Repository:** Classes responsáveis por manipular o banco de dados.
    - **Service:** Lógica de negócio e regras da aplicação.
    - **Main:** Interface de execução/entrada do programa (console simples).

- **Princípios aplicados**:
    - **Separação de responsabilidades** (cada camada tem um papel específico)
    - **MVC simplificado** (model + service + interface de console)
    - **Persistência em banco H2** (em memória, para fins de teste e aprendizado)

---

## 5️⃣ Funcionalidades
- **Create:** Adicionar novos usuários
- **Read:** Listar usuários existentes
- **Update:** Alterar informações de usuários
- **Delete:** Remover usuários do banco

---

## 6️⃣ Como Executar
1. Clone o projeto:
git clone <link-do-repositório>

2. Compile o projeto:
mvn compile

3. Execute a aplicação:
mvn exec:java -Dexec.mainClass="br.com.deloittebt.crud.Main"

4. Siga as instruções no console para realizar operações CRUD.