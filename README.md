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
assistente de IA para ajudar o educando a melhorar o currículo, cadastro e gestão de educandos
(status/frequência com auditoria — exclusivo do Administrador), importação/exportação em Excel de
educandos e relatórios, upload real de currículo (PDF), gestão de usuários (educadores), chamada de
presença por turma (exclusiva do Educador/Coordenador) e relatórios institucionais com gráficos
(matrículas por curso, frequência, conclusão & evasão, motivos de desistência).

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

## Assistente de currículo (IA)

O educando tem, na tela de perfil, um chat com IA para receber sugestões de como melhorar o currículo. Ainda não está em funcionando, mas é visto como uma implementação futuramente. 


## Estrutura do repositório

```
EuroFormaWebSite/
├── backend/            # API Java (Spring Boot)
│   └── src/main/resources/db/migration/   # Migrations Flyway (schema + seed)
├── frontend/           # SPA Angular
├── Dockerfile          # Build único: compila o Angular e embute no jar do Spring Boot
├── render.yaml         # Blueprint de deploy no Render (1 web service + 1 Postgres)
└── docker-compose.yml  # Postgres + Mailhog para desenvolvimento
```

## Testes

- Backend: `cd backend && ./mvnw test`
- Frontend: `cd frontend && npm test`

## Deploy público (Render) — para gerar um link/QR code de teste

Em produção, o backend Java **serve o próprio frontend Angular já compilado** (um único serviço, um
único domínio) — não precisa hospedar frontend e backend separados nem lidar com CORS entre eles.

1. Confirme que o repositório está atualizado no GitHub (branch `main`).
2. Crie uma conta em [render.com](https://render.com) (dá pra entrar direto com o GitHub).
3. No dashboard, clique em **New +** → **Blueprint**, selecione o repositório
   `GraziTDS/EuroFormaWebSite`. O Render vai ler o `render.yaml` da raiz e propor automaticamente:
   - um banco **Postgres** gratuito (`euroforma-db`);
   - um **Web Service** gratuito rodando o `Dockerfile` (`euroforma-projeto-educandos`).
4. Revise os nomes (o nome do serviço vira parte da URL pública, ex.:
   `https://euroforma-projeto-educandos.onrender.com` — se esse nome já estiver em uso por outra
   pessoa no Render, você pode alterá-lo na tela de revisão antes de aplicar) e clique em **Apply**.
5. Aguarde o primeiro deploy (compila o Angular + o Spring Boot dentro do Docker — leva alguns
   minutos na primeira vez). As migrations do Flyway rodam sozinhas na primeira subida, já com os
   usuários de teste da tabela acima.
6. Pronto — a URL pública do serviço é o link para o QR code. Teste logando com qualquer usuário da
   tabela de seed (senha `euroforma123`).

**Limitações do plano gratuito do Render (bom para demo/teste, não para produção real):**
- O serviço "dorme" depois de um tempo sem uso — a primeira requisição depois disso demora uns
  30-60s para "acordar" (as seguintes ficam rápidas normalmente).
- Upload de currículo fica em disco *dentro do container* — some a cada novo deploy/restart.
- Envio de e-mail (recuperação de senha, convite de novo educando) não funciona out-of-the-box,
  pois não há servidor SMTP configurado — configure `MAIL_HOST`/`MAIL_PORT`/`MAIL_USERNAME`/
  `MAIL_PASSWORD`/`MAIL_SMTP_AUTH=true`/`MAIL_SMTP_STARTTLS=true` nas variáveis de ambiente do
  serviço no dashboard do Render (ex.: usando uma conta Gmail com senha de app, ou um serviço como
  Brevo/Resend) se quiser esse fluxo funcionando de verdade no link público.
- Se quiser o assistente de IA do currículo ativo no link público, adicione a variável de ambiente
  `ANTHROPIC_API_KEY` no dashboard do serviço no Render (veja a seção acima).
