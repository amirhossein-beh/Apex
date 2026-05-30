import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('././features/auth/login/login')
      .then(m => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./features/layout/layout')
      .then(m => m.Layout),
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard')
          .then(m => m.Dashboard)
      },
      {
        path: 'clients',
        loadComponent: () => import('./features/clients/clients')
          .then(m => m.Clients)
      },
      {
        path: 'versions',
        loadComponent: () => import('./features/versions/versions')
          .then(m => m.Versions)
      },
      {
        path: 'traffic',
        loadComponent: () => import('./features/traffic/traffic')
          .then(m => m.Traffic)
      },
      {
        path: 'users',
        loadComponent: () => import('./features/users/users')
          .then(m => m.Users)
      },
      {
        path: 'deployments',
        loadComponent: () => import('./features/deployments.component/deployments.component')
          .then(m => m.DeploymentsComponent)
      }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
