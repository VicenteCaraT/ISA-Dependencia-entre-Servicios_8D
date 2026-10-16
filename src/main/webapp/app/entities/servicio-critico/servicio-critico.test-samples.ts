import { IServicioCritico, NewServicioCritico } from './servicio-critico.model';

export const sampleWithRequiredData: IServicioCritico = {
  id: 25157,
  prioridadEscalamiento: 4,
  requiereEscalamientoInmediato: false,
};

export const sampleWithPartialData: IServicioCritico = {
  id: 31301,
  prioridadEscalamiento: 2,
  requiereEscalamientoInmediato: false,
  justificacion: 'into cafe oof',
};

export const sampleWithFullData: IServicioCritico = {
  id: 30762,
  prioridadEscalamiento: 9,
  requiereEscalamientoInmediato: true,
  justificacion: 'fast unfurl inure',
};

export const sampleWithNewData: NewServicioCritico = {
  prioridadEscalamiento: 10,
  requiereEscalamientoInmediato: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
