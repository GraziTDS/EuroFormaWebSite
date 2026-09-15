import { Component, computed, input } from '@angular/core';
import { StatusEducando } from '../../core/models';

const ROTULOS: Record<StatusEducando, string> = {
  INSCRITO: 'Inscrito',
  EM_CURSO: 'Em curso',
  CONCLUIDO: 'Concluído',
  DESISTENTE: 'Desistente',
};

@Component({
  selector: 'app-status-chip',
  templateUrl: './status-chip.html',
  styleUrl: './status-chip.scss',
})
export class StatusChip {
  readonly status = input.required<StatusEducando>();
  protected readonly rotulo = computed(() => ROTULOS[this.status()]);
}
