import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ImpactoPropagadoResolve from './route/impacto-propagado-routing-resolve.service';

const impactoPropagadoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/impacto-propagado').then(m => m.ImpactoPropagado),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/impacto-propagado-detail').then(m => m.ImpactoPropagadoDetail),
    resolve: {
      impactoPropagado: ImpactoPropagadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/impacto-propagado-update').then(m => m.ImpactoPropagadoUpdate),
    resolve: {
      impactoPropagado: ImpactoPropagadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/impacto-propagado-update').then(m => m.ImpactoPropagadoUpdate),
    resolve: {
      impactoPropagado: ImpactoPropagadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default impactoPropagadoRoute;
