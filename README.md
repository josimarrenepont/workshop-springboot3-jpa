
# <img src="https://github.com/user-attachments/assets/8440781c-72e6-4dda-b4c6-b952de85438e" alt="shopping" width="100" />   Sistema de Gerenciamento de Pedidos

Este projeto é um sistema completo para gerenciar pedidos, controlar estoque e visualizar relatórios de vendas.

## Funcionalidades
- Cadastro e gerenciamento de produtos, categorias e clientes.
- Controle de estoque com redução automática em cada venda.
- Geração de relatórios de vendas.
- Integração com API RESTful.
- Automação de builds e deploy com GitHub Actions.
- Containerização com Docker e orquestração com Docker Compose.
  
# Tecnologias utilizadas

## Back End

* Java
* Spring Boot
* JPA / Hibernate
* Maven

# Implantação em produção

* Banco de dados: PostgreSQL

# Esteira CI/CD
### Este projeto utiliza GitHub Actions para implementar a esteira CI/CD, garantindo um processo automatizado de integração e entrega contínua. O pipeline realiza as seguintes etapas:

  1. Build: Compila o projeto usando Maven.
  2. Testes: Executa os testes automatizados.
  3. Containerização: Constrói a imagem Docker do projeto.
  4. Deploy: Publica a imagem no Docker Hub.

# Autor

Josimar Renepont dos Santos

https://www.linkedin.com/in/josimar-renepont/
