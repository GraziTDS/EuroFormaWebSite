import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/auth/role.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    loadComponent: () => import('./features/login/login').then((m) => m.Login),
  },
  {
    path: 'esqueci-senha',
    loadComponent: () => import('./features/login/esqueci-senha').then((m) => m.EsqueciSenha),
  },
  {
    path: 'redefinir-senha',
    loadComponent: () => import('./features/login/redefinir-senha').then((m) => m.RedefinirSenha),
  },
  {
    path: 'educando',
    loadComponent: () => import('./layout/shell/shell').then((m) => m.Shell),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['EDUCANDO'] },
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'home' },
      {
        path: 'home',
        loadComponent: () => import('./features/educando/home/educando-home').then((m) => m.EducandoHome),
      },
      {
        path: 'perfil',
        loadComponent: () =>
          import('./features/educando/perfil/educando-perfil').then((m) => m.EducandoPerfilPage),
      },
    ],
  },
  {
    path: 'educador',
    loadComponent: () => import('./layout/shell/shell').then((m) => m.Shell),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['EDUCADOR', 'COORDENADOR'] },
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/educador/dashboard/educador-dashboard').then((m) => m.EducadorDashboardPage),
      },
      {
        path: 'educandos',
        loadComponent: () =>
          import('./features/educador/lista/educando-lista').then((m) => m.EducandoLista),
        data: { titulo: 'Educandos' },
      },
      {
        path: 'educandos/:id',
        loadComponent: () =>
          import('./features/educador/detalhe/educando-detalhe').then((m) => m.EducandoDetalhe),
      },
      {
        path: 'cadastro',
        loadComponent: () =>
          import('./features/educador/cadastro/educador-cadastro').then((m) => m.EducadorCadastro),
      },
      {
        path: 'turmas',
        loadComponent: () => import('./features/turma/lista/turma-lista').then((m) => m.TurmaLista),
      },
      {
        path: 'turmas/:id',
        loadComponent: () => import('./features/turma/detalhe/turma-detalhe').then((m) => m.TurmaDetalhe),
      },
    ],
  },
  {
    path: 'admin',
    loadComponent: () => import('./layout/shell/shell').then((m) => m.Shell),
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMINISTRADOR'] },
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'visao-geral' },
      {
        path: 'visao-geral',
        loadComponent: () =>
          import('./features/admin/visao-geral/admin-visao-geral').then((m) => m.AdminVisaoGeral),
      },
      {
        path: 'usuarios',
        loadComponent: () => import('./features/admin/usuarios/admin-usuarios').then((m) => m.AdminUsuarios),
      },
      {
        path: 'educandos',
        loadComponent: () =>
          import('./features/educador/lista/educando-lista').then((m) => m.EducandoLista),
        data: { titulo: 'Educandos' },
      },
      {
        path: 'educandos/:id',
        loadComponent: () =>
          import('./features/educador/detalhe/educando-detalhe').then((m) => m.EducandoDetalhe),
      },
      {
        path: 'relatorios',
        loadComponent: () =>
          import('./features/admin/relatorios/admin-relatorios').then((m) => m.AdminRelatorios),
      },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
