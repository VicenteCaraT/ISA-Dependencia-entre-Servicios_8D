import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IImpactoPropagado } from '../impacto-propagado.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../impacto-propagado.test-samples';

import { ImpactoPropagadoService, RestImpactoPropagado } from './impacto-propagado.service';

const requireRestSample: RestImpactoPropagado = {
  ...sampleWithRequiredData,
  detectadoEn: sampleWithRequiredData.detectadoEn?.toJSON(),
};

describe('ImpactoPropagado Service', () => {
  let service: ImpactoPropagadoService;
  let httpMock: HttpTestingController;
  let expectedResult: IImpactoPropagado | IImpactoPropagado[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImpactoPropagadoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ImpactoPropagado', () => {
      const impactoPropagado = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(impactoPropagado).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImpactoPropagado', () => {
      const impactoPropagado = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(impactoPropagado).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImpactoPropagado', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImpactoPropagado', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ImpactoPropagado', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addImpactoPropagadoToCollectionIfMissing', () => {
      it('should add a ImpactoPropagado to an empty array', () => {
        const impactoPropagado: IImpactoPropagado = sampleWithRequiredData;
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing([], impactoPropagado);
        expect(expectedResult).toEqual([impactoPropagado]);
      });

      it('should not add a ImpactoPropagado to an array that contains it', () => {
        const impactoPropagado: IImpactoPropagado = sampleWithRequiredData;
        const impactoPropagadoCollection: IImpactoPropagado[] = [
          {
            ...impactoPropagado,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing(impactoPropagadoCollection, impactoPropagado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImpactoPropagado to an array that doesn't contain it", () => {
        const impactoPropagado: IImpactoPropagado = sampleWithRequiredData;
        const impactoPropagadoCollection: IImpactoPropagado[] = [sampleWithPartialData];
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing(impactoPropagadoCollection, impactoPropagado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(impactoPropagado);
      });

      it('should add only unique ImpactoPropagado to an array', () => {
        const impactoPropagadoArray: IImpactoPropagado[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const impactoPropagadoCollection: IImpactoPropagado[] = [sampleWithRequiredData];
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing(impactoPropagadoCollection, ...impactoPropagadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const impactoPropagado: IImpactoPropagado = sampleWithRequiredData;
        const impactoPropagado2: IImpactoPropagado = sampleWithPartialData;
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing([], impactoPropagado, impactoPropagado2);
        expect(expectedResult).toEqual([impactoPropagado, impactoPropagado2]);
      });

      it('should accept null and undefined values', () => {
        const impactoPropagado: IImpactoPropagado = sampleWithRequiredData;
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing([], null, impactoPropagado, undefined);
        expect(expectedResult).toEqual([impactoPropagado]);
      });

      it('should return initial array if no ImpactoPropagado is added', () => {
        const impactoPropagadoCollection: IImpactoPropagado[] = [sampleWithRequiredData];
        expectedResult = service.addImpactoPropagadoToCollectionIfMissing(impactoPropagadoCollection, undefined, null);
        expect(expectedResult).toEqual(impactoPropagadoCollection);
      });
    });

    describe('compareImpactoPropagado', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImpactoPropagado(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25142 };
        const entity2 = null;

        const compareResult1 = service.compareImpactoPropagado(entity1, entity2);
        const compareResult2 = service.compareImpactoPropagado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25142 };
        const entity2 = { id: 22901 };

        const compareResult1 = service.compareImpactoPropagado(entity1, entity2);
        const compareResult2 = service.compareImpactoPropagado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25142 };
        const entity2 = { id: 25142 };

        const compareResult1 = service.compareImpactoPropagado(entity1, entity2);
        const compareResult2 = service.compareImpactoPropagado(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
