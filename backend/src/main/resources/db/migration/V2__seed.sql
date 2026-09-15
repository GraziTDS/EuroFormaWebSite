-- Seed de desenvolvimento: replica fielmente os dados hoje hardcoded em
-- MockData.kt / mock_data.dart dos apps Flutter/Kotlin (mesmos 7 educandos, 5 educadores,
-- 1 administrador, 4 cursos e 4 eventos), agora com autenticação real.
--
-- Senha de todos os usuários seed: euroforma123 (hash BCrypt abaixo).
-- \gset/psql não é usado aqui pois o Flyway executa via JDBC puro.

-- ===================== CURSOS =====================
insert into curso (id, nome) values
    (1, 'Auxiliar de Farmácia'),
    (2, 'Logística Farmacêutica'),
    (3, 'Atendimento ao Cliente'),
    (4, 'Empreendedorismo Social');

-- ===================== USUÁRIOS =====================
-- Hash BCrypt de "euroforma123"
-- $2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6
insert into usuario (id, nome, email, senha_hash, role, ativo) values
    (1, 'Ricardo Mendonça', 'ricardo.mendonca@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'ADMINISTRADOR', true),
    (2, 'Mariana Alves', 'mariana.alves@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'COORDENADOR', true),
    (3, 'Rafael Tavares', 'rafael.tavares@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCADOR', true),
    (4, 'Juliana Pires', 'juliana.pires@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCADOR', true),
    (5, 'Sérgio Lopes', 'sergio.lopes@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCADOR', false),
    (6, 'Beatriz Cardoso', 'beatriz.cardoso@institutoeurofarma.org.br', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCADOR', true),
    (7, 'Ana Carolina Silva', 'ana.silva@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (8, 'Bruno Henrique Costa', 'bruno.costa@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (9, 'Carla Mendes Oliveira', 'carla.mendes@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (10, 'Diego Almeida Santos', 'diego.santos@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (11, 'Eduarda Ribeiro Lima', 'eduarda.lima@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (12, 'Felipe Nogueira Souza', 'felipe.souza@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (13, 'Gabriela Pinheiro Rocha', 'gabriela.rocha@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true);

-- ===================== EDUCADORES =====================
insert into educador (id, usuario_id, papel, turmas, ultimo_acesso) values
    (1, 2, 'COORDENADOR', 4, '2026-04-29 09:00:00+00'),
    (2, 3, 'EDUCADOR', 3, '2026-04-28 09:00:00+00'),
    (3, 4, 'EDUCADOR', 2, '2026-04-27 09:00:00+00'),
    (4, 5, 'EDUCADOR', 2, '2026-03-14 09:00:00+00'),
    (5, 6, 'EDUCADOR', 3, '2026-04-29 09:00:00+00');

-- ===================== EDUCANDOS =====================
insert into educando (id, usuario_id, cpf, telefone, nascimento, curso_id, progresso, media, iniciado_em,
                       endereco_rua, endereco_bairro, endereco_cep, endereco_cidade, endereco_uf,
                       linkedin, status, frequencia, motivo_desistencia) values
    (1, 7, '123.456.789-00', '(11) 98765-4321', '2002-04-11', 1, 64, 8.6, '2026-02-09',
     'Rua das Flores, 120', 'Vila Maria', '02114-000', 'São Paulo', 'SP',
     'linkedin.com/in/ana-carolina-silva', 'EM_CURSO', 92, null),
    (2, 8, '234.567.890-11', '(11) 97654-3210', '2001-08-02', 2, 100, 9.1, '2025-08-03',
     'Av. Guilherme Cotching, 780', 'Vila Nova Cachoeirinha', '02710-001', 'São Paulo', 'SP',
     'linkedin.com/in/bruno-henrique-costa', 'CONCLUIDO', 98, null),
    (3, 9, '345.678.901-22', '(11) 96543-2109', '2004-01-27', 3, 0, 0.0, '2026-04-27',
     'Rua Voluntários da Pátria, 2340', 'Santana', '02010-100', 'São Paulo', 'SP',
     'linkedin.com/in/carla-mendes-oliveira', 'INSCRITO', 0, null),
    (4, 10, '456.789.012-33', '(11) 95432-1098', '2003-06-15', 1, 48, 7.4, '2026-02-09',
     'Rua Itapicuru, 45', 'Perdizes', '01243-000', 'São Paulo', 'SP',
     'linkedin.com/in/diego-almeida-santos', 'EM_CURSO', 78, null),
    (5, 11, '567.890.123-44', '(11) 94321-0987', '2000-09-09', 4, 71, 8.9, '2026-02-09',
     'Rua Cardeal Arcoverde, 1500', 'Pinheiros', '05407-003', 'São Paulo', 'SP',
     'linkedin.com/in/eduarda-ribeiro-lima', 'EM_CURSO', 85, null),
    (6, 12, '678.901.234-55', '(11) 93210-9876', '1999-03-30', 2, 100, 9.4, '2025-08-04',
     'Rua Turiassu, 500', 'Perdizes', '05005-000', 'São Paulo', 'SP',
     'linkedin.com/in/felipe-nogueira-souza', 'CONCLUIDO', 95, null),
    (7, 13, '789.012.345-66', '(11) 92109-8765', '2002-12-14', 3, 22, 6.5, '2026-02-09',
     'Rua Domingos de Morais, 890', 'Vila Mariana', '04010-100', 'São Paulo', 'SP',
     'linkedin.com/in/gabriela-pinheiro-rocha', 'DESISTENTE', 32,
     'Conflito de horário com novo emprego em meio período. Aluna solicitou retorno futuro no próximo semestre.');

-- ===================== TESTES DE INGLÊS =====================
insert into teste_ingles (educando_id, nivel, pontuacao, data_realizacao) values
    (1, 'B1', 72, '2025-11-09'),
    (2, 'A2', 58, '2025-07-12'),
    (4, 'A2', 61, '2025-12-02'),
    (5, 'B2', 84, '2025-11-18'),
    (6, 'B1', 75, '2025-07-20');
-- educandos 3 (Carla) e 7 (Gabriela) ainda não realizaram o teste de inglês.

-- ===================== CURSOS ANTERIORES =====================
insert into curso_anterior (educando_id, nome, ano, situacao) values
    (1, 'Informática Básica', 2024, 'Concluído'),
    (1, 'Atendimento ao Cliente', 2025, 'Concluído'),
    (2, 'Informática Básica', 2024, 'Concluído'),
    (5, 'Atendimento ao Cliente', 2024, 'Concluído'),
    (6, 'Informática Básica', 2023, 'Concluído'),
    (6, 'Atendimento ao Cliente', 2024, 'Concluído');

-- ===================== HISTÓRICO =====================
insert into historico_educando (educando_id, titulo, data_evento) values
    (1, 'Inscrição realizada', '2026-02-09'),
    (1, 'Início do curso', '2026-02-14'),
    (1, 'Módulo 2 concluído', '2026-03-19'),
    (2, 'Inscrição realizada', '2025-07-20'),
    (2, 'Início do curso', '2025-08-03'),
    (2, 'Curso concluído', '2025-12-12'),
    (3, 'Inscrição realizada', '2026-04-27'),
    (4, 'Inscrição realizada', '2026-02-05'),
    (4, 'Início do curso', '2026-02-14'),
    (5, 'Inscrição realizada', '2026-02-03'),
    (5, 'Início do curso', '2026-02-14'),
    (5, 'Módulo 2 concluído', '2026-03-22'),
    (6, 'Inscrição realizada', '2025-07-22'),
    (6, 'Início do curso', '2025-08-04'),
    (6, 'Curso concluído', '2025-12-15'),
    (7, 'Inscrição realizada', '2026-02-05'),
    (7, 'Início do curso', '2026-02-14'),
    (7, 'Desistência registrada', '2026-04-10');

-- ===================== BOLETIM =====================
insert into boletim_item (educando_id, disciplina_nome, cp1, gs1, md1, cp2, gs2, md2, faltas,
                           presencas_total, presencas_realizadas, frequencia_percentual, media_parcial) values
    (1, 'Farmacologia básica', 8.0, 9.0, 8.5, 7.5, null, null, 3, 80, 77, 96.3, 8.2),
    (1, 'Boas práticas de manipulação', 9.0, 9.0, 9.0, 8.5, null, null, 2, 60, 58, 96.7, 8.8),
    (1, 'Atendimento ao cliente', 8.0, 8.0, 8.0, null, null, null, 4, 40, 36, 90.0, 8.0),
    (1, 'Informática aplicada', 7.0, 7.5, 7.3, 6.0, null, null, 10, 60, 50, 83.3, 6.8),

    (2, 'Gestão de estoque', 9.0, 9.5, 9.2, 9.0, 9.0, 9.0, 2, 80, 78, 97.5, 9.1),
    (2, 'Cadeia de frio e transporte', 8.5, 9.0, 8.7, 9.0, 9.5, 9.2, 4, 80, 76, 95.0, 9.0),

    (4, 'Farmacologia básica', 7.0, 7.5, 7.3, 7.0, null, null, 18, 80, 62, 77.5, 7.2),
    (4, 'Boas práticas de manipulação', 7.5, 8.0, 7.8, null, null, null, 13, 60, 47, 78.3, 7.8),

    (5, 'Fundamentos de empreendedorismo', 9.0, 9.0, 9.0, 8.5, null, null, 2, 60, 58, 96.7, 8.8),
    (5, 'Gestão financeira pessoal', 8.5, 9.0, 8.7, null, null, null, 4, 60, 56, 93.3, 8.7),

    (6, 'Gestão de estoque', 9.5, 9.5, 9.5, 9.0, 9.5, 9.2, 1, 80, 79, 98.8, 9.4),
    (6, 'Boas práticas de armazenagem', 9.0, 9.5, 9.2, 9.5, 9.5, 9.5, 2, 80, 78, 97.5, 9.3),

    (7, 'Comunicação e atendimento', 6.5, 7.0, 6.8, null, null, null, 14, 40, 26, 65.0, 6.8);
-- educando 3 (Carla) ainda não possui lançamentos no boletim.

-- ===================== EVENTOS =====================
insert into evento (id, tipo, titulo, descricao, local, data_hora, vagas_total) values
    (1, 'WORKSHOP', 'Workshop: Carreiras na Indústria Farmacêutica', 'Conheça as áreas e trilhas de crescimento profissional.', 'Auditório Eurofarma — Itapevi', '2026-05-13 14:00:00', 80),
    (2, 'PALESTRA', 'Palestra: Inclusão Digital e Empregabilidade', 'Como a tecnologia pode acelerar sua trajetória profissional.', 'Online (Zoom)', '2026-05-19 19:00:00', 200),
    (3, 'MENTORIA', 'Mentoria de Carreira em Grupo', 'Encontro com mentores voluntários da Eurofarma.', 'Centro Instituto Eurofarma — Osasco', '2026-05-24 10:00:00', 30),
    (4, 'NETWORKING', 'Networking Educandos & Egressos', 'Troque experiências com quem já se formou no programa.', 'Hub de Inovação Eurofarma', '2026-06-01 18:00:00', 60);

-- Algumas inscrições de exemplo entre os educandos seed (o número de vagas ocupadas
-- reflete apenas esta base de desenvolvimento, não o "+700 educandos por semestre"
-- citado como estatística institucional na tela de login).
insert into inscricao_evento (educando_id, evento_id) values
    (1, 1), (1, 2),
    (4, 1),
    (5, 3),
    (2, 4), (6, 4);

-- ===================== AJUSTE DAS SEQUENCES =====================
select setval('usuario_id_seq', (select max(id) from usuario));
select setval('curso_id_seq', (select max(id) from curso));
select setval('educando_id_seq', (select max(id) from educando));
select setval('educador_id_seq', (select max(id) from educador));
select setval('curso_anterior_id_seq', (select max(id) from curso_anterior));
select setval('historico_educando_id_seq', (select max(id) from historico_educando));
select setval('boletim_item_id_seq', (select max(id) from boletim_item));
select setval('evento_id_seq', (select max(id) from evento));
select setval('inscricao_evento_id_seq', (select max(id) from inscricao_evento));
