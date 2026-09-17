create table turma (
    id          bigserial primary key,
    nome        varchar(150) not null,
    curso_id    bigint not null references curso (id),
    educador_id bigint not null references educador (id)
);

alter table educando add column turma_id bigint references turma (id);

create table aula (
    id       bigserial primary key,
    turma_id bigint not null references turma (id),
    data_aula date not null,
    tema     varchar(255),
    unique (turma_id, data_aula)
);

create table presenca_aula (
    id          bigserial primary key,
    aula_id     bigint not null references aula (id),
    educando_id bigint not null references educando (id),
    presente    boolean not null default true,
    unique (aula_id, educando_id)
);

create index idx_educando_turma on educando (turma_id);
create index idx_aula_turma on aula (turma_id);
create index idx_presenca_aula on presenca_aula (aula_id);
