-- Campos adicionais alinhados com a "BASE DE DADOS EDUCACIONAL" real do Instituto Eurofarma,
-- para a importação/exportação em Excel refletir o formato que a instituição já usa hoje.
alter table educando add column rg varchar(20);
alter table educando add column nome_social varchar(150);
alter table educando add column genero varchar(50);
alter table educando add column raca varchar(50);
alter table educando add column regiao varchar(100);
alter table educando add column nome_responsavel varchar(150);
alter table educando add column contato_responsavel varchar(30);
