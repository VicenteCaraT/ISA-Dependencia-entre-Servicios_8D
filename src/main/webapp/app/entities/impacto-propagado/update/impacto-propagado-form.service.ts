import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImpactoPropagado, NewImpactoPropagado } from '../impacto-propagado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImpactoPropagado for edit and NewImpactoPropagadoFormGroupInput for create.
 */
type ImpactoPropagadoFormGroupInput = IImpactoPropagado | PartialWithRequiredKeyOf<NewImpactoPropagado>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImpactoPropagado | NewImpactoPropagado> = Omit<T, 'detectadoEn'> & {
  detectadoEn?: string | null;
};

type ImpactoPropagadoFormRawValue = FormValueOf<IImpactoPropagado>;

type NewImpactoPropagadoFormRawValue = FormValueOf<NewImpactoPropagado>;

type ImpactoPropagadoFormDefaults = Pick<NewImpactoPropagado, 'id' | 'detectadoEn'>;

type ImpactoPropagadoFormGroupContent = {
  id: FormControl<ImpactoPropagadoFormRawValue['id'] | NewImpactoPropagado['id']>;
  detectadoEn: FormControl<ImpactoPropagadoFormRawValue['detectadoEn']>;
  estado: FormControl<ImpactoPropagadoFormRawValue['estado']>;
  detalle: FormControl<ImpactoPropagadoFormRawValue['detalle']>;
  incidente: FormControl<ImpactoPropagadoFormRawValue['incidente']>;
  servicioAfectado: FormControl<ImpactoPropagadoFormRawValue['servicioAfectado']>;
  dependencia: FormControl<ImpactoPropagadoFormRawValue['dependencia']>;
};

export type ImpactoPropagadoFormGroup = FormGroup<ImpactoPropagadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImpactoPropagadoFormService {
  createImpactoPropagadoFormGroup(impactoPropagado?: ImpactoPropagadoFormGroupInput): ImpactoPropagadoFormGroup {
    const impactoPropagadoRawValue = this.convertImpactoPropagadoToImpactoPropagadoRawValue({
      ...this.getFormDefaults(),
      ...(impactoPropagado ?? { id: null }),
    });

    return new FormGroup<ImpactoPropagadoFormGroupContent>({
      id: new FormControl(
        { value: impactoPropagadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      detectadoEn: new FormControl(impactoPropagadoRawValue.detectadoEn, {
        validators: [Validators.required],
      }),
      estado: new FormControl(impactoPropagadoRawValue.estado, {
        validators: [Validators.required],
      }),
      detalle: new FormControl(impactoPropagadoRawValue.detalle, {
        validators: [Validators.maxLength(1000)],
      }),
      incidente: new FormControl(impactoPropagadoRawValue.incidente, {
        validators: [Validators.required],
      }),
      servicioAfectado: new FormControl(impactoPropagadoRawValue.servicioAfectado, {
        validators: [Validators.required],
      }),
      dependencia: new FormControl(impactoPropagadoRawValue.dependencia),
    });
  }

  getImpactoPropagado(form: ImpactoPropagadoFormGroup): IImpactoPropagado | NewImpactoPropagado {
    return this.convertImpactoPropagadoRawValueToImpactoPropagado(form.getRawValue());
  }

  resetForm(form: ImpactoPropagadoFormGroup, impactoPropagado: ImpactoPropagadoFormGroupInput): void {
    const impactoPropagadoRawValue = this.convertImpactoPropagadoToImpactoPropagadoRawValue({
      ...this.getFormDefaults(),
      ...impactoPropagado,
    });
    form.reset({
      ...impactoPropagadoRawValue,
      id: { value: impactoPropagadoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ImpactoPropagadoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      detectadoEn: currentTime,
    };
  }

  private convertImpactoPropagadoRawValueToImpactoPropagado(
    rawImpactoPropagado: ImpactoPropagadoFormRawValue | NewImpactoPropagadoFormRawValue,
  ): IImpactoPropagado | NewImpactoPropagado {
    return {
      ...rawImpactoPropagado,
      detectadoEn: dayjs(rawImpactoPropagado.detectadoEn, DATE_TIME_FORMAT),
    };
  }

  private convertImpactoPropagadoToImpactoPropagadoRawValue(
    impactoPropagado: IImpactoPropagado | (Partial<NewImpactoPropagado> & ImpactoPropagadoFormDefaults),
  ): ImpactoPropagadoFormRawValue | PartialWithRequiredKeyOf<NewImpactoPropagadoFormRawValue> {
    return {
      ...impactoPropagado,
      detectadoEn: impactoPropagado.detectadoEn ? impactoPropagado.detectadoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
