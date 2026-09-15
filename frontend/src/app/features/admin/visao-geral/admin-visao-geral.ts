import { KeyValuePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { AdminService } from '../../../core/services/admin.service';
import { AdminVisaoGeral as AdminVisaoGeralModel } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatCard } from '../../../shared/stat-card/stat-card';
import { StatusChip } from '../../../shared/status-chip/status-chip';

@Component({
  selector: 'app-admin-visao-geral',
  imports: [KeyValuePipe, SectionCard, StatCard, StatusChip],
  templateUrl: './admin-visao-geral.html',
  styleUrl: './admin-visao-geral.scss',
})
export class AdminVisaoGeral implements OnInit {
  protected readonly dados = signal<AdminVisaoGeralModel | null>(null);

  constructor(private readonly adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.visaoGeral().subscribe((dados) => this.dados.set(dados));
  }
}
