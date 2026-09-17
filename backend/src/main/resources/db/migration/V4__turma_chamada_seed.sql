-- Turmas (uma por curso, sob responsabilidade de um educador/coordenador já existente).
insert into turma (id, nome, curso_id, educador_id) values
    (1, 'Auxiliar de Farmácia - Turma A', 1, 2),
    (2, 'Logística Farmacêutica - Turma A', 2, 3),
    (3, 'Atendimento ao Cliente - Turma A', 3, 5),
    (4, 'Empreendedorismo Social - Turma A', 4, 1);

-- Matrícula dos 7 educandos seed nas respectivas turmas do seu curso.
update educando set turma_id = 1 where id in (1, 4); -- Ana, Diego (Auxiliar de Farmácia)
update educando set turma_id = 2 where id in (2, 6); -- Bruno, Felipe (Logística Farmacêutica)
update educando set turma_id = 3 where id in (3, 7); -- Carla, Gabriela (Atendimento ao Cliente)
update educando set turma_id = 4 where id = 5;       -- Eduarda (Empreendedorismo Social)

-- Duas aulas de exemplo na Turma A de Auxiliar de Farmácia, com chamada já registrada.
insert into aula (id, turma_id, data_aula, tema) values
    (1, 1, '2026-03-02', 'Farmacologia básica — revisão'),
    (2, 1, '2026-03-09', 'Boas práticas de manipulação');

insert into presenca_aula (aula_id, educando_id, presente) values
    (1, 1, true),
    (1, 4, true),
    (2, 1, true),
    (2, 4, false);

select setval('turma_id_seq', (select max(id) from turma));
select setval('aula_id_seq', (select max(id) from aula));
select setval('presenca_aula_id_seq', (select max(id) from presenca_aula));
