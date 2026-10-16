import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';
import { DependenciaDeServicioService } from '../service/dependencia-de-servicio.service';

const dependenciaDeServicioResolve = (route: ActivatedRouteSnapshot): Observable<null | IDependenciaDeServicio> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(DependenciaDeServicioService);
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

export default dependenciaDeServicioResolve;
