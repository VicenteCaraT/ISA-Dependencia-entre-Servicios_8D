import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IImpactoPropagado, NewImpactoPropagado } from '../impacto-propagado.model';

export type PartialUpdateImpactoPropagado = Partial<IImpactoPropagado> & Pick<IImpactoPropagado, 'id'>;

type RestOf<T extends IImpactoPropagado | NewImpactoPropagado> = Omit<T, 'detectadoEn'> & {
  detectadoEn?: string | null;
};

export type RestImpactoPropagado = RestOf<IImpactoPropagado>;

export type NewRestImpactoPropagado = RestOf<NewImpactoPropagado>;

export type PartialUpdateRestImpactoPropagado = RestOf<PartialUpdateImpactoPropagado>;

@Injectable()
export class ImpactoPropagadosService {
  readonly impactoPropagadosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly impactoPropagadosResource = httpResource<RestImpactoPropagado[]>(() => {
    const params = this.impactoPropagadosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of impactoPropagado that have been fetched. It is updated when the impactoPropagadosResource emits a new value.
   * In case of error while fetching the impactoPropagados, the signal is set to an empty array.
   */
  readonly impactoPropagados = computed(() =>
    (this.impactoPropagadosResource.hasValue() ? this.impactoPropagadosResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/impacto-propagados');

  protected convertValueFromServer(restImpactoPropagado: RestImpactoPropagado): IImpactoPropagado {
    return {
      ...restImpactoPropagado,
      detectadoEn: restImpactoPropagado.detectadoEn ? dayjs(restImpactoPropagado.detectadoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class ImpactoPropagadoService extends ImpactoPropagadosService {
  protected readonly http = inject(HttpClient);

  create(impactoPropagado: NewImpactoPropagado): Observable<IImpactoPropagado> {
    const copy = this.convertValueFromClient(impactoPropagado);
    return this.http.post<RestImpactoPropagado>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(impactoPropagado: IImpactoPropagado): Observable<IImpactoPropagado> {
    const copy = this.convertValueFromClient(impactoPropagado);
    return this.http
      .put<RestImpactoPropagado>(`${this.resourceUrl}/${encodeURIComponent(this.getImpactoPropagadoIdentifier(impactoPropagado))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(impactoPropagado: PartialUpdateImpactoPropagado): Observable<IImpactoPropagado> {
    const copy = this.convertValueFromClient(impactoPropagado);
    return this.http
      .patch<RestImpactoPropagado>(`${this.resourceUrl}/${encodeURIComponent(this.getImpactoPropagadoIdentifier(impactoPropagado))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IImpactoPropagado> {
    return this.http
      .get<RestImpactoPropagado>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IImpactoPropagado[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImpactoPropagado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getImpactoPropagadoIdentifier(impactoPropagado: Pick<IImpactoPropagado, 'id'>): number {
    return impactoPropagado.id;
  }

  compareImpactoPropagado(o1: Pick<IImpactoPropagado, 'id'> | null, o2: Pick<IImpactoPropagado, 'id'> | null): boolean {
    return o1 && o2 ? this.getImpactoPropagadoIdentifier(o1) === this.getImpactoPropagadoIdentifier(o2) : o1 === o2;
  }

  addImpactoPropagadoToCollectionIfMissing<Type extends Pick<IImpactoPropagado, 'id'>>(
    impactoPropagadoCollection: Type[],
    ...impactoPropagadosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const impactoPropagados: Type[] = impactoPropagadosToCheck.filter(isPresent);
    if (impactoPropagados.length > 0) {
      const impactoPropagadoCollectionIdentifiers = impactoPropagadoCollection.map(impactoPropagadoItem =>
        this.getImpactoPropagadoIdentifier(impactoPropagadoItem),
      );
      const impactoPropagadosToAdd = impactoPropagados.filter(impactoPropagadoItem => {
        const impactoPropagadoIdentifier = this.getImpactoPropagadoIdentifier(impactoPropagadoItem);
        if (impactoPropagadoCollectionIdentifiers.includes(impactoPropagadoIdentifier)) {
          return false;
        }
        impactoPropagadoCollectionIdentifiers.push(impactoPropagadoIdentifier);
        return true;
      });
      return [...impactoPropagadosToAdd, ...impactoPropagadoCollection];
    }
    return impactoPropagadoCollection;
  }

  protected convertValueFromClient<T extends IImpactoPropagado | NewImpactoPropagado | PartialUpdateImpactoPropagado>(
    impactoPropagado: T,
  ): RestOf<T> {
    return {
      ...impactoPropagado,
      detectadoEn: impactoPropagado.detectadoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestImpactoPropagado): IImpactoPropagado {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestImpactoPropagado[]): IImpactoPropagado[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
