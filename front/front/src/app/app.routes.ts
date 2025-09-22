import { Routes } from '@angular/router';
import { Login } from './features/login/login';
Login

export const routes: Routes = [

  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', component: Login  },
  { path: '**', redirectTo: 'login' }
];
