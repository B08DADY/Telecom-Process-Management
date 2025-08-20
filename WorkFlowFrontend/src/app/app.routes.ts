// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { CreateWorkOrderComponent } from './components/create-work-order/create-work-order';

export const routes: Routes = [
  // When the user goes to the main URL, redirect them to '/create-work-order'
  { path: '', redirectTo: 'create-work-order', pathMatch: 'full' },

  // When the user goes to '/create-work-order', show our component
  { path: 'create-work-order', component: CreateWorkOrderComponent }
];