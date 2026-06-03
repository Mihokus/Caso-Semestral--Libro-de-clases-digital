import { useEffect, useState } from "react";
import { dashboard, type DashboardDTO } from "@libroclases/api-client";
import { useUser } from "@/store/auth";
import Spinner from "@/components/Spinner";
import Alert from "@/components/Alert";
import EmptyState from "@/components/EmptyState";

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
      <p className="text-slate-600 mb-5">
        Bienvenido <strong className="text-violet-800">{user.nombre}</strong>{" "}
        <span className="text-sm text-violet-500">({user.roles.join(", ")})</span>
      </p>
      {loading && <Spinner text="Cargando dashboard..." large />}
      <Alert type="error" message={error} onClose={() => setError(null)} autoClose={false} />

      {data?.rol === "DOCENTE" && (
        <div>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-5">
            <Stat emoji="📚" label="Asignaturas" value={data.asignaturas.length} accent="violet" />
            <Stat emoji="👥" label="Alumnos" value={data.alumnosACargo} accent="blue" />
            <Stat emoji="✉️" label="Mensajes no leídos" value={data.mensajesNoLeidos} accent="red" />
            <Stat emoji="📝" label="Evaluaciones pendientes" value={data.evaluacionesPendientes} accent="yellow" />
          </div>
          <div className="card">
            <h2>Mis asignaturas</h2>
            {data.asignaturas.length === 0 ? (
              <EmptyState emoji="📚" message="Sin asignaturas asignadas" />
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
                      <td className="font-medium">{a.nombre}</td>
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
          <div className="grid grid-cols-2 gap-3 mb-5">
            <Stat emoji="👨‍👧" label="Pupilos" value={data.pupilos.length} accent="violet" />
            <Stat emoji="✉️" label="Mensajes no leídos" value={data.mensajesNoLeidos} accent="red" />
          </div>
          <div className="card">
            <h2>Pupilos</h2>
            {data.pupilos.length === 0 ? (
              <EmptyState emoji="👨‍👧" message="Sin pupilos registrados" />
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
                      <td className="font-medium">{p.alumno.nombre}</td>
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
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-5">
            <Stat emoji="🏫" label="Curso" value={data.alumno?.cursoNombre ?? "N/A"} accent="violet" />
            <Stat emoji="📋" label="Asistencia %" value={data.asistenciaPorcentaje ?? "N/A"} accent="green" />
            <Stat emoji="📈" label="Promedio general" value={data.promedioGeneral ?? "N/A"} accent="blue" />
            <Stat emoji="✉️" label="Mensajes no leídos" value={data.mensajesNoLeidos} accent="red" />
          </div>
          <div className="card">
            <h2>Mis datos</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 text-sm">
              <div className="bg-violet-50 rounded-lg px-3 py-2">
                <span className="text-violet-500">Alumno:</span>{" "}
                <span className="font-medium">{data.alumno?.nombre ?? "(sin asignar)"}</span>
              </div>
              <div className="bg-violet-50 rounded-lg px-3 py-2">
                <span className="text-violet-500">RUT:</span>{" "}
                <span className="font-medium">{data.alumno?.rut ?? "N/A"}</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {data?.rol === "ADMIN" && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-5">
          <Stat emoji="👥" label="Usuarios" value={data.totalUsuarios} accent="violet" />
          <Stat emoji="🏫" label="Cursos" value={data.totalCursos} accent="blue" />
          <Stat emoji="📚" label="Asignaturas" value={data.totalAsignaturas} accent="green" />
          <Stat emoji="✉️" label="Mensajes no leídos" value={data.mensajesNoLeidos} accent="red" />
        </div>
      )}
    </div>
  );
}

const ACCENTS: Record<string, string> = {
  violet: "border-l-violet-500",
  blue: "border-l-blue-500",
  green: "border-l-green-500",
  red: "border-l-red-500",
  yellow: "border-l-yellow-500",
};

function Stat({
  emoji,
  label,
  value,
  accent = "violet",
}: {
  emoji: string;
  label: string;
  value: number | string;
  accent?: string;
}) {
  return (
    <div className={`card !p-4 border-l-4 ${ACCENTS[accent]}`}>
      <div className="flex items-center justify-between">
        <div className="stat-label">{label}</div>
        <span className="text-xl">{emoji}</span>
      </div>
      <div className="stat-value mt-1">{value}</div>
    </div>
  );
}
