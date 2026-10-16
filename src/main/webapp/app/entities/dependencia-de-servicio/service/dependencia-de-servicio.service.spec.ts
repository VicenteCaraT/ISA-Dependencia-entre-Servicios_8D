import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../dependencia-de-servicio.test-samples';

import { DependenciaDeServicioService } from './dependencia-de-servicio.service';

const requireRestSample: IDependenciaDeServicio = {
  ...sampleWithRequiredData,
};

describe('DependenciaDeServicio Service', () => {
  let service: DependenciaDeServicioService;
  let httpMock: HttpTestingController;
  let expectedResult: IDependenciaDeServicio | IDependenciaDeServicio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DependenciaDeServicioService);
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

    it('should create a DependenciaDeServicio', () => {
      const dependenciaDeServicio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dependenciaDeServicio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DependenciaDeServicio', () => {
      const dependenciaDeServicio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dependenciaDeServicio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DependenciaDeServicio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DependenciaDeServicio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DependenciaDeServicio', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addDependenciaDeServicioToCollectionIfMissing', () => {
      it('should add a DependenciaDeServicio to an empty array', () => {
        const dependenciaDeServicio: IDependenciaDeServicio = sampleWithRequiredData;
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing([], dependenciaDeServicio);
        expect(expectedResult).toEqual([dependenciaDeServicio]);
      });

      it('should not add a DependenciaDeServicio to an array that contains it', () => {
        const dependenciaDeServicio: IDependenciaDeServicio = sampleWithRequiredData;
        const dependenciaDeServicioCollection: IDependenciaDeServicio[] = [
          {
            ...dependenciaDeServicio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing(dependenciaDeServicioCollection, dependenciaDeServicio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DependenciaDeServicio to an array that doesn't contain it", () => {
        const dependenciaDeServicio: IDependenciaDeServicio = sampleWithRequiredData;
        const dependenciaDeServicioCollection: IDependenciaDeServicio[] = [sampleWithPartialData];
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing(dependenciaDeServicioCollection, dependenciaDeServicio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependenciaDeServicio);
      });

      it('should add only unique DependenciaDeServicio to an array', () => {
        const dependenciaDeServicioArray: IDependenciaDeServicio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dependenciaDeServicioCollection: IDependenciaDeServicio[] = [sampleWithRequiredData];
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing(
          dependenciaDeServicioCollection,
          ...dependenciaDeServicioArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dependenciaDeServicio: IDependenciaDeServicio = sampleWithRequiredData;
        const dependenciaDeServicio2: IDependenciaDeServicio = sampleWithPartialData;
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing([], dependenciaDeServicio, dependenciaDeServicio2);
        expect(expectedResult).toEqual([dependenciaDeServicio, dependenciaDeServicio2]);
      });

      it('should accept null and undefined values', () => {
        const dependenciaDeServicio: IDependenciaDeServicio = sampleWithRequiredData;
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing([], null, dependenciaDeServicio, undefined);
        expect(expectedResult).toEqual([dependenciaDeServicio]);
      });

      it('should return initial array if no DependenciaDeServicio is added', () => {
        const dependenciaDeServicioCollection: IDependenciaDeServicio[] = [sampleWithRequiredData];
        expectedResult = service.addDependenciaDeServicioToCollectionIfMissing(dependenciaDeServicioCollection, undefined, null);
        expect(expectedResult).toEqual(dependenciaDeServicioCollection);
      });
    });

    describe('compareDependenciaDeServicio', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDependenciaDeServicio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23302 };
        const entity2 = null;

        const compareResult1 = service.compareDependenciaDeServicio(entity1, entity2);
        const compareResult2 = service.compareDependenciaDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23302 };
        const entity2 = { id: 11782 };

        const compareResult1 = service.compareDependenciaDeServicio(entity1, entity2);
        const compareResult2 = service.compareDependenciaDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23302 };
        const entity2 = { id: 23302 };

        const compareResult1 = service.compareDependenciaDeServicio(entity1, entity2);
        const compareResult2 = service.compareDependenciaDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
