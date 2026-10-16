import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../servicio-critico.test-samples';

import { ServicioCriticoFormService } from './servicio-critico-form.service';

describe('ServicioCritico Form Service', () => {
  let service: ServicioCriticoFormService;

  beforeEach(() => {
    service = TestBed.inject(ServicioCriticoFormService);
  });

  describe('Service methods', () => {
    describe('createServicioCriticoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServicioCriticoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prioridadEscalamiento: expect.any(Object),
            requiereEscalamientoInmediato: expect.any(Object),
            justificacion: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });

      it('passing IServicioCritico should create a new form with FormGroup', () => {
        const formGroup = service.createServicioCriticoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prioridadEscalamiento: expect.any(Object),
            requiereEscalamientoInmediato: expect.any(Object),
            justificacion: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });
    });

    describe('getServicioCritico', () => {
      it('should return NewServicioCritico for default ServicioCritico initial value', () => {
        const formGroup = service.createServicioCriticoFormGroup(sampleWithNewData);

        const servicioCritico = service.getServicioCritico(formGroup);

        expect(servicioCritico).toMatchObject(sampleWithNewData);
      });

      it('should return NewServicioCritico for empty ServicioCritico initial value', () => {
        const formGroup = service.createServicioCriticoFormGroup();

        const servicioCritico = service.getServicioCritico(formGroup);

        expect(servicioCritico).toMatchObject({});
      });

      it('should return IServicioCritico', () => {
        const formGroup = service.createServicioCriticoFormGroup(sampleWithRequiredData);

        const servicioCritico = service.getServicioCritico(formGroup);

        expect(servicioCritico).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServicioCritico should not enable id FormControl', () => {
        const formGroup = service.createServicioCriticoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServicioCritico should disable id FormControl', () => {
        const formGroup = service.createServicioCriticoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
