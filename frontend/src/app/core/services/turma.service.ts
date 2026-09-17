import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Aula, PresencaItem, Turma } from '../models';

@Injectable({ providedIn: 'root' })
export class TurmaService {
  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Turma[]> {
    return this.http.get<Turma[]>('/api/turmas');
  }

  listarAulas(turmaId: number): Observable<Aula[]> {
    return this.http.get<Aula[]>(`/api/turmas/${turmaId}/aulas`);
  }

  criarAula(turmaId: number, data: string, tema: string): Observable<Aula> {
    return this.http.post<Aula>(`/api/turmas/${turmaId}/aulas`, { data, tema });
  }

  obterChamada(aulaId: number): Observable<PresencaItem[]> {
    return this.http.get<PresencaItem[]>(`/api/aulas/${aulaId}/chamada`);
  }

  salvarChamada(aulaId: number, presencas: { educandoId: number; presente: boolean }[]): Observable<void> {
    return this.http.put<void>(`/api/aulas/${aulaId}/chamada`, { presencas });
  }
}
