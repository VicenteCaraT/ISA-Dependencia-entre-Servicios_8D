import { IDependenciaDeServicio, NewDependenciaDeServicio } from './dependencia-de-servicio.model';

export const sampleWithRequiredData: IDependenciaDeServicio = {
  id: 11744,
  tipo: 'OBLIGATORIA',
  activa: false,
};

export const sampleWithPartialData: IDependenciaDeServicio = {
  id: 14345,
  tipo: 'OBLIGATORIA',
  activa: true,
  descripcion: 'under tarragon squirm',
};

export const sampleWithFullData: IDependenciaDeServicio = {
  id: 26957,
  tipo: 'OBLIGATORIA',
  activa: true,
  descripcion: 'furthermore when',
};

export const sampleWithNewData: NewDependenciaDeServicio = {
  tipo: 'OBLIGATORIA',
  activa: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
