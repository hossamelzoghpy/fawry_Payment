// guards/user.guard.ts
import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ErrorHandlerService } from '../common/service/errorhandler.service';

export const userGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const errorHandler = inject(ErrorHandlerService);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.isUser()) {
    return true;
  }

  // User is logged in but doesn't have user role
  errorHandler.handleWarning('Access denied. This page is for users only.');
  router.navigate(['/gateways']);
  return false;
};