import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { TurmaService } from '../../../core/services/turma.service';
import { Turma } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';

@Component({
  selector: 'app-turma-lista',
  imports: [SectionCard],
  templateUrl: './turma-lista.html',
  styleUrl: './turma-lista.scss',
})
export class TurmaLista implements OnInit {
  protected readonly turmas = signal<Turma[]>([]);

  constructor(
    private readonly turmaService: TurmaService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.turmaService.listar().subscribe((turmas) => this.turmas.set(turmas));
  }

  abrir(turma: Turma): void {
    const base = this.router.url.startsWith('/admin') ? '/admin' : '/educador';
    this.router.navigate([base, 'turmas', turma.id]);
  }
}
