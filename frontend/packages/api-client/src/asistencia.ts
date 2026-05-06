import { client } from "./client";
import type {
  AlumnoDTO,
  AnotacionDTO,
  AnotacionRequest,
  AsistenciaBulkRequest,
  AsistenciaDTO,
} from "./types";

export async function listAlumnos(): Promise<AlumnoDTO[]> {
  const { data } = await client.get<AlumnoDTO[]>("/api/alumnos");
  return data;
}

export async function getAlumno(id: number): Promise<AlumnoDTO> {
  const { data } = await client.get<AlumnoDTO>(`/api/alumnos/${id}`);
  return data;
}

export async function listByCurso(cursoId: number): Promise<AlumnoDTO[]> {
  const { data } = await client.get<AlumnoDTO[]>(`/api/cursos/${cursoId}/alumnos`);
  return data;
}

export async function registrarBulk(req: AsistenciaBulkRequest): Promise<AsistenciaDTO[]> {
  const { data } = await client.post<AsistenciaDTO[]>("/api/asistencia", req);
  return data;
}

export async function historialAlumno(alumnoId: number): Promise<AsistenciaDTO[]> {
  const { data } = await client.get<AsistenciaDTO[]>(`/api/asistencia/alumno/${alumnoId}`);
  return data;
}

export async function porCursoYFecha(cursoId: number, fecha: string): Promise<AsistenciaDTO[]> {
  const { data } = await client.get<AsistenciaDTO[]>(`/api/asistencia/curso/${cursoId}/${fecha}`);
  return data;
}

export async function registrarAnotacion(req: AnotacionRequest): Promise<AnotacionDTO> {
  const { data } = await client.post<AnotacionDTO>("/api/anotaciones", req);
  return data;
}

export async function anotacionesAlumno(alumnoId: number): Promise<AnotacionDTO[]> {
  const { data } = await client.get<AnotacionDTO[]>(`/api/anotaciones/alumno/${alumnoId}`);
  return data;
}
