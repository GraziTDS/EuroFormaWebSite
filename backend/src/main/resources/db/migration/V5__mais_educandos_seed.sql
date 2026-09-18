-- Enriquece a base de demonstração com mais educandos (variedade de nomes, cursos, status e
-- meses de matrícula), para os dashboards, relatórios e listas ficarem populados de verdade
-- em uma apresentação — em vez dos 7 educandos originais dos apps mobile.
-- Mesma senha de todos: euroforma123 (hash já usado no seed original).

insert into usuario (id, nome, email, senha_hash, role, ativo) values
    (100, 'Rafaela Costa Lima', 'rafaela.lima@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (101, 'Thiago Almeida Souza', 'thiago.souza@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (102, 'Camila Rodrigues Pereira', 'camila.pereira@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (103, 'Vinícius Santos Barbosa', 'vinicius.barbosa@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (104, 'Larissa Oliveira Martins', 'larissa.martins@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (105, 'Gustavo Ferreira Ribeiro', 'gustavo.ribeiro@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (106, 'Juliana Carvalho Nunes', 'juliana.nunes@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (107, 'Rodrigo Mendes Araújo', 'rodrigo.araujo@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (108, 'Beatriz Gonçalves Teixeira', 'beatriz.teixeira@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (109, 'Leonardo Batista Correia', 'leonardo.correia@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (110, 'Fernanda Dias Cardoso', 'fernanda.cardoso@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (111, 'Matheus Rocha Pinto', 'matheus.pinto@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (112, 'Isabela Nascimento Freitas', 'isabela.freitas@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (113, 'Bruno Cavalcanti Moura', 'bruno.moura@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (114, 'Amanda Vieira Duarte', 'amanda.duarte@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (115, 'Daniel Monteiro Azevedo', 'daniel.azevedo@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (116, 'Patrícia Ramos Andrade', 'patricia.andrade@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (117, 'Felipe Cunha Barros', 'felipe.barros@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (118, 'Natália Farias Campos', 'natalia.campos@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (119, 'Marcelo Guimarães Xavier', 'marcelo.xavier@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (120, 'Débora Pires Fonseca', 'debora.fonseca@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (121, 'Rafael Siqueira Neto', 'rafael.neto@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (122, 'Priscila Machado Rezende', 'priscila.rezende@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (123, 'Eduardo Bezerra Lopes', 'eduardo.lopes@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (124, 'Vanessa Moreira Castro', 'vanessa.castro@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (125, 'Igor Pacheco Melo', 'igor.melo@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (126, 'Aline Corrêa Sales', 'aline.sales@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (127, 'Diego Nogueira Vasconcelos', 'diego.vasconcelos@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (128, 'Sabrina Teles Farias', 'sabrina.farias@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true),
    (129, 'Otávio Lacerda Brandão', 'otavio.brandao@email.com', '$2a$10$9yu76ZF3xLduQJZ.Ctcq1e86v9Y2PcfrgfaqVCsg3FbZWUVwamfd6', 'EDUCANDO', true);

-- educando_id = usuario_id - 100 + 100 (mesmo número, só pra facilitar leitura)
-- curso_id/turma_id giram entre os 4 cursos/turmas já existentes; status e frequência variam
-- para os relatórios (matrículas, frequência, conclusão & evasão, desistência) terem o que mostrar.
insert into educando (id, usuario_id, cpf, telefone, nascimento, curso_id, turma_id, progresso, media,
                       iniciado_em, endereco_rua, endereco_bairro, endereco_cep, endereco_cidade, endereco_uf,
                       status, frequencia, motivo_desistencia) values
    (100, 100, '900.000.001-00', '(11) 91000-0001', '2003-03-12', 1, 1, 55, 7.4, '2025-07-14', 'Rua Aimberê, 210', 'Perdizes', '05018-000', 'São Paulo', 'SP', 'EM_CURSO', 88, null),
    (101, 101, '900.000.002-00', '(11) 91000-0002', '2002-11-02', 2, 2, 40, 6.8, '2025-07-14', 'Rua Cardoso de Almeida, 500', 'Perdizes', '05013-000', 'São Paulo', 'SP', 'EM_CURSO', 74, null),
    (102, 102, '900.000.003-00', '(11) 91000-0003', '2004-05-20', 3, 3, 0, 0.0, '2026-05-04', 'Rua Girassol, 88', 'Vila Madalena', '05433-000', 'São Paulo', 'SP', 'INSCRITO', 0, null),
    (103, 103, '900.000.004-00', '(11) 91000-0004', '2001-08-09', 4, 4, 100, 9.1, '2025-09-08', 'Rua Harmonia, 320', 'Sumaré', '05435-000', 'São Paulo', 'SP', 'CONCLUIDO', 97, null),
    (104, 104, '900.000.005-00', '(11) 91000-0005', '2003-01-27', 1, 1, 30, 5.9, '2025-09-08', 'Rua Fradique Coutinho, 150', 'Pinheiros', '05416-000', 'São Paulo', 'SP', 'EM_CURSO', 61, null),
    (105, 105, '900.000.006-00', '(11) 91000-0006', '2002-06-15', 2, 2, 22, 6.4, '2025-09-08', 'Rua Teodoro Sampaio, 1200', 'Pinheiros', '05406-000', 'São Paulo', 'SP', 'DESISTENTE', 28, 'Mudança de cidade por motivo de trabalho do responsável.'),
    (106, 106, '900.000.007-00', '(11) 91000-0007', '2000-12-30', 3, 3, 100, 8.7, '2025-09-08', 'Rua Purpurina, 400', 'Vila Madalena', '05435-030', 'São Paulo', 'SP', 'CONCLUIDO', 95, null),
    (107, 107, '900.000.008-00', '(11) 91000-0008', '2003-09-05', 4, 4, 45, 7.0, '2025-11-10', 'Rua Wisard, 77', 'Vila Madalena', '05434-000', 'São Paulo', 'SP', 'EM_CURSO', 82, null),
    (108, 108, '900.000.009-00', '(11) 91000-0009', '2004-02-18', 1, 1, 18, 6.1, '2025-11-10', 'Rua Aspicuelta, 210', 'Vila Madalena', '05433-010', 'São Paulo', 'SP', 'EM_CURSO', 69, null),
    (109, 109, '900.000.010-00', '(11) 91000-0010', '2001-04-22', 2, 2, 0, 0.0, '2026-05-04', 'Rua Mourato Coelho, 500', 'Pinheiros', '05417-000', 'São Paulo', 'SP', 'INSCRITO', 0, null),
    (110, 110, '900.000.011-00', '(11) 91000-0011', '2002-10-11', 3, 3, 60, 7.9, '2025-11-10', 'Rua Simão Álvares, 900', 'Pinheiros', '05417-020', 'São Paulo', 'SP', 'EM_CURSO', 91, null),
    (111, 111, '900.000.012-00', '(11) 91000-0012', '2003-07-07', 4, 4, 15, 5.2, '2025-11-10', 'Rua Cunha Gago, 320', 'Pinheiros', '05421-000', 'São Paulo', 'SP', 'DESISTENTE', 22, 'Incompatibilidade de horário com novo trabalho.'),
    (112, 112, '900.000.013-00', '(11) 91000-0013', '2000-01-16', 1, 1, 100, 9.4, '2025-08-03', 'Rua Diana, 60', 'Sumarezinho', '05445-000', 'São Paulo', 'SP', 'CONCLUIDO', 99, null),
    (113, 113, '900.000.014-00', '(11) 91000-0014', '2004-08-25', 2, 2, 33, 6.6, '2026-01-12', 'Rua Girassol, 150', 'Vila Madalena', '05433-001', 'São Paulo', 'SP', 'EM_CURSO', 77, null),
    (114, 114, '900.000.015-00', '(11) 91000-0015', '2003-05-30', 3, 3, 52, 7.2, '2026-01-12', 'Rua Fidalga, 200', 'Vila Madalena', '05432-000', 'São Paulo', 'SP', 'EM_CURSO', 86, null),
    (115, 115, '900.000.016-00', '(11) 91000-0016', '2001-09-14', 4, 4, 0, 0.0, '2026-05-04', 'Rua Medeiros de Albuquerque, 90', 'Vila Madalena', '05432-010', 'São Paulo', 'SP', 'INSCRITO', 0, null),
    (116, 116, '900.000.017-00', '(11) 91000-0017', '2002-03-03', 1, 1, 70, 8.1, '2026-01-12', 'Rua Bandeira Paulista, 500', 'Itaim Bibi', '04532-000', 'São Paulo', 'SP', 'EM_CURSO', 93, null),
    (117, 117, '900.000.018-00', '(11) 91000-0018', '2003-12-01', 2, 2, 28, 5.7, '2026-01-12', 'Rua Joaquim Floriano, 700', 'Itaim Bibi', '04534-000', 'São Paulo', 'SP', 'EM_CURSO', 58, null),
    (118, 118, '900.000.019-00', '(11) 91000-0019', '2000-06-19', 3, 3, 100, 9.0, '2025-08-03', 'Rua Tabapuã, 300', 'Itaim Bibi', '04533-000', 'São Paulo', 'SP', 'CONCLUIDO', 96, null),
    (119, 119, '900.000.020-00', '(11) 91000-0020', '2004-04-04', 4, 4, 12, 4.8, '2026-01-12', 'Rua Leopoldo Couto de Magalhães, 200', 'Itaim Bibi', '04542-000', 'São Paulo', 'SP', 'DESISTENTE', 18, 'Baixa frequência por problemas de saúde na família.'),
    (120, 120, '900.000.021-00', '(11) 91000-0021', '2001-02-27', 1, 1, 48, 7.0, '2026-03-09', 'Rua Estados Unidos, 1500', 'Jardim América', '01427-000', 'São Paulo', 'SP', 'EM_CURSO', 80, null),
    (121, 121, '900.000.022-00', '(11) 91000-0022', '2003-10-08', 2, 2, 35, 6.9, '2026-03-09', 'Rua Oscar Freire, 900', 'Jardins', '01426-000', 'São Paulo', 'SP', 'EM_CURSO', 71, null),
    (122, 122, '900.000.023-00', '(11) 91000-0023', '2002-07-23', 3, 3, 0, 0.0, '2026-05-04', 'Rua Haddock Lobo, 400', 'Jardins', '01414-000', 'São Paulo', 'SP', 'INSCRITO', 0, null),
    (123, 123, '900.000.024-00', '(11) 91000-0024', '2000-11-11', 4, 4, 100, 8.9, '2025-08-03', 'Rua Pamplona, 1200', 'Jardim Paulista', '01405-000', 'São Paulo', 'SP', 'CONCLUIDO', 94, null),
    (124, 124, '900.000.025-00', '(11) 91000-0025', '2004-01-05', 1, 1, 20, 6.0, '2026-03-09', 'Alameda Lorena, 800', 'Jardim Paulista', '01424-000', 'São Paulo', 'SP', 'EM_CURSO', 63, null),
    (125, 125, '900.000.026-00', '(11) 91000-0026', '2003-06-06', 2, 2, 41, 7.3, '2026-03-09', 'Rua Bela Cintra, 1600', 'Consolação', '01415-000', 'São Paulo', 'SP', 'EM_CURSO', 84, null),
    (126, 126, '900.000.027-00', '(11) 91000-0027', '2001-05-17', 3, 3, 100, 9.2, '2025-08-03', 'Rua Augusta, 2200', 'Consolação', '01412-000', 'São Paulo', 'SP', 'CONCLUIDO', 98, null),
    (127, 127, '900.000.028-00', '(11) 91000-0028', '2002-09-29', 4, 4, 26, 5.5, '2026-03-09', 'Rua Frei Caneca, 500', 'Consolação', '01307-000', 'São Paulo', 'SP', 'EM_CURSO', 55, null),
    (128, 128, '900.000.029-00', '(11) 91000-0029', '2003-02-14', 1, 1, 0, 0.0, '2026-05-04', 'Rua Maria Antônia, 300', 'Vila Buarque', '01222-000', 'São Paulo', 'SP', 'INSCRITO', 0, null),
    (129, 129, '900.000.030-00', '(11) 91000-0030', '2000-08-08', 2, 2, 16, 4.5, '2026-01-12', 'Rua Barão de Itapetininga, 150', 'República', '01042-000', 'São Paulo', 'SP', 'DESISTENTE', 12, 'Desistência sem motivo informado pelo educando.');

insert into historico_educando (educando_id, titulo, data_evento)
select id, 'Inscrição realizada', iniciado_em from educando where id between 100 and 129;

insert into historico_educando (educando_id, titulo, data_evento)
select id, 'Início do curso', iniciado_em + 5 from educando where id between 100 and 129 and status <> 'INSCRITO';

insert into historico_educando (educando_id, titulo, data_evento)
select id, 'Curso concluído', iniciado_em + 150 from educando where id between 100 and 129 and status = 'CONCLUIDO';

insert into historico_educando (educando_id, titulo, data_evento)
select id, 'Desistência registrada', iniciado_em + 60 from educando where id between 100 and 129 and status = 'DESISTENTE';

select setval('usuario_id_seq', (select max(id) from usuario));
select setval('educando_id_seq', (select max(id) from educando));
select setval('historico_educando_id_seq', (select max(id) from historico_educando));
