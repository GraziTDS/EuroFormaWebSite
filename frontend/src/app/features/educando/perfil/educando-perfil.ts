import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EducandoService } from '../../../core/services/educando.service';
import { EducandoPerfil } from '../../../core/models';
import { SectionCard } from '../../../shared/section-card/section-card';

@Component({
  selector: 'app-educando-perfil',
  imports: [DatePipe, MatButtonModule, SectionCard],
  templateUrl: './educando-perfil.html',
  styleUrl: './educando-perfil.scss',
})
export class EducandoPerfilPage implements OnInit {
  @ViewChild('inputArquivo') inputArquivo?: ElementRef<HTMLInputElement>;

  protected readonly perfil = signal<EducandoPerfil | null>(null);
  protected readonly enviando = signal(false);

  constructor(
    private readonly educandoService: EducandoService,
    private readonly snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  abrirSeletorArquivo(): void {
    this.inputArquivo?.nativeElement.click();
  }

  aoSelecionarArquivo(event: Event): void {
    const arquivo = (event.target as HTMLInputElement).files?.[0];
    if (!arquivo) {
      return;
    }
    this.enviando.set(true);
    this.educandoService.enviarMeuCurriculo(arquivo).subscribe({
      next: () => {
        this.enviando.set(false);
        this.snackBar.open('Currículo enviado com sucesso!', 'Fechar', { duration: 3000 });
        this.carregar();
      },
      error: (err) => {
        this.enviando.set(false);
        this.snackBar.open(err?.error?.message ?? 'Falha ao enviar o currículo.', 'Fechar', { duration: 4000 });
      },
    });
  }

  private carregar(): void {
    this.educandoService.meuPerfil().subscribe((perfil) => this.perfil.set(perfil));
  }
}
