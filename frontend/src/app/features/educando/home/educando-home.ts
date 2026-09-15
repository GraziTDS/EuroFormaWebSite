import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EducandoService } from '../../../core/services/educando.service';
import { EventoService } from '../../../core/services/evento.service';
import { EducandoPerfil, Evento } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';

@Component({
  selector: 'app-educando-home',
  imports: [DatePipe, RouterLink, MatButtonModule, MatProgressBarModule, SectionCard, StatCard],
  templateUrl: './educando-home.html',
  styleUrl: './educando-home.scss',
})
export class EducandoHome implements OnInit {
  protected readonly perfil = signal<EducandoPerfil | null>(null);
  protected readonly eventos = signal<Evento[]>([]);

  constructor(
    private readonly educandoService: EducandoService,
    private readonly eventoService: EventoService,
    private readonly snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.educandoService.meuPerfil().subscribe((perfil) => this.perfil.set(perfil));
    this.carregarEventos();
  }

  inscrever(evento: Evento): void {
    this.eventoService.inscrever(evento.id).subscribe({
      next: () => {
        this.snackBar.open(`Inscrição confirmada em "${evento.titulo}"`, 'Fechar', { duration: 3000 });
        this.carregarEventos();
      },
      error: (err) => {
        const mensagem = err?.error?.message ?? 'Não foi possível concluir a inscrição.';
        this.snackBar.open(mensagem, 'Fechar', { duration: 4000 });
      },
    });
  }

  private carregarEventos(): void {
    this.eventoService.listar().subscribe((eventos) => this.eventos.set(eventos));
  }
}
