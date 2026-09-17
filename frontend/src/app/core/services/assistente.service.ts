import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface MensagemChat {
  role: 'user' | 'assistant';
  content: string;
}

@Injectable({ providedIn: 'root' })
export class AssistenteService {
  constructor(private readonly http: HttpClient) {}

  disponivel(): Observable<{ disponivel: boolean }> {
    return this.http.get<{ disponivel: boolean }>('/api/assistente/curriculo/disponivel');
  }

  chat(historico: MensagemChat[]): Observable<{ resposta: string }> {
    return this.http.post<{ resposta: string }>('/api/assistente/curriculo/chat', { historico });
  }
}
