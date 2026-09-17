import { KeyValuePipe } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { EducadorService } from '../../../core/services/educador.service';
import { EducadorDashboard } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';
import { StatusChip } from '../../../shared/status-chip/status-chip';
import { PALETA_GRAFICOS } from '../../../shared/chart-colors';

@Component({
  selector: 'app-educador-dashboard',
  imports: [KeyValuePipe, RouterLink, MatButtonModule, BaseChartDirective, SectionCard, StatCard, StatusChip],
  templateUrl: './educador-dashboard.html',
  styleUrl: './educador-dashboard.scss',
})
export class EducadorDashboardPage implements OnInit {
  protected readonly dados = signal<EducadorDashboard | null>(null);

  protected readonly evolucaoData = computed<ChartConfiguration<'line'>['data']>(() => {
    const pontos = this.dados()?.evolucaoMensal ?? [];
    return {
      labels: pontos.map((p) => p.mes),
      datasets: [
        {
          data: pontos.map((p) => p.total),
          label: 'Novas matrículas',
          borderColor: PALETA_GRAFICOS[1],
          backgroundColor: 'rgba(18, 71, 166, 0.15)',
          fill: true,
          tension: 0.35,
          pointRadius: 3,
        },
      ],
    };
  });

  protected readonly evolucaoOptions: ChartConfiguration<'line'>['options'] = {
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
  };

  protected readonly maiorDistribuicao = computed(() => {
    const distribuicao = this.dados()?.distribuicaoPorStatus ?? {};
    return Math.max(1, ...Object.values(distribuicao));
  });

  constructor(private readonly educadorService: EducadorService) {}

  ngOnInit(): void {
    this.educadorService.dashboard().subscribe((dados) => this.dados.set(dados));
  }
}
