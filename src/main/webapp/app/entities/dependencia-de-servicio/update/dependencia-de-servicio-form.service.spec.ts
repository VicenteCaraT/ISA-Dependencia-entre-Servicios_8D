import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../dependencia-de-servicio.test-samples';

import { DependenciaDeServicioFormService } from './dependencia-de-servicio-form.service';

describe('DependenciaDeServicio Form Service', () => {
  let service: DependenciaDeServicioFormService;

  beforeEach(() => {
    service = TestBed.inject(DependenciaDeServicioFormService);
  });

  describe('Service methods', () => {
    describe('createDependenciaDeServicioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            activa: expect.any(Object),
            descripcion: expect.any(Object),
            servicioDependiente: expect.any(Object),
            servicioProveedor: expect.any(Object),
          }),
        );
      });

      it('passing IDependenciaDeServicio should create a new form with FormGroup', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            activa: expect.any(Object),
            descripcion: expect.any(Object),
            servicioDependiente: expect.any(Object),
            servicioProveedor: expect.any(Object),
          }),
        );
      });
    });

    describe('getDependenciaDeServicio', () => {
      it('should return NewDependenciaDeServicio for default DependenciaDeServicio initial value', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup(sampleWithNewData);

        const dependenciaDeServicio = service.getDependenciaDeServicio(formGroup);

        expect(dependenciaDeServicio).toMatchObject(sampleWithNewData);
      });

      it('should return NewDependenciaDeServicio for empty DependenciaDeServicio initial value', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup();

        const dependenciaDeServicio = service.getDependenciaDeServicio(formGroup);

        expect(dependenciaDeServicio).toMatchObject({});
      });

      it('should return IDependenciaDeServicio', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup(sampleWithRequiredData);

        const dependenciaDeServicio = service.getDependenciaDeServicio(formGroup);

        expect(dependenciaDeServicio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDependenciaDeServicio should not enable id FormControl', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDependenciaDeServicio should disable id FormControl', () => {
        const formGroup = service.createDependenciaDeServicioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
