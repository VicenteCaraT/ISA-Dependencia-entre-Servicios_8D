import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DependenciaDeServicioResolve from './route/dependencia-de-servicio-routing-resolve.service';

const dependenciaDeServicioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dependencia-de-servicio').then(m => m.DependenciaDeServicio),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dependencia-de-servicio-detail').then(m => m.DependenciaDeServicioDetail),
    resolve: {
      dependenciaDeServicio: DependenciaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dependencia-de-servicio-update').then(m => m.DependenciaDeServicioUpdate),
    resolve: {
      dependenciaDeServicio: DependenciaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dependencia-de-servicio-update').then(m => m.DependenciaDeServicioUpdate),
    resolve: {
      dependenciaDeServicio: DependenciaDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dependenciaDeServicioRoute;
