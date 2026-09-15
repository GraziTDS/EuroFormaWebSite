import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Educador, EducadorDashboard } from '../models';

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
}
