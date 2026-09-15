import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-redefinir-senha',
  imports: [ReactiveFormsModule, RouterLink, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './redefinir-senha.html',
  styleUrl: './login.scss',
})
export class RedefinirSenha {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly token = this.route.snapshot.queryParamMap.get('token') ?? '';

  protected readonly form = this.fb.nonNullable.group({
    novaSenha: ['', [Validators.required, Validators.minLength(6)]],
  });

  protected readonly sucesso = signal(false);
  protected readonly erro = signal<string | null>(null);

  redefinir(): void {
    if (this.form.invalid || !this.token) {
      this.form.markAllAsTouched();
      return;
    }
    this.erro.set(null);
    this.authService.redefinirSenha(this.token, this.form.getRawValue().novaSenha).subscribe({
      next: () => {
        this.sucesso.set(true);
        setTimeout(() => this.router.navigateByUrl('/login'), 2500);
      },
      error: () => this.erro.set('Este link é inválido ou já expirou. Solicite um novo.'),
    });
  }
}
