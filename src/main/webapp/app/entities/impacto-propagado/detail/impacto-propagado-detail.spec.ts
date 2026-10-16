import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ImpactoPropagadoDetail } from './impacto-propagado-detail';

describe('ImpactoPropagado Management Detail Component', () => {
  let comp: ImpactoPropagadoDetail;
  let fixture: ComponentFixture<ImpactoPropagadoDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./impacto-propagado-detail').then(m => m.ImpactoPropagadoDetail),
              resolve: { impactoPropagado: () => of({ id: 25142 }) },
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
    fixture = TestBed.createComponent(ImpactoPropagadoDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load impactoPropagado on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImpactoPropagadoDetail);

      // THEN
      expect(instance.impactoPropagado()).toEqual(expect.objectContaining({ id: 25142 }));
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
