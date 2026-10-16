import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServicioCritico, NewServicioCritico } from '../servicio-critico.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServicioCritico for edit and NewServicioCriticoFormGroupInput for create.
 */
type ServicioCriticoFormGroupInput = IServicioCritico | PartialWithRequiredKeyOf<NewServicioCritico>;

type ServicioCriticoFormDefaults = Pick<NewServicioCritico, 'id' | 'requiereEscalamientoInmediato'>;

type ServicioCriticoFormGroupContent = {
  id: FormControl<IServicioCritico['id'] | NewServicioCritico['id']>;
  prioridadEscalamiento: FormControl<IServicioCritico['prioridadEscalamiento']>;
  requiereEscalamientoInmediato: FormControl<IServicioCritico['requiereEscalamientoInmediato']>;
  justificacion: FormControl<IServicioCritico['justificacion']>;
  servicio: FormControl<IServicioCritico['servicio']>;
};

export type ServicioCriticoFormGroup = FormGroup<ServicioCriticoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicioCriticoFormService {
  createServicioCriticoFormGroup(servicioCritico?: ServicioCriticoFormGroupInput): ServicioCriticoFormGroup {
    const servicioCriticoRawValue = {
      ...this.getFormDefaults(),
      ...(servicioCritico ?? { id: null }),
    };

    return new FormGroup<ServicioCriticoFormGroupContent>({
      id: new FormControl(
        { value: servicioCriticoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      prioridadEscalamiento: new FormControl(servicioCriticoRawValue.prioridadEscalamiento, {
        validators: [Validators.required, Validators.min(1), Validators.max(10)],
      }),
      requiereEscalamientoInmediato: new FormControl(servicioCriticoRawValue.requiereEscalamientoInmediato, {
        validators: [Validators.required],
      }),
      justificacion: new FormControl(servicioCriticoRawValue.justificacion, {
        validators: [Validators.maxLength(500)],
      }),
      servicio: new FormControl(servicioCriticoRawValue.servicio, {
        validators: [Validators.required],
      }),
    });
  }

  getServicioCritico(form: ServicioCriticoFormGroup): IServicioCritico | NewServicioCritico {
    return form.getRawValue();
  }

  resetForm(form: ServicioCriticoFormGroup, servicioCritico: ServicioCriticoFormGroupInput): void {
    const servicioCriticoRawValue = { ...this.getFormDefaults(), ...servicioCritico };
    form.reset({
      ...servicioCriticoRawValue,
      id: { value: servicioCriticoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServicioCriticoFormDefaults {
    return {
      id: null,
      requiereEscalamientoInmediato: false,
    };
  }
}
