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

  protected readonly urlTesteIngles =
    'https://preply.com/pt/teste-de-lingua/ingles?campaignid=23823967331&network=g&adgroupid=200095096790&keyword=&matchtype=a&creative=807907955664&targetid=kwl-3500001&placement=&loc_physical_ms=1031549&device=c&utm_source=google&utm_medium=cpc&utm_term=&utm_campaign=stu_sem_generic_web_0_por_br_multiplesub_ex-nls_0_exs&hsa_acc=3694996243&hsa_cam=23823967331&hsa_grp=200095096790&hsa_ad=807907955664&hsa_src=g&hsa_tgt=kwl-3500001&hsa_kw=&hsa_mt=a&hsa_net=adwords&hsa_ver=3&gad_source=1&gad_campaignid=23823967331&gbraid=0AAAAADcbD208II17nM3OtFthqnct8RNfj&gclid=CjwKCAjwn67VBhBnEiwAXUIN1dc4t4GqgAZ4NZEuQjN7xkIwiVYskhl09SMf_t_X194osedEzJX03RoCDHwQAvD_BwE';

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
