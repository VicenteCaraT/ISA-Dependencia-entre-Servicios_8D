import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IServicioCritico } from '../servicio-critico.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../servicio-critico.test-samples';

import { ServicioCriticoService } from './servicio-critico.service';

const requireRestSample: IServicioCritico = {
  ...sampleWithRequiredData,
};

describe('ServicioCritico Service', () => {
  let service: ServicioCriticoService;
  let httpMock: HttpTestingController;
  let expectedResult: IServicioCritico | IServicioCritico[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServicioCriticoService);
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

    it('should create a ServicioCritico', () => {
      const servicioCritico = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(servicioCritico).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServicioCritico', () => {
      const servicioCritico = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(servicioCritico).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServicioCritico', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServicioCritico', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServicioCritico', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addServicioCriticoToCollectionIfMissing', () => {
      it('should add a ServicioCritico to an empty array', () => {
        const servicioCritico: IServicioCritico = sampleWithRequiredData;
        expectedResult = service.addServicioCriticoToCollectionIfMissing([], servicioCritico);
        expect(expectedResult).toEqual([servicioCritico]);
      });

      it('should not add a ServicioCritico to an array that contains it', () => {
        const servicioCritico: IServicioCritico = sampleWithRequiredData;
        const servicioCriticoCollection: IServicioCritico[] = [
          {
            ...servicioCritico,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServicioCriticoToCollectionIfMissing(servicioCriticoCollection, servicioCritico);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServicioCritico to an array that doesn't contain it", () => {
        const servicioCritico: IServicioCritico = sampleWithRequiredData;
        const servicioCriticoCollection: IServicioCritico[] = [sampleWithPartialData];
        expectedResult = service.addServicioCriticoToCollectionIfMissing(servicioCriticoCollection, servicioCritico);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(servicioCritico);
      });

      it('should add only unique ServicioCritico to an array', () => {
        const servicioCriticoArray: IServicioCritico[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const servicioCriticoCollection: IServicioCritico[] = [sampleWithRequiredData];
        expectedResult = service.addServicioCriticoToCollectionIfMissing(servicioCriticoCollection, ...servicioCriticoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const servicioCritico: IServicioCritico = sampleWithRequiredData;
        const servicioCritico2: IServicioCritico = sampleWithPartialData;
        expectedResult = service.addServicioCriticoToCollectionIfMissing([], servicioCritico, servicioCritico2);
        expect(expectedResult).toEqual([servicioCritico, servicioCritico2]);
      });

      it('should accept null and undefined values', () => {
        const servicioCritico: IServicioCritico = sampleWithRequiredData;
        expectedResult = service.addServicioCriticoToCollectionIfMissing([], null, servicioCritico, undefined);
        expect(expectedResult).toEqual([servicioCritico]);
      });

      it('should return initial array if no ServicioCritico is added', () => {
        const servicioCriticoCollection: IServicioCritico[] = [sampleWithRequiredData];
        expectedResult = service.addServicioCriticoToCollectionIfMissing(servicioCriticoCollection, undefined, null);
        expect(expectedResult).toEqual(servicioCriticoCollection);
      });
    });

    describe('compareServicioCritico', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServicioCritico(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6726 };
        const entity2 = null;

        const compareResult1 = service.compareServicioCritico(entity1, entity2);
        const compareResult2 = service.compareServicioCritico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6726 };
        const entity2 = { id: 17267 };

        const compareResult1 = service.compareServicioCritico(entity1, entity2);
        const compareResult2 = service.compareServicioCritico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6726 };
        const entity2 = { id: 6726 };

        const compareResult1 = service.compareServicioCritico(entity1, entity2);
        const compareResult2 = service.compareServicioCritico(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
