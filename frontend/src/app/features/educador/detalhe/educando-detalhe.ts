import { DatePipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/auth/auth.service';
import { EducandoService } from '../../../core/services/educando.service';
import { Auditoria, EducandoPerfil, StatusEducando } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';
import { StatusChip } from '../../../shared/status-chip/status-chip';

const OPCOES_STATUS: { valor: StatusEducando; rotulo: string }[] = [
  { valor: 'INSCRITO', rotulo: 'Inscrito' },
  { valor: 'EM_CURSO', rotulo: 'Em curso' },
  { valor: 'CONCLUIDO', rotulo: 'Concluído' },
  { valor: 'DESISTENTE', rotulo: 'Desistente' },
];

@Component({
  selector: 'app-educando-detalhe',
  imports: [
    DatePipe,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatSliderModule,
    SectionCard,
    StatusChip,
  ],
  templateUrl: './educando-detalhe.html',
  styleUrl: './educando-detalhe.scss',
})
export class EducandoDetalhe implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly educandoService = inject(EducandoService);
  private readonly authService = inject(AuthService);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly opcoesStatus = OPCOES_STATUS;

  // Sprint 4: gerir status/frequência do aluno (e ver o histórico de auditoria) passou a ser
  // atribuição exclusiva do Administrador — Educador/Coordenador só acompanham em modo leitura.
  protected readonly podeAdministrar = computed(() => this.authService.role() === 'ADMINISTRADOR');

  protected readonly educandoId = Number(this.route.snapshot.paramMap.get('id'));
  protected readonly perfil = signal<EducandoPerfil | null>(null);
  protected readonly auditoria = signal<Auditoria[]>([]);

  protected readonly statusSelecionado = signal<StatusEducando>('INSCRITO');
  protected readonly frequenciaSelecionada = signal(0);
  protected readonly motivoDesistencia = signal('');
  protected readonly salvando = signal(false);

  ngOnInit(): void {
    this.carregar();
  }

  salvar(): void {
    this.salvando.set(true);
    this.educandoService
      .atualizarStatus(this.educandoId, this.statusSelecionado(), this.motivoDesistencia() || undefined)
      .subscribe(() => {
        this.educandoService.atualizarFrequencia(this.educandoId, this.frequenciaSelecionada()).subscribe(() => {
          this.salvando.set(false);
          this.snackBar.open('Alterações registradas com auditoria.', 'Fechar', { duration: 3000 });
          this.carregar();
        });
      });
  }

  private carregar(): void {
    this.educandoService.obter(this.educandoId).subscribe((perfil) => {
      this.perfil.set(perfil);
      this.statusSelecionado.set(perfil.status);
      this.frequenciaSelecionada.set(perfil.frequencia);
      this.motivoDesistencia.set(perfil.motivoDesistencia ?? '');
    });
    if (this.podeAdministrar()) {
      this.educandoService.auditoria(this.educandoId).subscribe((lista) => this.auditoria.set(lista));
    }
  }
}
