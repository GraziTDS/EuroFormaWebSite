import { Component, OnInit, computed, signal } from '@angular/core';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { RelatorioService } from '../../../core/services/relatorio.service';
import { RelatorioResponse, TipoRelatorio } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';
import { PALETA_GRAFICOS } from '../../../shared/chart-colors';
import { baixarArquivo } from '../../../shared/download-file';

const TIPOS: { valor: TipoRelatorio; rotulo: string }[] = [
  { valor: 'MATRICULAS_POR_CURSO', rotulo: 'Matrículas por curso' },
  { valor: 'FREQUENCIA_POR_EDUCANDO', rotulo: 'Frequência por educando' },
  { valor: 'CONCLUSAO_EVASAO', rotulo: 'Conclusão & evasão' },
  { valor: 'MOTIVOS_DESISTENCIA', rotulo: 'Motivos de desistência' },
];

@Component({
  selector: 'app-admin-relatorios',
  imports: [BaseChartDirective, MatButtonModule, MatButtonToggleModule, SectionCard, StatCard],
  templateUrl: './admin-relatorios.html',
  styleUrl: './admin-relatorios.scss',
})
export class AdminRelatorios implements OnInit {
  protected readonly tipos = TIPOS;
  protected readonly tipoSelecionado = signal<TipoRelatorio>('MATRICULAS_POR_CURSO');
  protected readonly relatorio = signal<RelatorioResponse | null>(null);

  protected readonly barMatriculasData = computed<ChartConfiguration<'bar'>['data']>(() => {
    const itens = this.relatorio()?.matriculasPorCurso ?? [];
    return {
      labels: itens.map((i) => i.chave),
      datasets: [{ data: itens.map((i) => i.total), backgroundColor: PALETA_GRAFICOS[1], borderRadius: 4 }],
    };
  });

  protected readonly pieMatriculasData = computed<ChartConfiguration<'pie'>['data']>(() => {
    const itens = this.relatorio()?.matriculasPorCurso ?? [];
    return {
      labels: itens.map((i) => i.chave),
      datasets: [{ data: itens.map((i) => i.total), backgroundColor: PALETA_GRAFICOS, borderWidth: 0 }],
    };
  });

  protected readonly barFrequenciaData = computed<ChartConfiguration<'bar'>['data']>(() => {
    const itens = this.relatorio()?.frequenciaPorEducando ?? [];
    return {
      labels: itens.map((i) => i.nome),
      datasets: [{ data: itens.map((i) => i.frequencia), backgroundColor: PALETA_GRAFICOS[2], borderRadius: 4 }],
    };
  });

  protected readonly barFrequenciaOptions: ChartConfiguration<'bar'>['options'] = {
    indexAxis: 'y',
    plugins: { legend: { display: false } },
    scales: { x: { beginAtZero: true, max: 100 } },
  };

  protected readonly barSimplesOptions: ChartConfiguration<'bar'>['options'] = {
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
  };

  protected readonly pieOptions: ChartConfiguration<'pie'>['options'] = {
    plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 11 } } } },
  };

  protected readonly barEvasaoData = computed<ChartConfiguration<'bar'>['data']>(() => {
    const r = this.relatorio();
    if (!r) {
      return { labels: [], datasets: [] };
    }
    return {
      labels: ['Ativos', 'Concluídos', 'Desistentes'],
      datasets: [
        {
          data: [r.ativos, r.concluidos, r.desistentes],
          backgroundColor: [PALETA_GRAFICOS[1], PALETA_GRAFICOS[2], '#d64545'],
          borderRadius: 4,
        },
      ],
    };
  });

  constructor(private readonly relatorioService: RelatorioService) {}

  ngOnInit(): void {
    this.gerar();
  }

  selecionar(tipo: TipoRelatorio): void {
    this.tipoSelecionado.set(tipo);
    this.gerar();
  }

  exportarExcel(): void {
    this.relatorioService.exportarExcel(this.tipoSelecionado()).subscribe((blob) => {
      baixarArquivo(blob, `relatorio-${this.tipoSelecionado().toLowerCase()}.xlsx`);
    });
  }

  private gerar(): void {
    this.relatorioService.gerar(this.tipoSelecionado()).subscribe((resposta) => this.relatorio.set(resposta));
  }
}
