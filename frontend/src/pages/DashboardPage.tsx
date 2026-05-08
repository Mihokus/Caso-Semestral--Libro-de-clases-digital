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
      <p className="text-gray-700 mb-4">
        Bienvenido <strong>{user.nombre}</strong>{" "}
        <span className="text-sm text-gray-600">({user.roles.join(", ")})</span>
      </p>
      {loading && <p className="text-sm text-gray-600">Cargando...</p>}
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 text-sm">
          {error}
        </p>
      )}
      {data?.rol === "DOCENTE" && (
        <div>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-4">
            <Stat label="Asignaturas" value={data.asignaturas.length} />
            <Stat label="Alumnos" value={data.alumnosACargo} />
            <Stat label="Mensajes no leídos" value={data.mensajesNoLeidos} />
            <Stat label="Evaluaciones pendientes" value={data.evaluacionesPendientes} />
          </div>
          <div className="card">
            <h2>Mis asignaturas</h2>
            {data.asignaturas.length === 0 ? (
              <p className="text-sm text-gray-600">(sin asignaturas)</p>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Asignatura</th>
                    <th>Curso</th>
                  </tr>
                </thead>
                <tbody>
                  {data.asignaturas.map((a) => (
                    <tr key={a.id}>
                      <td>{a.nombre}</td>
                      <td>{a.cursoNombre}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}
      {data?.rol === "APODERADO" && (
        <div>
          <div className="grid grid-cols-2 gap-3 mb-4">
            <Stat label="Pupilos" value={data.pupilos.length} />
            <Stat label="Mensajes no leídos" value={data.mensajesNoLeidos} />
          </div>
          <div className="card">
            <h2>Pupilos</h2>
            {data.pupilos.length === 0 ? (
              <p className="text-sm text-gray-600">(sin pupilos)</p>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Alumno</th>
                    <th>Curso</th>
                    <th>Asistencia %</th>
                    <th>Promedio</th>
                  </tr>
                </thead>
                <tbody>
                  {data.pupilos.map((p) => (
                    <tr key={p.alumno.id}>
                      <td>{p.alumno.nombre}</td>
                      <td>{p.alumno.cursoNombre}</td>
                      <td>{p.asistenciaPorcentaje ?? "N/A"}</td>
                      <td>{p.promedioGeneral ?? "N/A"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}
      {data?.rol === "ESTUDIANTE" && (
        <div>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-4">
            <Stat label="Curso" value={data.alumno?.cursoNombre ?? "N/A"} />
            <Stat label="Asistencia %" value={data.asistenciaPorcentaje ?? "N/A"} />
            <Stat label="Promedio general" value={data.promedioGeneral ?? "N/A"} />
            <Stat label="Mensajes no leídos" value={data.mensajesNoLeidos} />
          </div>
          <div className="card">
            <h2>Mis datos</h2>
            <p className="text-sm">Alumno: {data.alumno?.nombre ?? "(sin asignar)"}</p>
            <p className="text-sm">RUT: {data.alumno?.rut ?? "N/A"}</p>
          </div>
        </div>
      )}
      {data?.rol === "ADMIN" && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-4">
          <Stat label="Usuarios" value={data.totalUsuarios} />
          <Stat label="Cursos" value={data.totalCursos} />
          <Stat label="Asignaturas" value={data.totalAsignaturas} />
          <Stat label="Mensajes no leídos" value={data.mensajesNoLeidos} />
        </div>
      )}
    </div>
  );
}

function Stat({ label, value }: { label: string; value: number | string }) {
  return (
    <div className="card">
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
    </div>
  );
}
