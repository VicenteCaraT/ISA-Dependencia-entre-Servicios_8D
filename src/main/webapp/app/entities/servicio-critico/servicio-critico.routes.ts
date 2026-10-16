import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ServicioCriticoResolve from './route/servicio-critico-routing-resolve.service';

const servicioCriticoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/servicio-critico').then(m => m.ServicioCritico),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/servicio-critico-detail').then(m => m.ServicioCriticoDetail),
    resolve: {
      servicioCritico: ServicioCriticoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/servicio-critico-update').then(m => m.ServicioCriticoUpdate),
    resolve: {
      servicioCritico: ServicioCriticoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/servicio-critico-update').then(m => m.ServicioCriticoUpdate),
    resolve: {
      servicioCritico: ServicioCriticoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default servicioCriticoRoute;
