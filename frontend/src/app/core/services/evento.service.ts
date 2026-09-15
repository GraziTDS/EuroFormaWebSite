import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Evento } from '../models';

@Injectable({ providedIn: 'root' })
export class EventoService {
  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Evento[]> {
    return this.http.get<Evento[]>('/api/eventos');
  }

  inscrever(id: number): Observable<void> {
    return this.http.post<void>(`/api/eventos/${id}/inscricao`, {});
  }
}
