import { useEffect, useState } from "react";
import { dashboard, type DashboardDTO } from "@libroclases/api-client";
import { useUser } from "@/store/auth";

export default function DashboardPage() {
  const user = useUser();
  const [data, setData] = useState<DashboardDTO | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    dashboard
      .get()
      .then((d) => setData(d))
      .catch((e) => setError(e?.message ?? "Error al cargar dashboard"))
      .finally(() => setLoading(false));
  }, []);

  if (!user) return null;

  return (
    <div>
      <h1>Dashboard</h1>
      <p>
        Bienvenido <strong>{user.nombre}</strong> ({user.roles.join(", ")})
      </p>
      {loading && <p>Cargando...</p>}
      {error && <p>Error: {error}</p>}
      {data?.rol === "DOCENTE" && (
        <div>
          <h2>Resumen Docente</h2>
          <ul>
            <li>Asignaturas a cargo: {data.asignaturas.length}</li>
            <li>Alumnos: {data.alumnosACargo}</li>
            <li>Mensajes no leídos: {data.mensajesNoLeidos}</li>
            <li>Evaluaciones pendientes: {data.evaluacionesPendientes}</li>
          </ul>
          <h3>Mis asignaturas</h3>
          <ul>
            {data.asignaturas.map((a) => (
              <li key={a.id}>
                {a.nombre} — {a.cursoNombre}
              </li>
            ))}
          </ul>
        </div>
      )}
      {data?.rol === "APODERADO" && (
        <div>
          <h2>Resumen Apoderado</h2>
          <p>Mensajes no leídos: {data.mensajesNoLeidos}</p>
          <h3>Pupilos ({data.pupilos.length})</h3>
          <ul>
            {data.pupilos.map((p) => (
              <li key={p.alumno.id}>
                {p.alumno.nombre} — Asistencia: {p.asistenciaPorcentaje ?? "N/A"}% — Promedio:{" "}
                {p.promedioGeneral ?? "N/A"}
              </li>
            ))}
          </ul>
        </div>
      )}
      {data?.rol === "ESTUDIANTE" && (
        <div>
          <h2>Mi resumen</h2>
          <ul>
            <li>Alumno: {data.alumno?.nombre ?? "(sin asignar)"}</li>
            <li>Curso: {data.alumno?.cursoNombre ?? "N/A"}</li>
            <li>Asistencia: {data.asistenciaPorcentaje ?? "N/A"}%</li>
            <li>Promedio general: {data.promedioGeneral ?? "N/A"}</li>
            <li>Mensajes no leídos: {data.mensajesNoLeidos}</li>
          </ul>
        </div>
      )}
      {data?.rol === "ADMIN" && (
        <div>
          <h2>Resumen Admin</h2>
          <ul>
            <li>Total usuarios: {data.totalUsuarios}</li>
            <li>Total cursos: {data.totalCursos}</li>
            <li>Total asignaturas: {data.totalAsignaturas}</li>
            <li>Mensajes no leídos: {data.mensajesNoLeidos}</li>
          </ul>
        </div>
      )}
    </div>
  );
}
