# Roteiro de apresentação — euroForma Web

Guia rápido de apoio para a apresentação: o que já está pronto, o que está em desenvolvimento e
pontos que valem a pena destacar ao vivo.

## ✅ O que já está pronto

- **Login real** com recuperação de senha por e-mail (JWT, sem perfil "mockado" como nos apps mobile).
- **Educando**: painel com progresso/frequência/média, boletim completo (notas, faltas, legenda de
  desempenho), ficha de perfil, **upload real de currículo (PDF)**, botão de acesso ao Moodle,
  inscrição em eventos e teste de inglês (link para a Preply).
- **Educador/Coordenador**: dashboard com gráficos (evolução de matrículas por mês, distribuição por
  status), lista/busca de educandos, cadastro de novo educando, **controle de faltas por turma**
  (calendário de aulas + chamada de presença, exclusivo do Educador/Coordenador).
- **Administrador — Governança**: visão geral institucional com gráficos, gestão de usuários
  (ativar/desativar educadores), gestão de educandos (status/frequência com auditoria — atribuição
  exclusiva do Administrador), relatórios com gráficos (matrículas por curso, frequência, conclusão &
  evasão, motivos de desistência).
- **Importação/exportação em Excel** de educandos e de relatórios — inclui um botão **"Baixar
  modelo"**, então a planilha já vem pronta no formato certo para só preencher e importar.
- **Backend rodando com uma base mockada bem populada** (39 educandos, distribuídos entre os 4
  cursos e diferentes status, com 9 meses de matrícula diferentes) — os gráficos e relatórios já
  aparecem com dados de verdade, não só 3-4 registros de exemplo.

## 🚧 Em desenvolvimento / Próximos passos

- **Assistente de IA para insights de currículo**: já implementado tecnicamente (chat flutuante na
  tela do educando, usando a API da Anthropic), mas tratado como demonstração futura — precisa de uma
  chave de API configurada para ficar ativo em produção.
- **Link do Moodle**: botão já existe na tela inicial do educando, mas hoje aponta para uma URL
  placeholder — falta o link real do Moodle do Instituto Eurofarma.
- **API de integração com o Moodle**: próximo passo natural depois do link — sincronizar dados de
  frequência/notas automaticamente em vez de cadastro manual.

## 🎤 Pontos para destacar na apresentação

- **Governança**: a gestão de usuários e o controle de status/frequência/auditoria dos educandos são
  uma atribuição exclusiva do Administrador — reforça a ideia de governança institucional (Educador
  acompanha, mas não administra).
- **Formato pronto para importar**: quem for importar uma planilha de educandos não precisa adivinhar
  as colunas — tem um botão de "Baixar modelo" já no formato certo.
- **Backend mockado, mas com volume real**: a base já sobe com quase 40 educandos variados, então
  dashboards e relatórios não parecem vazios numa demonstração.
- **Inserção de eventos e inscrições é exclusiva**: um educando só se inscreve em nome dele mesmo (sem
  inscrição por terceiros) e não consegue se inscrever duas vezes no mesmo evento — regra garantida
  pelo próprio banco de dados (chave única), não só pela tela.
- **Controle de faltas (chamada)**: sim, já está pronto e funcional — calendário de aulas por turma e
  lista de presença, exclusivo do Educador/Coordenador responsável por aquela turma.
