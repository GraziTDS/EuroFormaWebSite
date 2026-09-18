import { Component, ElementRef, OnInit, ViewChild, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EducandoService } from '../../../core/services/educando.service';
import { EducandoResumo, StatusEducando } from '../../../core/models';
import { StatusChip } from '../../../shared/status-chip/status-chip';
import { baixarArquivo } from '../../../shared/download-file';

const OPCOES_STATUS: { valor: StatusEducando | ''; rotulo: string }[] = [
  { valor: '', rotulo: 'Todos os status' },
  { valor: 'INSCRITO', rotulo: 'Inscrito' },
  { valor: 'EM_CURSO', rotulo: 'Em curso' },
  { valor: 'CONCLUIDO', rotulo: 'Concluído' },
  { valor: 'DESISTENTE', rotulo: 'Desistente' },
];

@Component({
  selector: 'app-educando-lista',
  imports: [
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressBarModule,
    StatusChip,
  ],
  templateUrl: './educando-lista.html',
  styleUrl: './educando-lista.scss',
})
export class EducandoLista implements OnInit {
  @ViewChild('inputImportar') inputImportar?: ElementRef<HTMLInputElement>;

  private readonly educandoService = inject(EducandoService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly titulo = this.route.snapshot.data['titulo'] ?? 'Educandos';
  protected readonly opcoesStatus = OPCOES_STATUS;

  protected readonly busca = signal('');
  protected readonly statusSelecionado = signal<StatusEducando | ''>('');
  protected readonly educandos = signal<EducandoResumo[]>([]);
  protected readonly importando = signal(false);

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

  exportarExcel(): void {
    this.educandoService.exportarExcel().subscribe((blob) => baixarArquivo(blob, 'educandos.xlsx'));
  }

  baixarModelo(): void {
    this.educandoService
      .baixarModeloImportacao()
      .subscribe((blob) => baixarArquivo(blob, 'modelo-importacao-educandos.xlsx'));
  }

  abrirSeletorImportacao(): void {
    this.inputImportar?.nativeElement.click();
  }

  aoSelecionarArquivoImportacao(event: Event): void {
    const arquivo = (event.target as HTMLInputElement).files?.[0];
    if (!arquivo) {
      return;
    }
    this.importando.set(true);
    this.educandoService.importarExcel(arquivo).subscribe({
      next: (resultado) => {
        this.importando.set(false);
        const mensagem = `${resultado.criados} educando(s) importado(s).` +
          (resultado.erros.length > 0 ? ` ${resultado.erros.length} linha(s) com erro.` : '');
        this.snackBar.open(mensagem, 'Fechar', { duration: 5000 });
        this.buscar();
      },
      error: (err) => {
        this.importando.set(false);
        this.snackBar.open(err?.error?.message ?? 'Falha ao importar planilha.', 'Fechar', { duration: 4000 });
      },
    });
  }
}
