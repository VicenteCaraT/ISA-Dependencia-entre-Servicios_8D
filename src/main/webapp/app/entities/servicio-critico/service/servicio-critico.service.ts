import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IServicioCritico, NewServicioCritico } from '../servicio-critico.model';

export type PartialUpdateServicioCritico = Partial<IServicioCritico> & Pick<IServicioCritico, 'id'>;

@Injectable()
export class ServicioCriticosService {
  readonly servicioCriticosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly servicioCriticosResource = httpResource<IServicioCritico[]>(() => {
    const params = this.servicioCriticosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of servicioCritico that have been fetched. It is updated when the servicioCriticosResource emits a new value.
   * In case of error while fetching the servicioCriticos, the signal is set to an empty array.
   */
  readonly servicioCriticos = computed(() => (this.servicioCriticosResource.hasValue() ? this.servicioCriticosResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/servicio-criticos');
}

@Injectable({ providedIn: 'root' })
export class ServicioCriticoService extends ServicioCriticosService {
  protected readonly http = inject(HttpClient);

  create(servicioCritico: NewServicioCritico): Observable<IServicioCritico> {
    return this.http.post<IServicioCritico>(this.resourceUrl, servicioCritico);
  }

  update(servicioCritico: IServicioCritico): Observable<IServicioCritico> {
    return this.http.put<IServicioCritico>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioCriticoIdentifier(servicioCritico))}`,
      servicioCritico,
    );
  }

  partialUpdate(servicioCritico: PartialUpdateServicioCritico): Observable<IServicioCritico> {
    return this.http.patch<IServicioCritico>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioCriticoIdentifier(servicioCritico))}`,
      servicioCritico,
    );
  }

  find(id: number): Observable<IServicioCritico> {
    return this.http.get<IServicioCritico>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IServicioCritico[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServicioCritico[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServicioCriticoIdentifier(servicioCritico: Pick<IServicioCritico, 'id'>): number {
    return servicioCritico.id;
  }

  compareServicioCritico(o1: Pick<IServicioCritico, 'id'> | null, o2: Pick<IServicioCritico, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicioCriticoIdentifier(o1) === this.getServicioCriticoIdentifier(o2) : o1 === o2;
  }

  addServicioCriticoToCollectionIfMissing<Type extends Pick<IServicioCritico, 'id'>>(
    servicioCriticoCollection: Type[],
    ...servicioCriticosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicioCriticos: Type[] = servicioCriticosToCheck.filter(isPresent);
    if (servicioCriticos.length > 0) {
      const servicioCriticoCollectionIdentifiers = servicioCriticoCollection.map(servicioCriticoItem =>
        this.getServicioCriticoIdentifier(servicioCriticoItem),
      );
      const servicioCriticosToAdd = servicioCriticos.filter(servicioCriticoItem => {
        const servicioCriticoIdentifier = this.getServicioCriticoIdentifier(servicioCriticoItem);
        if (servicioCriticoCollectionIdentifiers.includes(servicioCriticoIdentifier)) {
          return false;
        }
        servicioCriticoCollectionIdentifiers.push(servicioCriticoIdentifier);
        return true;
      });
      return [...servicioCriticosToAdd, ...servicioCriticoCollection];
    }
    return servicioCriticoCollection;
  }
}
