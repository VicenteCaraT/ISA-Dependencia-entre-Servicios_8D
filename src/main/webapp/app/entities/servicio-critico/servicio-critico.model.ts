import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IServicioCritico {
  id: number;
  prioridadEscalamiento?: number | null;
  requiereEscalamientoInmediato?: boolean | null;
  justificacion?: string | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewServicioCritico = Omit<IServicioCritico, 'id'> & { id: null };
