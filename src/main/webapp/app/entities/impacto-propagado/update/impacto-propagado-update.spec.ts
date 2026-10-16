import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDependenciaDeServicio } from 'app/entities/dependencia-de-servicio/dependencia-de-servicio.model';
import { DependenciaDeServicioService } from 'app/entities/dependencia-de-servicio/service/dependencia-de-servicio.service';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IImpactoPropagado } from '../impacto-propagado.model';
import { ImpactoPropagadoService } from '../service/impacto-propagado.service';

import { ImpactoPropagadoFormService } from './impacto-propagado-form.service';
import { ImpactoPropagadoUpdate } from './impacto-propagado-update';

describe('ImpactoPropagado Management Update Component', () => {
  let comp: ImpactoPropagadoUpdate;
  let fixture: ComponentFixture<ImpactoPropagadoUpdate>;
  let activatedRoute: ActivatedRoute;
  let impactoPropagadoFormService: ImpactoPropagadoFormService;
  let impactoPropagadoService: ImpactoPropagadoService;
  let incidenteService: IncidenteService;
  let servicioService: ServicioService;
  let dependenciaDeServicioService: DependenciaDeServicioService;

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

    fixture = TestBed.createComponent(ImpactoPropagadoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    impactoPropagadoFormService = TestBed.inject(ImpactoPropagadoFormService);
    impactoPropagadoService = TestBed.inject(ImpactoPropagadoService);
    incidenteService = TestBed.inject(IncidenteService);
    servicioService = TestBed.inject(ServicioService);
    dependenciaDeServicioService = TestBed.inject(DependenciaDeServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Incidente query and add missing value', () => {
      const impactoPropagado: IImpactoPropagado = { id: 22901 };
      const incidente: IIncidente = { id: 31968 };
      impactoPropagado.incidente = incidente;

      const incidenteCollection: IIncidente[] = [{ id: 31968 }];
      vitest.spyOn(incidenteService, 'query').mockReturnValue(of(new HttpResponse({ body: incidenteCollection })));
      const additionalIncidentes = [incidente];
      const expectedCollection: IIncidente[] = [...additionalIncidentes, ...incidenteCollection];
      vitest.spyOn(incidenteService, 'addIncidenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      expect(incidenteService.query).toHaveBeenCalled();
      expect(incidenteService.addIncidenteToCollectionIfMissing).toHaveBeenCalledWith(
        incidenteCollection,
        ...additionalIncidentes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.incidentesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Servicio query and add missing value', () => {
      const impactoPropagado: IImpactoPropagado = { id: 22901 };
      const servicioAfectado: IServicio = { id: 24037 };
      impactoPropagado.servicioAfectado = servicioAfectado;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicioAfectado];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call DependenciaDeServicio query and add missing value', () => {
      const impactoPropagado: IImpactoPropagado = { id: 22901 };
      const dependencia: IDependenciaDeServicio = { id: 23302 };
      impactoPropagado.dependencia = dependencia;

      const dependenciaDeServicioCollection: IDependenciaDeServicio[] = [{ id: 23302 }];
      vitest.spyOn(dependenciaDeServicioService, 'query').mockReturnValue(of(new HttpResponse({ body: dependenciaDeServicioCollection })));
      const additionalDependenciaDeServicios = [dependencia];
      const expectedCollection: IDependenciaDeServicio[] = [...additionalDependenciaDeServicios, ...dependenciaDeServicioCollection];
      vitest.spyOn(dependenciaDeServicioService, 'addDependenciaDeServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      expect(dependenciaDeServicioService.query).toHaveBeenCalled();
      expect(dependenciaDeServicioService.addDependenciaDeServicioToCollectionIfMissing).toHaveBeenCalledWith(
        dependenciaDeServicioCollection,
        ...additionalDependenciaDeServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.dependenciaDeServiciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const impactoPropagado: IImpactoPropagado = { id: 22901 };
      const incidente: IIncidente = { id: 31968 };
      impactoPropagado.incidente = incidente;
      const servicioAfectado: IServicio = { id: 24037 };
      impactoPropagado.servicioAfectado = servicioAfectado;
      const dependencia: IDependenciaDeServicio = { id: 23302 };
      impactoPropagado.dependencia = dependencia;

      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      expect(comp.incidentesSharedCollection()).toContainEqual(incidente);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicioAfectado);
      expect(comp.dependenciaDeServiciosSharedCollection()).toContainEqual(dependencia);
      expect(comp.impactoPropagado).toEqual(impactoPropagado);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IImpactoPropagado>();
      const impactoPropagado = { id: 25142 };
      vitest.spyOn(impactoPropagadoFormService, 'getImpactoPropagado').mockReturnValue(impactoPropagado);
      vitest.spyOn(impactoPropagadoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(impactoPropagado);
      saveSubject.complete();

      // THEN
      expect(impactoPropagadoFormService.getImpactoPropagado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(impactoPropagadoService.update).toHaveBeenCalledWith(expect.objectContaining(impactoPropagado));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IImpactoPropagado>();
      const impactoPropagado = { id: 25142 };
      vitest.spyOn(impactoPropagadoFormService, 'getImpactoPropagado').mockReturnValue({ id: null });
      vitest.spyOn(impactoPropagadoService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactoPropagado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(impactoPropagado);
      saveSubject.complete();

      // THEN
      expect(impactoPropagadoFormService.getImpactoPropagado).toHaveBeenCalled();
      expect(impactoPropagadoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IImpactoPropagado>();
      const impactoPropagado = { id: 25142 };
      vitest.spyOn(impactoPropagadoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactoPropagado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(impactoPropagadoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIncidente', () => {
      it('should forward to incidenteService', () => {
        const entity = { id: 31968 };
        const entity2 = { id: 10195 };
        vitest.spyOn(incidenteService, 'compareIncidente');
        comp.compareIncidente(entity, entity2);
        expect(incidenteService.compareIncidente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDependenciaDeServicio', () => {
      it('should forward to dependenciaDeServicioService', () => {
        const entity = { id: 23302 };
        const entity2 = { id: 11782 };
        vitest.spyOn(dependenciaDeServicioService, 'compareDependenciaDeServicio');
        comp.compareDependenciaDeServicio(entity, entity2);
        expect(dependenciaDeServicioService.compareDependenciaDeServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
