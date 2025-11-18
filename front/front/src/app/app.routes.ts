import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/register/register').then(
        (m) => m.Register
      ),
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/parking/parking-dashboard.component/parking-dashboard.component').then(
        (m) => m.ParkingDashboardComponent
      ),
  },
  {
    path: 'parking-spaces/new',
    loadComponent: () =>
      import('./features/parking/parking-form.component/parking-form.component').then(
        (m) => m.ParkingFormComponent
      ),
  },

  {
    path: 'parking-space/:id',
    loadComponent: () =>
      import('./features/parking/parking-detail.component/parking-detail.component').then(
        (m) => m.ParkingDetailComponent
      ),
  },

  {
    path: 'reservas/mis-reservas',
    loadComponent: () =>
      import('./features/bookings/my-bookings.component/my-bookings.component').then(
        (m) => m.MyBookingsComponent
      ),

  },

  {
    path: 'account',
    loadComponent: () =>
      import('./features/account/account.component/account.component').then(
        (m) => m.AccountComponent
      ),
  },

  {
    path: '**',
    redirectTo: 'login',
  },


];
