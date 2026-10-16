import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ImpactoPropagadoDeleteDialog } from '../delete/impacto-propagado-delete-dialog';
import { IImpactoPropagado } from '../impacto-propagado.model';
import { ImpactoPropagadoService } from '../service/impacto-propagado.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-impacto-propagado',
  templateUrl: './impacto-propagado.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    FormatMediumDatetimePipe,
  ],
})
export class ImpactoPropagado implements OnInit {
  subscription: Subscription | null = null;
  readonly impactoPropagados = signal<IImpactoPropagado[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly impactoPropagadoService = inject(ImpactoPropagadoService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.impactoPropagadoService.impactoPropagadosResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.impactoPropagados.set(this.fillComponentAttributesFromResponseBody([...this.impactoPropagadoService.impactoPropagados()]));
    });
  }

  trackId = (item: IImpactoPropagado): number => this.impactoPropagadoService.getImpactoPropagadoIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.impactoPropagados().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(impactoPropagado: IImpactoPropagado): void {
    const modalRef = this.modalService.open(ImpactoPropagadoDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.impactoPropagado = impactoPropagado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IImpactoPropagado[]): IImpactoPropagado[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IImpactoPropagado[]): IImpactoPropagado[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.impactoPropagadoService.impactoPropagadosParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
