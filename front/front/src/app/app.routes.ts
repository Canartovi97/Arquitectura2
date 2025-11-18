import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: 'login',
    // features/login/login.component.ts
    loadComponent: () =>
      import('./features/login/login').then(
        (m) => m.Login
      ),
  },
  {
    path: 'register',
    // features/register/register.component.ts
    loadComponent: () =>
      import('./features/register/register').then(
        (m) => m.Register
      ),
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
