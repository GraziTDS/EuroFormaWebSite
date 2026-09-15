create table usuario (
    id          bigserial primary key,
    nome        varchar(150) not null,
    email       varchar(150) not null unique,
    senha_hash  varchar(255) not null,
    role        varchar(20)  not null,
    ativo       boolean      not null default true,
    criado_em   timestamptz  not null default now()
);

create table curso (
    id   bigserial primary key,
    nome varchar(150) not null unique
);

create table educando (
    id                               bigserial primary key,
    usuario_id                      bigint not null unique references usuario (id),
    cpf                             varchar(14) unique,
    telefone                        varchar(20),
    nascimento                      date,
    curso_id                        bigint not null references curso (id),
    progresso                       int not null default 0,
    media                           numeric(4, 2) not null default 0,
    iniciado_em                     date,
    endereco_rua                    varchar(150),
    endereco_bairro                 varchar(100),
    endereco_cep                    varchar(10),
    endereco_cidade                 varchar(100),
    endereco_uf                     varchar(2),
    linkedin                        varchar(255),
    curriculo_arquivo_path          varchar(255),
    curriculo_arquivo_nome_original varchar(255),
    status                          varchar(20) not null default 'INSCRITO',
    frequencia                      int not null default 0,
    motivo_desistencia              text
);

create table teste_ingles (
    educando_id    bigint primary key references educando (id),
    nivel          varchar(10) not null,
    pontuacao      int not null,
    data_realizacao date not null
);

create table curso_anterior (
    id          bigserial primary key,
    educando_id bigint not null references educando (id),
    nome        varchar(150) not null,
    ano         int not null,
    situacao    varchar(50) not null
);

create table historico_educando (
    id          bigserial primary key,
    educando_id bigint not null references educando (id),
    titulo      varchar(150) not null,
    data_evento date not null
);

create table boletim_item (
    id                    bigserial primary key,
    educando_id           bigint not null references educando (id),
    disciplina_nome       varchar(150) not null,
    cp1                   numeric(4, 2),
    gs1                   numeric(4, 2),
    md1                   numeric(4, 2),
    cp2                   numeric(4, 2),
    gs2                   numeric(4, 2),
    md2                   numeric(4, 2),
    faltas                int not null default 0,
    presencas_total       int not null default 0,
    presencas_realizadas  int not null default 0,
    frequencia_percentual numeric(5, 2) not null default 0,
    media_parcial         numeric(4, 2)
);

create table educador (
    id            bigserial primary key,
    usuario_id    bigint not null unique references usuario (id),
    papel         varchar(20) not null,
    turmas        int not null default 0,
    ultimo_acesso timestamptz
);

create table evento (
    id          bigserial primary key,
    tipo        varchar(20) not null,
    titulo      varchar(150) not null,
    descricao   text,
    local       varchar(150),
    data_hora   timestamp not null,
    vagas_total int not null
);

create table inscricao_evento (
    id           bigserial primary key,
    educando_id  bigint not null references educando (id),
    evento_id    bigint not null references evento (id),
    inscrito_em  timestamptz not null default now(),
    unique (educando_id, evento_id)
);

create table auditoria_alteracao (
    id               bigserial primary key,
    educando_id      bigint not null references educando (id),
    autor_usuario_id bigint not null references usuario (id),
    campo            varchar(20) not null,
    valor_anterior   varchar(255),
    valor_novo       varchar(255),
    criado_em        timestamptz not null default now()
);

create table password_reset_token (
    id          bigserial primary key,
    usuario_id  bigint not null references usuario (id),
    token       varchar(255) not null unique,
    expira_em   timestamptz not null,
    usado       boolean not null default false
);

create index idx_educando_status on educando (status);
create index idx_educando_curso on educando (curso_id);
create index idx_auditoria_educando on auditoria_alteracao (educando_id);
create index idx_inscricao_evento_evento on inscricao_evento (evento_id);
