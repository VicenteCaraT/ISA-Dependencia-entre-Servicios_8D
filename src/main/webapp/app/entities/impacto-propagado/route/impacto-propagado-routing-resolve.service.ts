import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IImpactoPropagado } from '../impacto-propagado.model';
import { ImpactoPropagadoService } from '../service/impacto-propagado.service';

const impactoPropagadoResolve = (route: ActivatedRouteSnapshot): Observable<null | IImpactoPropagado> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ImpactoPropagadoService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default impactoPropagadoResolve;
