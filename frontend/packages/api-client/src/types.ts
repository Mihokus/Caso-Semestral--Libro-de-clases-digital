export type Role = "DOCENTE" | "APODERADO" | "ESTUDIANTE" | "ADMIN";

export type EstadoAsistencia = "PRESENTE" | "AUSENTE" | "JUSTIFICADO";

export type TipoAnotacion = "POSITIVA" | "NEGATIVA" | "NEUTRAL";

export type TipoMensaje = "COMUNICADO" | "DIRECTO";

export interface RoleDTO {
  id: number;
  nombre: string;
  descripcion: string | null;
}

export interface MenuDTO {
  id: number;
  label: string;
  path: string;
  parentId: number | null;
  orden: number | null;
  roles: string[];
}

export interface MenuRequest {
  label: string;
  path: string;
  parentId?: number | null;
  orden?: number | null;
  roleIds?: number[];
}

export interface UserDTO {
  id: number;
  email: string;
  nombre: string;
  entityId: number | null;
  roles: string[];
  menus: MenuDTO[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  nombre: string;
  role: Role;
  entityId?: number | null;
}

export interface LoginResponse {
  token: string;
  user: UserDTO;
}

export interface UpdateRolesRequest {
  roleIds: number[];
}

export interface ApoderadoInfo {
  id: number;
  nombre: string;
  email: string;
}

export interface RegistradoPor {
  id: number;
  nombre: string;
}

export interface PartyInfo {
  id: number;
  nombre: string;
  rol: string;
}

export interface AlumnoDTO {
  id: number;
  nombre: string;
  rut: string;
  cursoId: number;
  cursoNombre: string;
  apoderados: ApoderadoInfo[];
}

export interface AsistenciaBulkEntrada {
  alumnoId: number;
  estado: EstadoAsistencia;
}

export interface AsistenciaBulkRequest {
  cursoId: number;
  fecha: string;
  asistencias: AsistenciaBulkEntrada[];
}

export interface AsistenciaDTO {
  id: number;
  alumnoId: number;
  alumnoNombre: string;
  cursoId: number;
  cursoNombre: string;
  fecha: string;
  estado: EstadoAsistencia;
  registradoPor: RegistradoPor;
}

export interface AnotacionRequest {
  alumnoId: number;
  tipo: TipoAnotacion;
  descripcion: string;
}

export interface AnotacionDTO {
  id: number;
  alumnoId: number;
  alumnoNombre: string;
  tipo: TipoAnotacion;
  descripcion: string;
  fecha: string;
  registradoPor: RegistradoPor;
}

export interface MensajeDirectoRequest {
  destinatarioId: number;
  titulo: string;
  contenido: string;
}

export interface MensajeComunicadoRequest {
  cursoId: number;
  titulo: string;
  contenido: string;
}

export interface MensajeDTO {
  id: number;
  tipo: TipoMensaje;
  titulo: string;
  contenido: string;
  remitente: PartyInfo;
  destinatario: PartyInfo | null;
  cursoId: number | null;
  fechaEnvio: string;
  leido: boolean;
}

export interface CursoDTO {
  id: number;
  nombre: string;
  nivel: string;
  cantidadAlumnos: number | null;
}

export interface CursoRequest {
  nombre: string;
  nivel: string;
}

export interface AsignaturaRequest {
  nombre: string;
  cursoId: number;
  docenteId: number;
}

export interface AsignaturaDTO {
  id: number;
  nombre: string;
  cursoId: number;
  cursoNombre: string;
  docenteId: number;
  docenteNombre: string;
}

export interface EvaluacionRequest {
  alumnoId: number;
  asignaturaId: number;
  nombre: string;
  nota: number;
  ponderacion: number;
}

export interface EvaluacionDTO {
  id: number;
  alumnoId: number;
  alumnoNombre: string;
  asignaturaId: number;
  asignaturaNombre: string;
  nombre: string;
  nota: number;
  ponderacion: number;
  fecha: string;
  registradoPor: RegistradoPor;
}

export interface RendimientoAlumno {
  alumnoId: number;
  alumnoNombre: string;
  promedio: number;
  cantidadEvaluaciones: number;
}

export interface RendimientoDTO {
  asignaturaId: number;
  asignaturaNombre: string;
  promedioCurso: number;
  cantidadEvaluaciones: number;
  alumnos: RendimientoAlumno[];
}

export interface UserSummary {
  id: number;
  email: string;
  nombre: string;
}

export interface DocenteDashboardDTO {
  rol: "DOCENTE";
  user: UserSummary;
  asignaturas: AsignaturaDTO[];
  alumnosACargo: number;
  mensajesNoLeidos: number;
  evaluacionesPendientes: number;
}

export interface PupiloResumen {
  alumno: AlumnoDTO;
  asistenciaPorcentaje: number | null;
  promedioGeneral: number | null;
  ultimasAnotaciones: AnotacionDTO[];
}

export interface ApoderadoDashboardDTO {
  rol: "APODERADO";
  user: UserSummary;
  pupilos: PupiloResumen[];
  mensajesNoLeidos: number;
}

export interface EstudianteDashboardDTO {
  rol: "ESTUDIANTE";
  user: UserSummary;
  alumno: AlumnoDTO | null;
  asistenciaPorcentaje: number | null;
  promedioGeneral: number | null;
  mensajesNoLeidos: number;
}

export interface AdminDashboardDTO {
  rol: "ADMIN";
  user: UserSummary;
  totalUsuarios: number;
  totalCursos: number;
  totalAsignaturas: number;
  mensajesNoLeidos: number;
}

export type DashboardDTO =
  | DocenteDashboardDTO
  | ApoderadoDashboardDTO
  | EstudianteDashboardDTO
  | AdminDashboardDTO;
