import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { EducadorService } from '../../../core/services/educador.service';
import { Educador } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';

@Component({
  selector: 'app-admin-usuarios',
  imports: [DatePipe, MatSlideToggleModule, SectionCard],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.scss',
})
export class AdminUsuarios implements OnInit {
  protected readonly educadores = signal<Educador[]>([]);

  constructor(private readonly educadorService: EducadorService) {}

  ngOnInit(): void {
    this.carregar();
  }

  alternarAtivo(educador: Educador): void {
    this.educadorService.atualizarAtivo(educador.id, !educador.ativo).subscribe(() => this.carregar());
  }

  private carregar(): void {
    this.educadorService.listarUsuarios().subscribe((lista) => this.educadores.set(lista));
  }
}
