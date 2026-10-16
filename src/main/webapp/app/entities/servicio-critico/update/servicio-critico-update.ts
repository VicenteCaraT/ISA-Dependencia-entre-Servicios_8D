import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ServicioCriticoService } from '../service/servicio-critico.service';
import { IServicioCritico } from '../servicio-critico.model';

import { ServicioCriticoFormGroup, ServicioCriticoFormService } from './servicio-critico-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-servicio-critico-update',
  templateUrl: './servicio-critico-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ServicioCriticoUpdate implements OnInit {
  readonly isSaving = signal(false);
  servicioCritico: IServicioCritico | null = null;

  serviciosCollection = signal<IServicio[]>([]);

  protected servicioCriticoService = inject(ServicioCriticoService);
  protected servicioCriticoFormService = inject(ServicioCriticoFormService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServicioCriticoFormGroup = this.servicioCriticoFormService.createServicioCriticoFormGroup();

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicioCritico }) => {
      this.servicioCritico = servicioCritico;
      if (servicioCritico) {
        this.updateForm(servicioCritico);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const servicioCritico = this.servicioCriticoFormService.getServicioCritico(this.editForm);
    if (servicioCritico.id === null) {
      this.subscribeToSaveResponse(this.servicioCriticoService.create(servicioCritico));
    } else {
      this.subscribeToSaveResponse(this.servicioCriticoService.update(servicioCritico));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServicioCritico | null>): void {
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

  protected updateForm(servicioCritico: IServicioCritico): void {
    this.servicioCritico = servicioCritico;
    this.servicioCriticoFormService.resetForm(this.editForm, servicioCritico);

    this.serviciosCollection.set(
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(this.serviciosCollection(), servicioCritico.servicio),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query({ 'perfilCriticoId.specified': 'false' })
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.servicioCritico?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosCollection.set(servicios));
  }
}
