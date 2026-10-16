import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { DependenciaDeServicioDetail } from './dependencia-de-servicio-detail';

describe('DependenciaDeServicio Management Detail Component', () => {
  let comp: DependenciaDeServicioDetail;
  let fixture: ComponentFixture<DependenciaDeServicioDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./dependencia-de-servicio-detail').then(m => m.DependenciaDeServicioDetail),
              resolve: { dependenciaDeServicio: () => of({ id: 23302 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DependenciaDeServicioDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load dependenciaDeServicio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DependenciaDeServicioDetail);

      // THEN
      expect(instance.dependenciaDeServicio()).toEqual(expect.objectContaining({ id: 23302 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
