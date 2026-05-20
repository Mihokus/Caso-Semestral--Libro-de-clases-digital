import { client } from "./client";
import type {
  AsignaturaDTO,
  AsignaturaRequest,
  CursoDTO,
  CursoRequest,
  EvaluacionDTO,
  EvaluacionRequest,
  RendimientoDTO,
} from "./types";

export async function listAsignaturas(): Promise<AsignaturaDTO[]> {
  const { data } = await client.get<AsignaturaDTO[]>("/api/asignaturas");
  return data;
}

export async function getAsignatura(id: number): Promise<AsignaturaDTO> {
  const { data } = await client.get<AsignaturaDTO>(`/api/asignaturas/${id}`);
  return data;
}

export async function crearAsignatura(req: AsignaturaRequest): Promise<AsignaturaDTO> {
  const { data } = await client.post<AsignaturaDTO>("/api/asignaturas", req);
  return data;
}

export async function rendimiento(asignaturaId: number): Promise<RendimientoDTO> {
  const { data } = await client.get<RendimientoDTO>(`/api/asignaturas/${asignaturaId}/rendimiento`);
  return data;
}

export async function listCursos(): Promise<CursoDTO[]> {
  const { data } = await client.get<CursoDTO[]>("/api/cursos");
  return data;
}

export async function crearCurso(req: CursoRequest): Promise<CursoDTO> {
  const { data } = await client.post<CursoDTO>("/api/cursos", req);
  return data;
}

export async function registrarNota(req: EvaluacionRequest): Promise<EvaluacionDTO> {
  const { data } = await client.post<EvaluacionDTO>("/api/notas", req);
  return data;
}

export async function notasAlumno(alumnoId: number): Promise<EvaluacionDTO[]> {
  const { data } = await client.get<EvaluacionDTO[]>(`/api/notas/alumno/${alumnoId}`);
  return data;
}
