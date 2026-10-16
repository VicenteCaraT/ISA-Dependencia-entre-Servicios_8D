import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IDependenciaDeServicio } from 'app/entities/dependencia-de-servicio/dependencia-de-servicio.model';
import { DependenciaDeServicioService } from 'app/entities/dependencia-de-servicio/service/dependencia-de-servicio.service';
import { EstadoImpacto } from 'app/entities/enumerations/estado-impacto.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IImpactoPropagado } from '../impacto-propagado.model';
import { ImpactoPropagadoService } from '../service/impacto-propagado.service';

import { ImpactoPropagadoFormGroup, ImpactoPropagadoFormService } from './impacto-propagado-form.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-impacto-propagado-update',
  templateUrl: './impacto-propagado-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ImpactoPropagadoUpdate implements OnInit {
  readonly isSaving = signal(false);
  impactoPropagado: IImpactoPropagado | null = null;
  estadoImpactoValues = Object.keys(EstadoImpacto);

  incidentesSharedCollection = signal<IIncidente[]>([]);
  serviciosSharedCollection = signal<IServicio[]>([]);
  dependenciaDeServiciosSharedCollection = signal<IDependenciaDeServicio[]>([]);

  protected impactoPropagadoService = inject(ImpactoPropagadoService);
  protected impactoPropagadoFormService = inject(ImpactoPropagadoFormService);
  protected incidenteService = inject(IncidenteService);
  protected servicioService = inject(ServicioService);
  protected dependenciaDeServicioService = inject(DependenciaDeServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImpactoPropagadoFormGroup = this.impactoPropagadoFormService.createImpactoPropagadoFormGroup();

  compareIncidente = (o1: IIncidente | null, o2: IIncidente | null): boolean => this.incidenteService.compareIncidente(o1, o2);

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  compareDependenciaDeServicio = (o1: IDependenciaDeServicio | null, o2: IDependenciaDeServicio | null): boolean =>
    this.dependenciaDeServicioService.compareDependenciaDeServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ impactoPropagado }) => {
      this.impactoPropagado = impactoPropagado;
      if (impactoPropagado) {
        this.updateForm(impactoPropagado);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const impactoPropagado = this.impactoPropagadoFormService.getImpactoPropagado(this.editForm);
    if (impactoPropagado.id === null) {
      this.subscribeToSaveResponse(this.impactoPropagadoService.create(impactoPropagado));
    } else {
      this.subscribeToSaveResponse(this.impactoPropagadoService.update(impactoPropagado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IImpactoPropagado | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(impactoPropagado: IImpactoPropagado): void {
    this.impactoPropagado = impactoPropagado;
    this.impactoPropagadoFormService.resetForm(this.editForm, impactoPropagado);

    this.incidentesSharedCollection.update(incidentes =>
      this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, impactoPropagado.incidente),
    );
    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, impactoPropagado.servicioAfectado),
    );
    this.dependenciaDeServiciosSharedCollection.update(dependenciaDeServicios =>
      this.dependenciaDeServicioService.addDependenciaDeServicioToCollectionIfMissing<IDependenciaDeServicio>(
        dependenciaDeServicios,
        impactoPropagado.dependencia,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.incidenteService
      .query()
      .pipe(map((res: HttpResponse<IIncidente[]>) => res.body ?? []))
      .pipe(
        map((incidentes: IIncidente[]) =>
          this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, this.impactoPropagado?.incidente),
        ),
      )
      .subscribe((incidentes: IIncidente[]) => this.incidentesSharedCollection.set(incidentes));

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.impactoPropagado?.servicioAfectado),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));

    this.dependenciaDeServicioService
      .query()
      .pipe(map((res: HttpResponse<IDependenciaDeServicio[]>) => res.body ?? []))
      .pipe(
        map((dependenciaDeServicios: IDependenciaDeServicio[]) =>
          this.dependenciaDeServicioService.addDependenciaDeServicioToCollectionIfMissing<IDependenciaDeServicio>(
            dependenciaDeServicios,
            this.impactoPropagado?.dependencia,
          ),
        ),
      )
      .subscribe((dependenciaDeServicios: IDependenciaDeServicio[]) =>
        this.dependenciaDeServiciosSharedCollection.set(dependenciaDeServicios),
      );
  }
}
