# Gestão de Frotas - Fleet Management System

Sistema de gestão de frotas de veículos desenvolvido com **Clean Architecture** e **Domain-Driven Design (DDD)**.

## 🏗️ Arquitetura

O projeto segue os princípios de Clean Architecture com separação clara de responsabilidades:

### Camadas

```
├── domain/              # Camada de Domínio (Regras de Negócio)
│   ├── entity/         # Entidades ricas com lógica de negócio
│   ├── valueobject/    # Value Objects (LicensePlate)
│   ├── enums/          # Enumerações de domínio
│   └── repository/     # Interfaces de repositório
│
├── application/         # Camada de Aplicação (Casos de Uso)
│   ├── usecase/        # Casos de uso
│   ├── dto/            # Data Transfer Objects
│   └── mapper/         # Mappers MapStruct (DTO ↔ Domain)
│
├── infrastructure/      # Camada de Infraestrutura (Detalhes Técnicos)
│   └── persistence/
│       ├── entity/     # Entidades JPA
│       ├── mapper/     # Mappers MapStruct (Domain ↔ JPA)
│       ├── jpa/        # Spring Data JPA Repositories
│       └── repository/ # Implementações dos repositórios de domínio
│
└── presentation/        # Camada de Apresentação (API REST)
    ├── controller/     # Controllers REST
    └── exception/      # Exception Handlers
```

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.5.8**
- **Spring Data JPA** - Persistência
- **PostgreSQL** - Banco de dados
- **MapStruct 1.5.5** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate
- **Hibernate JPA Metamodel Generator** - Type-safe queries
- **SpringDoc OpenAPI** - Documentação da API
- **Docker Compose** - PostgreSQL containerizado

## 📦 Agregados Principais

### 1. Vehicle (Veículo)
- Gestão de veículos da frota
- Tipos: CAR, TRUCK, MOTORCYCLE, VAN, BUS
- Status: AVAILABLE, IN_USE, MAINTENANCE, INACTIVE
- Controle de quilometragem
- Validação de placa (formatos antigo e Mercosul)

### 2. Driver (Motorista)
- Gestão de motoristas
- Validação de CPF e CNH
- Controle de validade da CNH
- Status: ACTIVE, INACTIVE, SUSPENDED

### 3. Maintenance (Manutenção)
- Registro de manutenções preventivas e corretivas
- Workflow: SCHEDULED → IN_PROGRESS → COMPLETED/CANCELLED
- Controle de custos
- Relacionamento com veículos

### 4. Trip (Viagem)
- Registro de viagens/deslocamentos
- Relacionamento com veículo e motorista
- Cálculo automático de distância percorrida
- Controle de quilometragem inicial e final

## 🔧 Configuração

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker (para PostgreSQL)

### Banco de Dados

O projeto usa Docker Compose para subir o PostgreSQL automaticamente:

```yaml
# compose.yaml
services:
  postgres:
    image: 'postgres:latest'
    environment:
      - 'POSTGRES_DB=gestao_frotas'
      - 'POSTGRES_PASSWORD=postgres'
      - 'POSTGRES_USER=postgres'
    ports:
      - '5432:5432'
```

### Executar a Aplicação

1. **Compilar o projeto:**
```bash
mvn clean compile
```

2. **Executar:**
```bash
mvn spring-boot:run
```

O Docker Compose iniciará automaticamente o PostgreSQL na primeira execução.

## 📚 API Documentation

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🔍 Endpoints Principais

### Vehicles
- `POST /api/vehicles` - Criar veículo
- `GET /api/vehicles` - Listar todos os veículos
- `GET /api/vehicles/{id}` - Buscar veículo por ID
- `PUT /api/vehicles/{id}` - Atualizar veículo
- `DELETE /api/vehicles/{id}` - Remover veículo

### Drivers
- `POST /api/drivers` - Criar motorista
- `GET /api/drivers` - Listar todos os motoristas
- `GET /api/drivers/{id}` - Buscar motorista por ID

## 🎯 Características Técnicas

### Clean Architecture
- **Independência de frameworks**: Lógica de negócio isolada
- **Testabilidade**: Camadas desacopladas facilitam testes
- **Independência de UI**: API REST pode ser substituída
- **Independência de banco de dados**: Repositórios abstraídos

### Domain-Driven Design
- **Entidades ricas**: Lógica de negócio nas entidades de domínio
- **Value Objects**: LicensePlate com validação
- **Agregados**: Vehicle, Driver, Maintenance, Trip
- **Repository Pattern**: Interfaces no domínio, implementação na infraestrutura

### Relacionamentos JPA
- Uso de `@ManyToOne` com `@JoinColumn` para relacionamentos adequados
- Lazy loading para otimização de performance
- Queries type-safe com JPA Criteria API e Metamodel

### Mapeamento com MapStruct
- Conversão automática DTO ↔ Domain Entity
- Conversão automática Domain Entity ↔ JPA Entity
- Mappers customizados para Value Objects

## 📝 Exemplo de Uso

### Criar um Veículo

```bash
curl -X POST http://localhost:8080/api/vehicles \
  -H "Content-Type: application/json" \
  -d '{
    "licensePlate": "ABC1234",
    "type": "CAR",
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023,
    "currentMileage": 0
  }'
```

### Criar um Motorista

```bash
curl -X POST http://localhost:8080/api/drivers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cpf": "12345678901",
    "cnh": "12345678901",
    "cnhCategory": "B",
    "cnhExpirationDate": "2025-12-31"
  }'
```

## 🧪 Testes

```bash
mvn test
```

## 📄 Licença

Este projeto foi desenvolvido como exemplo de aplicação Clean Architecture/DDD com Spring Boot.
