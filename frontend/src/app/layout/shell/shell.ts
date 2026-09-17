import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../core/auth/auth.service';
import { Role } from '../../core/models';
import { AssistenteCurriculo } from '../../features/educando/assistente/assistente-curriculo';

interface ItemNav {
  rotulo: string;
  caminho: string;
}

const NAV_POR_GRUPO: Record<'educando' | 'educador' | 'admin', ItemNav[]> = {
  educando: [
    { rotulo: 'Início', caminho: 'home' },
    { rotulo: 'Meu perfil', caminho: 'perfil' },
  ],
  educador: [
    { rotulo: 'Dashboard', caminho: 'dashboard' },
    { rotulo: 'Educandos', caminho: 'educandos' },
    { rotulo: 'Turmas', caminho: 'turmas' },
    { rotulo: 'Cadastrar', caminho: 'cadastro' },
  ],
  admin: [
    { rotulo: 'Visão geral', caminho: 'visao-geral' },
    { rotulo: 'Usuários', caminho: 'usuarios' },
    { rotulo: 'Educandos', caminho: 'educandos' },
    { rotulo: 'Relatórios', caminho: 'relatorios' },
  ],
};

const ROTULO_PERFIL: Record<Role, string> = {
  EDUCANDO: 'Educando',
  EDUCADOR: 'Educador',
  COORDENADOR: 'Coordenador',
  ADMINISTRADOR: 'Administrador',
};

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatButtonModule, MatIconModule, AssistenteCurriculo],
  templateUrl: './shell.html',
  styleUrl: './shell.scss',
})
export class Shell {
  private readonly authService = inject(AuthService);

  protected readonly usuario = this.authService.usuarioAtual;

  protected readonly itensNav = computed<ItemNav[]>(() => {
    const grupo = this.authService.grupoDeRotas(this.authService.role());
    return grupo ? NAV_POR_GRUPO[grupo] : [];
  });

  protected readonly rotuloPerfil = computed(() => {
    const role = this.authService.role();
    return role ? ROTULO_PERFIL[role] : '';
  });

  protected readonly ehEducando = computed(() => this.authService.role() === 'EDUCANDO');

  sair(): void {
    this.authService.logout();
  }
}
