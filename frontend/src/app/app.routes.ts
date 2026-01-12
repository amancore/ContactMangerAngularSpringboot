import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/contacts', pathMatch: 'full' },
  {
    path: 'contacts',
    loadComponent: () => import('./contacts/list/list').then((m) => m.List),
  },
  {
    path: 'contacts/new',
    loadComponent: () => import('./contacts/form/form').then((m) => m.Form),
  },
  {
    path: 'contacts/:id/edit',
    loadComponent: () => import('./contacts/form/form').then((m) => m.Form),
  },
  { path: '**', redirectTo: '/contacts' },
];
