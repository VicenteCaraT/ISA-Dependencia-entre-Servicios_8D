import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { TipoDependencia } from 'app/entities/enumerations/tipo-dependencia.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';
import { DependenciaDeServicioService } from '../service/dependencia-de-servicio.service';

import { DependenciaDeServicioFormGroup, DependenciaDeServicioFormService } from './dependencia-de-servicio-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-dependencia-de-servicio-update',
  templateUrl: './dependencia-de-servicio-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DependenciaDeServicioUpdate implements OnInit {
  readonly isSaving = signal(false);
  dependenciaDeServicio: IDependenciaDeServicio | null = null;
  tipoDependenciaValues = Object.keys(TipoDependencia);

  serviciosSharedCollection = signal<IServicio[]>([]);

  protected dependenciaDeServicioService = inject(DependenciaDeServicioService);
  protected dependenciaDeServicioFormService = inject(DependenciaDeServicioFormService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DependenciaDeServicioFormGroup = this.dependenciaDeServicioFormService.createDependenciaDeServicioFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependenciaDeServicio }) => {
      this.dependenciaDeServicio = dependenciaDeServicio;
      if (dependenciaDeServicio) {
        this.updateForm(dependenciaDeServicio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const dependenciaDeServicio = this.dependenciaDeServicioFormService.getDependenciaDeServicio(this.editForm);
    if (dependenciaDeServicio.id === null) {
      this.subscribeToSaveResponse(this.dependenciaDeServicioService.create(dependenciaDeServicio));
    } else {
      this.subscribeToSaveResponse(this.dependenciaDeServicioService.update(dependenciaDeServicio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDependenciaDeServicio | null>): void {
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

  protected updateForm(dependenciaDeServicio: IDependenciaDeServicio): void {
    this.dependenciaDeServicio = dependenciaDeServicio;
    this.dependenciaDeServicioFormService.resetForm(this.editForm, dependenciaDeServicio);

    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(
        servicios,
        dependenciaDeServicio.servicioDependiente,
        dependenciaDeServicio.servicioProveedor,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(
            servicios,
            this.dependenciaDeServicio?.servicioDependiente,
            this.dependenciaDeServicio?.servicioProveedor,
          ),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
