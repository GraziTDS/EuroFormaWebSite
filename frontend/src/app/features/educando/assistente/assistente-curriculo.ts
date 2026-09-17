import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AssistenteService, MensagemChat } from '../../../core/services/assistente.service';

@Component({
  selector: 'app-assistente-curriculo',
  imports: [FormsModule, MatButtonModule, MatFormFieldModule, MatIconModule, MatInputModule, MatProgressSpinnerModule],
  templateUrl: './assistente-curriculo.html',
  styleUrl: './assistente-curriculo.scss',
})
export class AssistenteCurriculo implements OnInit {
  private readonly assistenteService = inject(AssistenteService);

  protected readonly aberto = signal(false);
  protected readonly disponivel = signal(true);
  protected readonly mensagens = signal<MensagemChat[]>([
    {
      role: 'assistant',
      content:
        'Olá! Sou o assistente de currículo do euroForma. Me conte um pouco sobre suas experiências e o que ' +
        'você quer melhorar no seu currículo, que eu te ajudo com sugestões práticas.',
    },
  ]);
  protected readonly enviando = signal(false);
  protected rascunho = '';

  ngOnInit(): void {
    this.assistenteService.disponivel().subscribe({
      next: (r) => this.disponivel.set(r.disponivel),
      error: () => this.disponivel.set(false),
    });
  }

  alternar(): void {
    this.aberto.set(!this.aberto());
  }

  fechar(): void {
    this.aberto.set(false);
  }

  enviar(): void {
    const texto = this.rascunho.trim();
    if (!texto || this.enviando()) {
      return;
    }

    this.mensagens.update((lista) => [...lista, { role: 'user', content: texto }]);
    this.rascunho = '';
    this.enviando.set(true);

    this.assistenteService.chat(this.mensagens()).subscribe({
      next: (resposta) => {
        this.mensagens.update((lista) => [...lista, { role: 'assistant', content: resposta.resposta }]);
        this.enviando.set(false);
      },
      error: () => {
        this.mensagens.update((lista) => [
          ...lista,
          { role: 'assistant', content: 'Desculpe, não consegui responder agora. Tente novamente em instantes.' },
        ]);
        this.enviando.set(false);
      },
    });
  }
}
