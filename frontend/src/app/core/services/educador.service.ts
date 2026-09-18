import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Educador, EducadorDashboard, PapelEducador } from '../models';

export interface AtualizarEducadorPayload {
  nome: string;
  email: string;
  papel: PapelEducador;
  turmas: number;
}

@Injectable({ providedIn: 'root' })
export class EducadorService {
  constructor(private readonly http: HttpClient) {}

  dashboard(): Observable<EducadorDashboard> {
    return this.http.get<EducadorDashboard>('/api/educador/dashboard');
  }

  listarUsuarios(): Observable<Educador[]> {
    return this.http.get<Educador[]>('/api/educadores');
  }

  atualizarAtivo(id: number, ativo: boolean): Observable<void> {
    return this.http.patch<void>(`/api/educadores/${id}/ativo`, { ativo });
  }

  atualizar(id: number, payload: AtualizarEducadorPayload): Observable<Educador> {
    return this.http.put<Educador>(`/api/educadores/${id}`, payload);
  }
}
