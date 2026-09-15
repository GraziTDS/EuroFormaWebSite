# euroForma Web

Versão **Web** do Projeto Educandos do Instituto Eurofarma. 
## Stack

- **Backend**: Java 21 + Spring Boot 4 (Web, Security, Data JPA, Validation, Mail, Flyway) + Maven
- **Frontend**: Angular 22 (standalone, signals) + Angular Material
- **Banco de dados**: PostgreSQL 16
- **Autenticação**: JWT (login real, sem perfil "mockado" como nos apps mobile)
- **E-mail (dev)**: Mailhog (captura os e-mails de redefinição de senha/boas-vindas localmente)

## Domínio

Três perfis de acesso — **Educando**, **Educador/Coordenador** e **Administrador** — cobrindo:
login real com recuperação de senha por e-mail, painel e boletim do educando, inscrição em eventos,
cadastro e gestão de educandos (status/frequência com auditoria), upload real de currículo (PDF),
gestão de usuários (educadores) e relatórios institucionais (matrículas por curso, frequência,
conclusão & evasão, motivos de desistência).

## Como rodar localmente

### Pré-requisitos

- JDK 21+
- Node.js 20+ e npm
- Docker (para Postgres e Mailhog) — ou uma instância própria de PostgreSQL 16

### 1. Suba o banco e o Mailhog

```bash
docker compose up -d
```

Isso sobe:
- **Postgres** em `localhost:5432` (db/user/senha: `euroforma`/`euroforma`/`euroforma`)
- **Mailhog** — SMTP em `localhost:1025`, UI web em [http://localhost:8025](http://localhost:8025)

> Se preferir não usar Docker, configure um Postgres 16 local e ajuste as variáveis de ambiente
> `DB_HOST`/`DB_PORT`/`DB_NAME`/`DB_USER`/`DB_PASSWORD` (veja `backend/src/main/resources/application.yml`).

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

O Flyway roda as migrations automaticamente na primeira subida (`V1__schema.sql` cria o schema,
`V2__seed.sql` popula com os mesmos 7 educandos, 5 educadores/coordenadores, 1 administrador, 4 cursos
e 4 eventos que já existiam nos apps mobile — só que agora com login de verdade).

API disponível em `http://localhost:8080/api`.

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Disponível em `http://localhost:4200` (o `ng serve` já usa `proxy.conf.json` para encaminhar `/api`
para o backend em `localhost:8080`, sem precisar configurar CORS manualmente em dev).

## Usuários de desenvolvimento (seed)

Senha de **todos** os usuários abaixo: `euroforma123`

| Perfil | E-mail | Observação |
|---|---|---|
| Administrador | ricardo.mendonca@institutoeurofarma.org.br | |
| Coordenador (Educador) | mariana.alves@institutoeurofarma.org.br | 4 turmas |
| Educador | rafael.tavares@institutoeurofarma.org.br | |
| Educador | juliana.pires@institutoeurofarma.org.br | |
| Educador (inativo) | sergio.lopes@institutoeurofarma.org.br | conta desativada — login deve falhar |
| Educador | beatriz.cardoso@institutoeurofarma.org.br | |
| Educando | ana.silva@email.com | Em curso, com boletim completo |
| Educando | bruno.costa@email.com | Concluído |
| Educando | carla.mendes@email.com | Recém-inscrita, sem boletim |
| Educando | diego.santos@email.com | Em curso |
| Educando | eduarda.lima@email.com | Em curso |
| Educando | felipe.souza@email.com | Concluído |
| Educando | gabriela.rocha@email.com | Desistente (com motivo registrado) |

Para testar a recuperação de senha ou o convite de senha de um educando recém-cadastrado, abra
[http://localhost:8025](http://localhost:8025) (Mailhog) — o e-mail com o link cai lá.

## Estrutura do repositório

```
EuroFormaWebSite/
├── backend/            # API Java (Spring Boot)
│   └── src/main/resources/db/migration/   # Migrations Flyway (schema + seed)
├── frontend/           # SPA Angular
└── docker-compose.yml  # Postgres + Mailhog para desenvolvimento
```

## Testes

- Backend: `cd backend && ./mvnw test`
- Frontend: `cd frontend && npm test`

## Nota sobre o ambiente desta sessão

O Docker Desktop desta máquina não conseguiu inicializar o motor Linux durante o desenvolvimento
(não há WSL instalado e o backend Hyper-V retornou erro 500 mesmo após reiniciar o Docker Desktop),
então a verificação de ponta a ponta com Postgres real (migrations + login + telas) ainda não pôde
ser executada neste ambiente. O schema/migrations e o código foram escritos e revisados com cuidado,
mas vale rodar `docker compose up -d` e subir o backend assim que o Docker estiver saudável para
confirmar que tudo sobe sem erros de migração/mapeamento antes de considerar o MVP validado de ponta
a ponta.
