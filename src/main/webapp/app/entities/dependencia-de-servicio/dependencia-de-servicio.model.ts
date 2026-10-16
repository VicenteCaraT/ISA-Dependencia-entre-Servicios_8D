import { TipoDependencia } from 'app/entities/enumerations/tipo-dependencia.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IDependenciaDeServicio {
  id: number;
  tipo?: keyof typeof TipoDependencia | null;
  activa?: boolean | null;
  descripcion?: string | null;
  servicioDependiente?: Pick<IServicio, 'id' | 'nombre'> | null;
  servicioProveedor?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewDependenciaDeServicio = Omit<IDependenciaDeServicio, 'id'> & { id: null };
