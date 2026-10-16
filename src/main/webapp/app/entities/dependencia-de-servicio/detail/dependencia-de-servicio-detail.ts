import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-dependencia-de-servicio-detail',
  templateUrl: './dependencia-de-servicio-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink],
})
export class DependenciaDeServicioDetail {
  readonly dependenciaDeServicio = input<IDependenciaDeServicio | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
