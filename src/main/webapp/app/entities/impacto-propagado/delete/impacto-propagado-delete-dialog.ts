import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IImpactoPropagado } from '../impacto-propagado.model';
import { ImpactoPropagadoService } from '../service/impacto-propagado.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './impacto-propagado-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ImpactoPropagadoDeleteDialog {
  impactoPropagado?: IImpactoPropagado;

  protected readonly impactoPropagadoService = inject(ImpactoPropagadoService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.impactoPropagadoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
