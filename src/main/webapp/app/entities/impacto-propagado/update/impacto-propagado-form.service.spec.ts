import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../impacto-propagado.test-samples';

import { ImpactoPropagadoFormService } from './impacto-propagado-form.service';

describe('ImpactoPropagado Form Service', () => {
  let service: ImpactoPropagadoFormService;

  beforeEach(() => {
    service = TestBed.inject(ImpactoPropagadoFormService);
  });

  describe('Service methods', () => {
    describe('createImpactoPropagadoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImpactoPropagadoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detectadoEn: expect.any(Object),
            estado: expect.any(Object),
            detalle: expect.any(Object),
            incidente: expect.any(Object),
            servicioAfectado: expect.any(Object),
            dependencia: expect.any(Object),
          }),
        );
      });

      it('passing IImpactoPropagado should create a new form with FormGroup', () => {
        const formGroup = service.createImpactoPropagadoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detectadoEn: expect.any(Object),
            estado: expect.any(Object),
            detalle: expect.any(Object),
            incidente: expect.any(Object),
            servicioAfectado: expect.any(Object),
            dependencia: expect.any(Object),
          }),
        );
      });
    });

    describe('getImpactoPropagado', () => {
      it('should return NewImpactoPropagado for default ImpactoPropagado initial value', () => {
        const formGroup = service.createImpactoPropagadoFormGroup(sampleWithNewData);

        const impactoPropagado = service.getImpactoPropagado(formGroup);

        expect(impactoPropagado).toMatchObject(sampleWithNewData);
      });

      it('should return NewImpactoPropagado for empty ImpactoPropagado initial value', () => {
        const formGroup = service.createImpactoPropagadoFormGroup();

        const impactoPropagado = service.getImpactoPropagado(formGroup);

        expect(impactoPropagado).toMatchObject({});
      });

      it('should return IImpactoPropagado', () => {
        const formGroup = service.createImpactoPropagadoFormGroup(sampleWithRequiredData);

        const impactoPropagado = service.getImpactoPropagado(formGroup);

        expect(impactoPropagado).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImpactoPropagado should not enable id FormControl', () => {
        const formGroup = service.createImpactoPropagadoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImpactoPropagado should disable id FormControl', () => {
        const formGroup = service.createImpactoPropagadoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
