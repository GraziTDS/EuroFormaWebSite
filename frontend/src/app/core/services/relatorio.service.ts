import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RelatorioResponse, TipoRelatorio } from '../models';

@Injectable({ providedIn: 'root' })
export class RelatorioService {
  constructor(private readonly http: HttpClient) {}

  gerar(tipo: TipoRelatorio): Observable<RelatorioResponse> {
    return this.http.get<RelatorioResponse>('/api/relatorios', { params: { tipo } });
  }
}
