import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';
import { DependenciaDeServicioService } from '../service/dependencia-de-servicio.service';

import { DependenciaDeServicioFormService } from './dependencia-de-servicio-form.service';
import { DependenciaDeServicioUpdate } from './dependencia-de-servicio-update';

describe('DependenciaDeServicio Management Update Component', () => {
  let comp: DependenciaDeServicioUpdate;
  let fixture: ComponentFixture<DependenciaDeServicioUpdate>;
  let activatedRoute: ActivatedRoute;
  let dependenciaDeServicioFormService: DependenciaDeServicioFormService;
  let dependenciaDeServicioService: DependenciaDeServicioService;
  let servicioService: ServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(DependenciaDeServicioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dependenciaDeServicioFormService = TestBed.inject(DependenciaDeServicioFormService);
    dependenciaDeServicioService = TestBed.inject(DependenciaDeServicioService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Servicio query and add missing value', () => {
      const dependenciaDeServicio: IDependenciaDeServicio = { id: 11782 };
      const servicioDependiente: IServicio = { id: 24037 };
      dependenciaDeServicio.servicioDependiente = servicioDependiente;
      const servicioProveedor: IServicio = { id: 24037 };
      dependenciaDeServicio.servicioProveedor = servicioProveedor;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicioDependiente, servicioProveedor];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dependenciaDeServicio });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const dependenciaDeServicio: IDependenciaDeServicio = { id: 11782 };
      const servicioDependiente: IServicio = { id: 24037 };
      dependenciaDeServicio.servicioDependiente = servicioDependiente;
      const servicioProveedor: IServicio = { id: 24037 };
      dependenciaDeServicio.servicioProveedor = servicioProveedor;

      activatedRoute.data = of({ dependenciaDeServicio });
      comp.ngOnInit();

      expect(comp.serviciosSharedCollection()).toContainEqual(servicioDependiente);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicioProveedor);
      expect(comp.dependenciaDeServicio).toEqual(dependenciaDeServicio);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDependenciaDeServicio>();
      const dependenciaDeServicio = { id: 23302 };
      vitest.spyOn(dependenciaDeServicioFormService, 'getDependenciaDeServicio').mockReturnValue(dependenciaDeServicio);
      vitest.spyOn(dependenciaDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependenciaDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(dependenciaDeServicio);
      saveSubject.complete();

      // THEN
      expect(dependenciaDeServicioFormService.getDependenciaDeServicio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dependenciaDeServicioService.update).toHaveBeenCalledWith(expect.objectContaining(dependenciaDeServicio));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDependenciaDeServicio>();
      const dependenciaDeServicio = { id: 23302 };
      vitest.spyOn(dependenciaDeServicioFormService, 'getDependenciaDeServicio').mockReturnValue({ id: null });
      vitest.spyOn(dependenciaDeServicioService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependenciaDeServicio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(dependenciaDeServicio);
      saveSubject.complete();

      // THEN
      expect(dependenciaDeServicioFormService.getDependenciaDeServicio).toHaveBeenCalled();
      expect(dependenciaDeServicioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDependenciaDeServicio>();
      const dependenciaDeServicio = { id: 23302 };
      vitest.spyOn(dependenciaDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependenciaDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dependenciaDeServicioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
