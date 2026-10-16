import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ServicioCriticoService } from '../service/servicio-critico.service';
import { IServicioCritico } from '../servicio-critico.model';

const servicioCriticoResolve = (route: ActivatedRouteSnapshot): Observable<null | IServicioCritico> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ServicioCriticoService);
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

export default servicioCriticoResolve;
