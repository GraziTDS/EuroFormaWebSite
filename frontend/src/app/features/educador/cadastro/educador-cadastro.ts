import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CursoService } from '../../../core/services/curso.service';
import { EducandoService } from '../../../core/services/educando.service';
import { Curso } from '../../../core/models';

@Component({
  selector: 'app-educador-cadastro',
  imports: [ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule],
  templateUrl: './educador-cadastro.html',
  styleUrl: './educador-cadastro.scss',
})
export class EducadorCadastro implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly cursoService = inject(CursoService);
  private readonly educandoService = inject(EducandoService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly router = inject(Router);

  protected readonly cursos = signal<Curso[]>([]);
  protected readonly enviando = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    nomeCompleto: ['', Validators.required],
    cpf: [''],
    telefone: [''],
    email: ['', [Validators.required, Validators.email]],
    cursoId: [null as number | null, Validators.required],
  });

  ngOnInit(): void {
    this.cursoService.listar().subscribe((cursos) => this.cursos.set(cursos));
  }

  cadastrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.enviando.set(true);
    const valores = this.form.getRawValue();
    this.educandoService
      .cadastrar({
        nomeCompleto: valores.nomeCompleto,
        cpf: valores.cpf,
        telefone: valores.telefone,
        email: valores.email,
        cursoId: valores.cursoId!,
      })
      .subscribe({
        next: () => {
          this.enviando.set(false);
          this.snackBar.open('Educando cadastrado! Um e-mail para definição de senha foi enviado.', 'Fechar', {
            duration: 4000,
          });
          this.form.reset();
          this.router.navigateByUrl(this.router.url.includes('/admin') ? '/admin/educandos' : '/educador/educandos');
        },
        error: (err) => {
          this.enviando.set(false);
          this.snackBar.open(err?.error?.message ?? 'Não foi possível cadastrar o educando.', 'Fechar', {
            duration: 4000,
          });
        },
      });
  }
}
