import { HttpClient } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginResponse, Role } from '../models';

const STORAGE_KEY = 'euroforma.sessao';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly usuario = signal<LoginResponse | null>(this.carregarSessao());

  readonly usuarioAtual = this.usuario.asReadonly();
  readonly autenticado = computed(() => this.usuario() !== null);
  readonly role = computed<Role | null>(() => this.usuario()?.role ?? null);

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {}

  login(email: string, senha: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/api/auth/login', { email, senha }).pipe(
      tap((resposta) => {
        this.usuario.set(resposta);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(resposta));
      }),
    );
  }

  logout(): void {
    this.usuario.set(null);
    localStorage.removeItem(STORAGE_KEY);
    this.router.navigateByUrl('/login');
  }

  esqueciSenha(email: string): Observable<void> {
    return this.http.post<void>('/api/auth/esqueci-senha', { email });
  }

  redefinirSenha(token: string, novaSenha: string): Observable<void> {
    return this.http.post<void>('/api/auth/redefinir-senha', { token, novaSenha });
  }

  getToken(): string | null {
    return this.usuario()?.token ?? null;
  }

  /** Mapeia o role de autenticação para o "grupo de shell" usado nas rotas (educando/educador/admin). */
  grupoDeRotas(role: Role | null): 'educando' | 'educador' | 'admin' | null {
    switch (role) {
      case 'EDUCANDO':
        return 'educando';
      case 'EDUCADOR':
      case 'COORDENADOR':
        return 'educador';
      case 'ADMINISTRADOR':
        return 'admin';
      default:
        return null;
    }
  }

  private carregarSessao(): LoginResponse | null {
    const bruto = localStorage.getItem(STORAGE_KEY);
    if (!bruto) {
      return null;
    }
    try {
      return JSON.parse(bruto) as LoginResponse;
    } catch {
      return null;
    }
  }
}
