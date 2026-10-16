import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IDependenciaDeServicio } from '../dependencia-de-servicio.model';
import { DependenciaDeServicioService } from '../service/dependencia-de-servicio.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dependencia-de-servicio-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class DependenciaDeServicioDeleteDialog {
  dependenciaDeServicio?: IDependenciaDeServicio;

  protected readonly dependenciaDeServicioService = inject(DependenciaDeServicioService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dependenciaDeServicioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
