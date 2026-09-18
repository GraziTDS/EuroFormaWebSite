import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EducadorService } from '../../../core/services/educador.service';
import { Educador, PapelEducador } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';

interface RascunhoEdicao {
  nome: string;
  email: string;
  papel: PapelEducador;
  turmas: number;
}

@Component({
  selector: 'app-admin-usuarios',
  imports: [
    DatePipe,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    SectionCard,
  ],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.scss',
})
export class AdminUsuarios implements OnInit {
  protected readonly educadores = signal<Educador[]>([]);
  protected readonly editandoId = signal<number | null>(null);
  protected readonly salvando = signal(false);
  protected rascunho: RascunhoEdicao = { nome: '', email: '', papel: 'EDUCADOR', turmas: 0 };

  constructor(
    private readonly educadorService: EducadorService,
    private readonly snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  alternarAtivo(educador: Educador): void {
    this.educadorService.atualizarAtivo(educador.id, !educador.ativo).subscribe(() => this.carregar());
  }

  editar(educador: Educador): void {
    this.editandoId.set(educador.id);
    this.rascunho = {
      nome: educador.nome,
      email: educador.email,
      papel: educador.papel,
      turmas: educador.turmas,
    };
  }

  cancelarEdicao(): void {
    this.editandoId.set(null);
  }

  salvar(id: number): void {
    this.salvando.set(true);
    this.educadorService.atualizar(id, { ...this.rascunho }).subscribe({
      next: () => {
        this.salvando.set(false);
        this.editandoId.set(null);
        this.snackBar.open('Perfil atualizado com sucesso.', 'Fechar', { duration: 3000 });
        this.carregar();
      },
      error: (err) => {
        this.salvando.set(false);
        this.snackBar.open(err?.error?.message ?? 'Falha ao atualizar o perfil.', 'Fechar', { duration: 4000 });
      },
    });
  }

  private carregar(): void {
    this.educadorService.listarUsuarios().subscribe((lista) => this.educadores.set(lista));
  }
}
