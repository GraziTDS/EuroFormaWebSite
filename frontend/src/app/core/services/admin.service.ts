import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminVisaoGeral } from '../models';

@Injectable({ providedIn: 'root' })
export class AdminService {
  constructor(private readonly http: HttpClient) {}

  visaoGeral(): Observable<AdminVisaoGeral> {
    return this.http.get<AdminVisaoGeral>('/api/admin/visao-geral');
  }
}
