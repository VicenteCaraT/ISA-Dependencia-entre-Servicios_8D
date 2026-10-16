import dayjs from 'dayjs/esm';

import { IDependenciaDeServicio } from 'app/entities/dependencia-de-servicio/dependencia-de-servicio.model';
import { EstadoImpacto } from 'app/entities/enumerations/estado-impacto.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IImpactoPropagado {
  id: number;
  detectadoEn?: dayjs.Dayjs | null;
  estado?: keyof typeof EstadoImpacto | null;
  detalle?: string | null;
  incidente?: Pick<IIncidente, 'id' | 'titulo'> | null;
  servicioAfectado?: Pick<IServicio, 'id' | 'nombre'> | null;
  dependencia?: Pick<IDependenciaDeServicio, 'id' | 'descripcion'> | null;
}

export type NewImpactoPropagado = Omit<IImpactoPropagado, 'id'> & { id: null };
