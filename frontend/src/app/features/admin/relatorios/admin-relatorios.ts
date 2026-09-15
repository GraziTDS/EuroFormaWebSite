import { Component, OnInit, signal } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { RelatorioService } from '../../../core/services/relatorio.service';
import { RelatorioResponse, TipoRelatorio } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';

const TIPOS: { valor: TipoRelatorio; rotulo: string }[] = [
  { valor: 'MATRICULAS_POR_CURSO', rotulo: 'Matrículas por curso' },
  { valor: 'FREQUENCIA_POR_EDUCANDO', rotulo: 'Frequência por educando' },
  { valor: 'CONCLUSAO_EVASAO', rotulo: 'Conclusão & evasão' },
  { valor: 'MOTIVOS_DESISTENCIA', rotulo: 'Motivos de desistência' },
];

@Component({
  selector: 'app-admin-relatorios',
  imports: [MatButtonToggleModule, SectionCard, StatCard],
  templateUrl: './admin-relatorios.html',
  styleUrl: './admin-relatorios.scss',
})
export class AdminRelatorios implements OnInit {
  protected readonly tipos = TIPOS;
  protected readonly tipoSelecionado = signal<TipoRelatorio>('MATRICULAS_POR_CURSO');
  protected readonly relatorio = signal<RelatorioResponse | null>(null);

  constructor(private readonly relatorioService: RelatorioService) {}

  ngOnInit(): void {
    this.gerar();
  }

  selecionar(tipo: TipoRelatorio): void {
    this.tipoSelecionado.set(tipo);
    this.gerar();
  }

  private gerar(): void {
    this.relatorioService.gerar(this.tipoSelecionado()).subscribe((resposta) => this.relatorio.set(resposta));
  }
}
