import dayjs from 'dayjs/esm';

import { IImpactoPropagado, NewImpactoPropagado } from './impacto-propagado.model';

export const sampleWithRequiredData: IImpactoPropagado = {
  id: 10250,
  detectadoEn: dayjs('2026-10-16T06:27'),
  estado: 'DETECTADO',
};

export const sampleWithPartialData: IImpactoPropagado = {
  id: 20549,
  detectadoEn: dayjs('2026-10-16T09:23'),
  estado: 'DESCARTADO',
};

export const sampleWithFullData: IImpactoPropagado = {
  id: 20030,
  detectadoEn: dayjs('2026-10-16T07:51'),
  estado: 'NOTIFICADO',
  detalle: 'ectoderm insert',
};

export const sampleWithNewData: NewImpactoPropagado = {
  detectadoEn: dayjs('2026-10-16T03:19'),
  estado: 'NOTIFICADO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
