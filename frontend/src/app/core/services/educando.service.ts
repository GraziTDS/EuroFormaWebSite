import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Auditoria, EducandoPerfil, EducandoResumo, StatusEducando } from '../models';

export interface CadastroEducandoPayload {
  nomeCompleto: string;
  cpf: string;
  telefone: string;
  email: string;
  cursoId: number;
}

@Injectable({ providedIn: 'root' })
export class EducandoService {
  constructor(private readonly http: HttpClient) {}

  meuPerfil(): Observable<EducandoPerfil> {
    return this.http.get<EducandoPerfil>('/api/educandos/me');
  }

  enviarMeuCurriculo(arquivo: File): Observable<void> {
    const formData = new FormData();
    formData.append('arquivo', arquivo);
    return this.http.post<void>('/api/educandos/me/curriculo', formData);
  }

  listar(busca?: string, status?: StatusEducando | ''): Observable<EducandoResumo[]> {
    const params: Record<string, string> = {};
    if (busca) {
      params['busca'] = busca;
    }
    if (status) {
      params['status'] = status;
    }
    return this.http.get<EducandoResumo[]>('/api/educandos', { params });
  }

  obter(id: number): Observable<EducandoPerfil> {
    return this.http.get<EducandoPerfil>(`/api/educandos/${id}`);
  }

  cadastrar(payload: CadastroEducandoPayload): Observable<EducandoPerfil> {
    return this.http.post<EducandoPerfil>('/api/educandos', payload);
  }

  atualizarStatus(id: number, status: StatusEducando, motivoDesistencia?: string): Observable<void> {
    return this.http.patch<void>(`/api/educandos/${id}/status`, { status, motivoDesistencia });
  }

  atualizarFrequencia(id: number, frequencia: number): Observable<void> {
    return this.http.patch<void>(`/api/educandos/${id}/frequencia`, { frequencia });
  }

  auditoria(id: number): Observable<Auditoria[]> {
    return this.http.get<Auditoria[]>(`/api/educandos/${id}/auditoria`);
  }

  baixarCurriculoUrl(id: number): string {
    return `/api/educandos/${id}/curriculo`;
  }
}
