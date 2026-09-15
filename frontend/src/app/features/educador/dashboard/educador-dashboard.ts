import { KeyValuePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { EducadorService } from '../../../core/services/educador.service';
import { EducadorDashboard } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';
import { StatusChip } from '../../../shared/status-chip/status-chip';

@Component({
  selector: 'app-educador-dashboard',
  imports: [KeyValuePipe, RouterLink, MatButtonModule, SectionCard, StatCard, StatusChip],
  templateUrl: './educador-dashboard.html',
  styleUrl: './educador-dashboard.scss',
})
export class EducadorDashboardPage implements OnInit {
  protected readonly dados = signal<EducadorDashboard | null>(null);

  constructor(private readonly educadorService: EducadorService) {}

  ngOnInit(): void {
    this.educadorService.dashboard().subscribe((dados) => this.dados.set(dados));
  }
}
