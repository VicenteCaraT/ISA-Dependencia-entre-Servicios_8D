import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDependenciaDeServicio, NewDependenciaDeServicio } from '../dependencia-de-servicio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDependenciaDeServicio for edit and NewDependenciaDeServicioFormGroupInput for create.
 */
type DependenciaDeServicioFormGroupInput = IDependenciaDeServicio | PartialWithRequiredKeyOf<NewDependenciaDeServicio>;

type DependenciaDeServicioFormDefaults = Pick<NewDependenciaDeServicio, 'id' | 'activa'>;

type DependenciaDeServicioFormGroupContent = {
  id: FormControl<IDependenciaDeServicio['id'] | NewDependenciaDeServicio['id']>;
  tipo: FormControl<IDependenciaDeServicio['tipo']>;
  activa: FormControl<IDependenciaDeServicio['activa']>;
  descripcion: FormControl<IDependenciaDeServicio['descripcion']>;
  servicioDependiente: FormControl<IDependenciaDeServicio['servicioDependiente']>;
  servicioProveedor: FormControl<IDependenciaDeServicio['servicioProveedor']>;
};

export type DependenciaDeServicioFormGroup = FormGroup<DependenciaDeServicioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DependenciaDeServicioFormService {
  createDependenciaDeServicioFormGroup(dependenciaDeServicio?: DependenciaDeServicioFormGroupInput): DependenciaDeServicioFormGroup {
    const dependenciaDeServicioRawValue = {
      ...this.getFormDefaults(),
      ...(dependenciaDeServicio ?? { id: null }),
    };

    return new FormGroup<DependenciaDeServicioFormGroupContent>({
      id: new FormControl(
        { value: dependenciaDeServicioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(dependenciaDeServicioRawValue.tipo, {
        validators: [Validators.required],
      }),
      activa: new FormControl(dependenciaDeServicioRawValue.activa, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(dependenciaDeServicioRawValue.descripcion, {
        validators: [Validators.maxLength(500)],
      }),
      servicioDependiente: new FormControl(dependenciaDeServicioRawValue.servicioDependiente, {
        validators: [Validators.required],
      }),
      servicioProveedor: new FormControl(dependenciaDeServicioRawValue.servicioProveedor, {
        validators: [Validators.required],
      }),
    });
  }

  getDependenciaDeServicio(form: DependenciaDeServicioFormGroup): IDependenciaDeServicio | NewDependenciaDeServicio {
    return form.getRawValue();
  }

  resetForm(form: DependenciaDeServicioFormGroup, dependenciaDeServicio: DependenciaDeServicioFormGroupInput): void {
    const dependenciaDeServicioRawValue = { ...this.getFormDefaults(), ...dependenciaDeServicio };
    form.reset({
      ...dependenciaDeServicioRawValue,
      id: { value: dependenciaDeServicioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DependenciaDeServicioFormDefaults {
    return {
      id: null,
      activa: false,
    };
  }
}
