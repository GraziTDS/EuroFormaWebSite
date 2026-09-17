import { KeyValuePipe } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { AdminService } from '../../../core/services/admin.service';
import { EducadorService } from '../../../core/services/educador.service';
import { AdminVisaoGeral as AdminVisaoGeralModel, Educador } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';
import { StatusChip } from '../../../shared/status-chip/status-chip';
import { PALETA_GRAFICOS } from '../../../shared/chart-colors';

@Component({
  selector: 'app-admin-visao-geral',
  imports: [KeyValuePipe, RouterLink, BaseChartDirective, SectionCard, StatCard, StatusChip],
  templateUrl: './admin-visao-geral.html',
  styleUrl: './admin-visao-geral.scss',
})
export class AdminVisaoGeral implements OnInit {
  protected readonly dados = signal<AdminVisaoGeralModel | null>(null);
  protected readonly educadores = signal<Educador[]>([]);

  protected readonly maiorDistribuicao = computed(() => {
    const distribuicao = this.dados()?.distribuicaoPorStatus ?? {};
    return Math.max(1, ...Object.values(distribuicao));
  });

  protected readonly evolucaoData = computed<ChartConfiguration<'line'>['data']>(() => {
    const pontos = this.dados()?.evolucaoMensal ?? [];
    return {
      labels: pontos.map((p) => p.mes),
      datasets: [
        {
          data: pontos.map((p) => p.total),
          label: 'Novas matrículas',
          borderColor: PALETA_GRAFICOS[0],
          backgroundColor: 'rgba(11, 59, 140, 0.15)',
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

  protected readonly barData = computed<ChartConfiguration<'bar'>['data']>(() => {
    const porCurso = this.dados()?.educandosPorCurso ?? {};
    const chaves = Object.keys(porCurso);
    return {
      labels: chaves,
      datasets: [
        {
          data: chaves.map((k) => porCurso[k]),
          backgroundColor: PALETA_GRAFICOS[1],
          borderRadius: 4,
          maxBarThickness: 48,
        },
      ],
    };
  });

  protected readonly barOptions: ChartConfiguration<'bar'>['options'] = {
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
  };

  constructor(
    private readonly adminService: AdminService,
    private readonly educadorService: EducadorService,
  ) {}

  ngOnInit(): void {
    this.adminService.visaoGeral().subscribe((dados) => this.dados.set(dados));
    this.educadorService.listarUsuarios().subscribe((lista) => this.educadores.set(lista));
  }
}
