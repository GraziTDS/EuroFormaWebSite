import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Role } from '../models';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.autenticado()) {
    return true;
  }
  return router.createUrlTree(['/login']);
};

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const rolesPermitidos = route.data['roles'] as Role[] | undefined;
  const roleAtual = authService.role();

  if (!roleAtual) {
    return router.createUrlTree(['/login']);
  }
  if (rolesPermitidos && !rolesPermitidos.includes(roleAtual)) {
    return router.createUrlTree(['/', authService.grupoDeRotas(roleAtual) ?? 'login']);
  }
  return true;
};
