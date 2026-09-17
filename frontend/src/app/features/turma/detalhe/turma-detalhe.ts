import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TurmaService } from '../../../core/services/turma.service';
import { Aula, PresencaItem } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';

@Component({
  selector: 'app-turma-detalhe',
  imports: [DatePipe, FormsModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatCheckboxModule, SectionCard],
  templateUrl: './turma-detalhe.html',
  styleUrl: './turma-detalhe.scss',
})
export class TurmaDetalhe implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly turmaService = inject(TurmaService);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly turmaId = Number(this.route.snapshot.paramMap.get('id'));
  protected readonly aulas = signal<Aula[]>([]);
  protected readonly aulaSelecionada = signal<Aula | null>(null);
  protected readonly presencas = signal<PresencaItem[]>([]);
  protected readonly salvando = signal(false);

  protected novaAulaData = new Date().toISOString().substring(0, 10);
  protected novaAulaTema = '';

  ngOnInit(): void {
    this.carregarAulas();
  }

  criarAula(): void {
    if (!this.novaAulaData) {
      return;
    }
    this.turmaService.criarAula(this.turmaId, this.novaAulaData, this.novaAulaTema).subscribe({
      next: (aula) => {
        this.novaAulaTema = '';
        this.carregarAulas();
        this.abrirChamada(aula);
      },
      error: (err) => {
        this.snackBar.open(err?.error?.message ?? 'Não foi possível registrar a aula.', 'Fechar', { duration: 4000 });
      },
    });
  }

  abrirChamada(aula: Aula): void {
    this.aulaSelecionada.set(aula);
    this.turmaService.obterChamada(aula.id).subscribe((lista) => this.presencas.set(lista));
  }

  alternarPresenca(item: PresencaItem): void {
    this.presencas.update((lista) =>
      lista.map((p) => (p.educandoId === item.educandoId ? { ...p, presente: !p.presente } : p)),
    );
  }

  salvarChamada(): void {
    const aula = this.aulaSelecionada();
    if (!aula) {
      return;
    }
    this.salvando.set(true);
    const payload = this.presencas().map((p) => ({ educandoId: p.educandoId, presente: p.presente }));
    this.turmaService.salvarChamada(aula.id, payload).subscribe(() => {
      this.salvando.set(false);
      this.snackBar.open('Chamada salva com sucesso.', 'Fechar', { duration: 3000 });
      this.carregarAulas();
    });
  }

  private carregarAulas(): void {
    this.turmaService.listarAulas(this.turmaId).subscribe((aulas) => this.aulas.set(aulas));
  }
}
