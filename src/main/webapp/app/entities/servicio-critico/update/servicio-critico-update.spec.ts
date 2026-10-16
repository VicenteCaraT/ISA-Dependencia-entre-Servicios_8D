import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioCriticoService } from '../service/servicio-critico.service';
import { IServicioCritico } from '../servicio-critico.model';

import { ServicioCriticoFormService } from './servicio-critico-form.service';
import { ServicioCriticoUpdate } from './servicio-critico-update';

describe('ServicioCritico Management Update Component', () => {
  let comp: ServicioCriticoUpdate;
  let fixture: ComponentFixture<ServicioCriticoUpdate>;
  let activatedRoute: ActivatedRoute;
  let servicioCriticoFormService: ServicioCriticoFormService;
  let servicioCriticoService: ServicioCriticoService;
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

    fixture = TestBed.createComponent(ServicioCriticoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioCriticoFormService = TestBed.inject(ServicioCriticoFormService);
    servicioCriticoService = TestBed.inject(ServicioCriticoService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call servicio query and add missing value', () => {
      const servicioCritico: IServicioCritico = { id: 17267 };
      const servicio: IServicio = { id: 24037 };
      servicioCritico.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const expectedCollection: IServicio[] = [servicio, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicioCritico });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(servicioCollection, servicio);
      expect(comp.serviciosCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const servicioCritico: IServicioCritico = { id: 17267 };
      const servicio: IServicio = { id: 24037 };
      servicioCritico.servicio = servicio;

      activatedRoute.data = of({ servicioCritico });
      comp.ngOnInit();

      expect(comp.serviciosCollection()).toContainEqual(servicio);
      expect(comp.servicioCritico).toEqual(servicioCritico);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioCritico>();
      const servicioCritico = { id: 6726 };
      vitest.spyOn(servicioCriticoFormService, 'getServicioCritico').mockReturnValue(servicioCritico);
      vitest.spyOn(servicioCriticoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioCritico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioCritico);
      saveSubject.complete();

      // THEN
      expect(servicioCriticoFormService.getServicioCritico).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioCriticoService.update).toHaveBeenCalledWith(expect.objectContaining(servicioCritico));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioCritico>();
      const servicioCritico = { id: 6726 };
      vitest.spyOn(servicioCriticoFormService, 'getServicioCritico').mockReturnValue({ id: null });
      vitest.spyOn(servicioCriticoService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioCritico: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioCritico);
      saveSubject.complete();

      // THEN
      expect(servicioCriticoFormService.getServicioCritico).toHaveBeenCalled();
      expect(servicioCriticoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioCritico>();
      const servicioCritico = { id: 6726 };
      vitest.spyOn(servicioCriticoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioCritico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioCriticoService.update).toHaveBeenCalled();
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
