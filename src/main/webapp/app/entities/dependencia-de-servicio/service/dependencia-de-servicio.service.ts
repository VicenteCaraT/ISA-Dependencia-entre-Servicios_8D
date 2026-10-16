import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDependenciaDeServicio, NewDependenciaDeServicio } from '../dependencia-de-servicio.model';

export type PartialUpdateDependenciaDeServicio = Partial<IDependenciaDeServicio> & Pick<IDependenciaDeServicio, 'id'>;

@Injectable()
export class DependenciaDeServiciosService {
  readonly dependenciaDeServiciosParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly dependenciaDeServiciosResource = httpResource<IDependenciaDeServicio[]>(() => {
    const params = this.dependenciaDeServiciosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of dependenciaDeServicio that have been fetched. It is updated when the dependenciaDeServiciosResource emits a new value.
   * In case of error while fetching the dependenciaDeServicios, the signal is set to an empty array.
   */
  readonly dependenciaDeServicios = computed(() =>
    this.dependenciaDeServiciosResource.hasValue() ? this.dependenciaDeServiciosResource.value() : [],
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/dependencia-de-servicios');
}

@Injectable({ providedIn: 'root' })
export class DependenciaDeServicioService extends DependenciaDeServiciosService {
  protected readonly http = inject(HttpClient);

  create(dependenciaDeServicio: NewDependenciaDeServicio): Observable<IDependenciaDeServicio> {
    return this.http.post<IDependenciaDeServicio>(this.resourceUrl, dependenciaDeServicio);
  }

  update(dependenciaDeServicio: IDependenciaDeServicio): Observable<IDependenciaDeServicio> {
    return this.http.put<IDependenciaDeServicio>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDependenciaDeServicioIdentifier(dependenciaDeServicio))}`,
      dependenciaDeServicio,
    );
  }

  partialUpdate(dependenciaDeServicio: PartialUpdateDependenciaDeServicio): Observable<IDependenciaDeServicio> {
    return this.http.patch<IDependenciaDeServicio>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDependenciaDeServicioIdentifier(dependenciaDeServicio))}`,
      dependenciaDeServicio,
    );
  }

  find(id: number): Observable<IDependenciaDeServicio> {
    return this.http.get<IDependenciaDeServicio>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IDependenciaDeServicio[]>> {
    const options = createRequestOption(req);
    return this.http.get<IDependenciaDeServicio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDependenciaDeServicioIdentifier(dependenciaDeServicio: Pick<IDependenciaDeServicio, 'id'>): number {
    return dependenciaDeServicio.id;
  }

  compareDependenciaDeServicio(o1: Pick<IDependenciaDeServicio, 'id'> | null, o2: Pick<IDependenciaDeServicio, 'id'> | null): boolean {
    return o1 && o2 ? this.getDependenciaDeServicioIdentifier(o1) === this.getDependenciaDeServicioIdentifier(o2) : o1 === o2;
  }

  addDependenciaDeServicioToCollectionIfMissing<Type extends Pick<IDependenciaDeServicio, 'id'>>(
    dependenciaDeServicioCollection: Type[],
    ...dependenciaDeServiciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dependenciaDeServicios: Type[] = dependenciaDeServiciosToCheck.filter(isPresent);
    if (dependenciaDeServicios.length > 0) {
      const dependenciaDeServicioCollectionIdentifiers = dependenciaDeServicioCollection.map(dependenciaDeServicioItem =>
        this.getDependenciaDeServicioIdentifier(dependenciaDeServicioItem),
      );
      const dependenciaDeServiciosToAdd = dependenciaDeServicios.filter(dependenciaDeServicioItem => {
        const dependenciaDeServicioIdentifier = this.getDependenciaDeServicioIdentifier(dependenciaDeServicioItem);
        if (dependenciaDeServicioCollectionIdentifiers.includes(dependenciaDeServicioIdentifier)) {
          return false;
        }
        dependenciaDeServicioCollectionIdentifiers.push(dependenciaDeServicioIdentifier);
        return true;
      });
      return [...dependenciaDeServiciosToAdd, ...dependenciaDeServicioCollection];
    }
    return dependenciaDeServicioCollection;
  }
}
