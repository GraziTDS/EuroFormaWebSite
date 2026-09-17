export type Role = 'EDUCANDO' | 'EDUCADOR' | 'COORDENADOR' | 'ADMINISTRADOR';

export type StatusEducando = 'INSCRITO' | 'EM_CURSO' | 'CONCLUIDO' | 'DESISTENTE';

export type TipoEvento = 'WORKSHOP' | 'PALESTRA' | 'MENTORIA' | 'NETWORKING';

export type PapelEducador = 'EDUCADOR' | 'COORDENADOR';

export type TipoRelatorio =
  | 'MATRICULAS_POR_CURSO'
  | 'FREQUENCIA_POR_EDUCANDO'
  | 'CONCLUSAO_EVASAO'
  | 'MOTIVOS_DESISTENCIA';

export type CampoAuditoria = 'STATUS' | 'FREQUENCIA';

export interface LoginResponse {
  token: string;
  id: number;
  nome: string;
  email: string;
  role: Role;
}

export interface Curso {
  id: number;
  nome: string;
}

export interface Endereco {
  rua: string | null;
  bairro: string | null;
  cep: string | null;
  cidade: string | null;
  uf: string | null;
}

export interface TesteIngles {
  nivel: string;
  pontuacao: number;
  data: string;
}

export interface CursoAnterior {
  nome: string;
  ano: number;
  situacao: string;
}

export interface Historico {
  titulo: string;
  data: string;
}

export interface Disciplina {
  nome: string;
  cp1: number | null;
  gs1: number | null;
  md1: number | null;
  cp2: number | null;
  gs2: number | null;
  md2: number | null;
  faltas: number;
  presencasTotal: number;
  presencasRealizadas: number;
  frequenciaPercentual: number;
  mediaParcial: number | null;
}

export interface EducandoResumo {
  id: number;
  nome: string;
  curso: string;
  frequencia: number;
  progresso: number;
  status: StatusEducando;
}

export interface EducandoPerfil {
  id: number;
  nome: string;
  email: string;
  telefone: string | null;
  cpf: string | null;
  nascimento: string | null;
  idade: number | null;
  curso: string;
  progresso: number;
  media: number;
  iniciadoEm: string | null;
  endereco: Endereco | null;
  linkedin: string | null;
  curriculoArquivoNomeOriginal: string | null;
  curriculoDisponivel: boolean;
  testeIngles: TesteIngles | null;
  cursosAnteriores: CursoAnterior[];
  historico: Historico[];
  boletim: Disciplina[];
  status: StatusEducando;
  frequencia: number;
  motivoDesistencia: string | null;
}

export interface Auditoria {
  campo: CampoAuditoria;
  valorAnterior: string | null;
  valorNovo: string | null;
  autorNome: string;
  criadoEm: string;
}

export interface Educador {
  id: number;
  nome: string;
  email: string;
  papel: PapelEducador;
  turmas: number;
  ultimoAcesso: string | null;
  ativo: boolean;
}

export interface PontoEvolucao {
  mes: string;
  total: number;
}

export interface EducadorDashboard {
  totalAlunos: number;
  ativos: number;
  concluintes: number;
  taxaConclusao: number;
  distribuicaoPorStatus: Partial<Record<StatusEducando, number>>;
  recentes: EducandoResumo[];
  evolucaoMensal: PontoEvolucao[];
}

export interface AdminVisaoGeral {
  totalEducandos: number;
  ativos: number;
  presencaMedia: number;
  taxaConclusao: number;
  concluidos: number;
  educadoresAtivos: number;
  educadoresTotal: number;
  distribuicaoPorStatus: Partial<Record<StatusEducando, number>>;
  educandosPorCurso: Record<string, number>;
  evolucaoMensal: PontoEvolucao[];
}

export interface Evento {
  id: number;
  tipo: TipoEvento;
  titulo: string;
  descricao: string | null;
  local: string | null;
  dataHora: string;
  vagasTotal: number;
  vagasOcupadas: number;
  inscrito: boolean;
}

export interface Turma {
  id: number;
  nome: string;
  curso: string;
  educadorNome: string;
  totalEducandos: number;
}

export interface Aula {
  id: number;
  data: string;
  tema: string | null;
  totalPresentes: number;
  totalEducandos: number;
}

export interface PresencaItem {
  educandoId: number;
  nome: string;
  presente: boolean;
}

export interface RelatorioResponse {
  tipo: TipoRelatorio;
  noFiltro: number;
  ativos: number;
  concluidos: number;
  desistentes: number;
  matriculasPorCurso: { chave: string; total: number }[] | null;
  frequenciaPorEducando: { nome: string; frequencia: number }[] | null;
  motivosDesistencia: { nome: string; motivo: string | null }[] | null;
}
