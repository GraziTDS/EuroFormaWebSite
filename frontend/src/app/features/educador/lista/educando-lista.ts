import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { EducandoService } from '../../../core/services/educando.service';
import { EducandoResumo, StatusEducando } from '../../../core/models';
import { StatusChip } from '../../../shared/status-chip/status-chip';

const OPCOES_STATUS: { valor: StatusEducando | ''; rotulo: string }[] = [
  { valor: '', rotulo: 'Todos os status' },
  { valor: 'INSCRITO', rotulo: 'Inscrito' },
  { valor: 'EM_CURSO', rotulo: 'Em curso' },
  { valor: 'CONCLUIDO', rotulo: 'Concluído' },
  { valor: 'DESISTENTE', rotulo: 'Desistente' },
];

@Component({
  selector: 'app-educando-lista',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatProgressBarModule, StatusChip],
  templateUrl: './educando-lista.html',
  styleUrl: './educando-lista.scss',
})
export class EducandoLista implements OnInit {
  private readonly educandoService = inject(EducandoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly titulo = this.route.snapshot.data['titulo'] ?? 'Educandos';
  protected readonly opcoesStatus = OPCOES_STATUS;

  protected readonly busca = signal('');
  protected readonly statusSelecionado = signal<StatusEducando | ''>('');
  protected readonly educandos = signal<EducandoResumo[]>([]);

  protected readonly baseRota = computed(() => (this.router.url.startsWith('/admin') ? '/admin' : '/educador'));

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.educandoService.listar(this.busca(), this.statusSelecionado()).subscribe((lista) => this.educandos.set(lista));
  }

  abrir(id: number): void {
    this.router.navigate([this.baseRota(), 'educandos', id]);
  }
}
