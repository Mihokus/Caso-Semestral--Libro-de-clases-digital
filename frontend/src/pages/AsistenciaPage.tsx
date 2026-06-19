import { type FormEvent, useEffect, useState } from "react";
import {
  academica,
  asistencia,
  type AlumnoDTO,
  type AnotacionDTO,
  type AsistenciaDTO,
  type CursoDTO,
  type EstadoAsistencia,
  type TipoAnotacion,
} from "@libroclases/api-client";
import Alert from "@/components/Alert";
import Spinner from "@/components/Spinner";
import EmptyState from "@/components/EmptyState";
import { AttendanceBadge, AnnotationBadge } from "@/components/StatusBadge";

type Tab = "tomar" | "historial" | "anotaciones";

const ESTADOS: EstadoAsistencia[] = ["PRESENTE", "AUSENTE", "JUSTIFICADO"];
const TIPOS: TipoAnotacion[] = ["POSITIVA", "NEGATIVA", "NEUTRAL"];

const ESTADO_SELECT_CLS: Record<EstadoAsistencia, string> = {
  PRESENTE: "bg-green-50 border-green-300 text-green-800",
  AUSENTE: "bg-red-50 border-red-300 text-red-800",
  JUSTIFICADO: "bg-yellow-50 border-yellow-300 text-yellow-800",
};

export default function AsistenciaPage() {
  const [tab, setTab] = useState<Tab>("tomar");

  return (
    <div>
      <h1>Asistencia</h1>
      <div className="border-b border-violet-200 flex gap-1 mb-4">
        <TabBtn active={tab === "tomar"} onClick={() => setTab("tomar")}>
          📋 Tomar asistencia
        </TabBtn>
        <TabBtn active={tab === "historial"} onClick={() => setTab("historial")}>
          🗓️ Historial
        </TabBtn>
        <TabBtn active={tab === "anotaciones"} onClick={() => setTab("anotaciones")}>
          📌 Anotaciones
        </TabBtn>
      </div>
      {tab === "tomar" && <TomarTab />}
      {tab === "historial" && <HistorialTab />}
      {tab === "anotaciones" && <AnotacionesTab />}
    </div>
  );
}

function TabBtn({
  active,
  onClick,
  children,
}: {
  active: boolean;
  onClick: () => void;
  children: React.ReactNode;
}) {
  return (
    <button onClick={onClick} className={`tab-btn ${active ? "tab-active" : ""}`}>
      {children}
    </button>
  );
}

function TomarTab() {
  const [cursos, setCursos] = useState<CursoDTO[]>([]);
  const [cursoId, setCursoId] = useState("");
  const [fecha, setFecha] = useState(new Date().toISOString().slice(0, 10));
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [estados, setEstados] = useState<Record<number, EstadoAsistencia>>({});
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    academica
      .listCursos()
      .then((cs) => {
        setCursos(cs);
        if (cs.length > 0) setCursoId(String(cs[0].id));
      })
      .catch((e) => setError((e as Error).message));
  }, []);

  async function cargarAlumnos() {
    if (!cursoId) return;
    setError(null);
    setMsg(null);
    setLoading(true);
    try {
      const data = await asistencia.listByCurso(parseInt(cursoId, 10));
      setAlumnos(data);
      const init: Record<number, EstadoAsistencia> = {};
      data.forEach((a) => (init[a.id] = "PRESENTE"));
      setEstados(init);
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  function marcarTodos(estado: EstadoAsistencia) {
    const next: Record<number, EstadoAsistencia> = {};
    alumnos.forEach((a) => (next[a.id] = estado));
    setEstados(next);
  }

  async function guardar() {
    setError(null);
    setMsg(null);
    setSaving(true);
    try {
      await asistencia.registrarBulk({
        cursoId: parseInt(cursoId, 10),
        fecha,
        asistencias: alumnos.map((a) => ({ alumnoId: a.id, estado: estados[a.id] })),
      });
      setMsg("Asistencia guardada correctamente");
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setSaving(false);
    }
  }

  const resumen = ESTADOS.map((est) => ({
    est,
    count: alumnos.filter((a) => estados[a.id] === est).length,
  }));

  return (
    <div className="card">
      <h2>Tomar asistencia</h2>
      <div className="row">
        <div>
          <label>Curso</label>
          <select value={cursoId} onChange={(e) => setCursoId(e.target.value)}>
            {cursos.length === 0 && <option value="">(sin cursos)</option>}
            {cursos.map((c) => (
              <option key={c.id} value={c.id}>
                {c.nombre} — {c.nivel}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label>Fecha</label>
          <input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} />
        </div>
        <button onClick={cargarAlumnos} className="btn btn-secondary" disabled={!cursoId || loading}>
          {loading ? "Cargando..." : "Cargar alumnos"}
        </button>
      </div>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />

      {loading && <Spinner text="Cargando alumnos..." />}

      {!loading && alumnos.length > 0 && (
        <>
          <div className="flex flex-wrap items-center gap-2 mt-4 mb-2">
            <span className="text-xs text-slate-500 mr-1">Marcar todos:</span>
            {ESTADOS.map((est) => (
              <button
                key={est}
                onClick={() => marcarTodos(est)}
                className="text-xs px-2.5 py-1 rounded-full border border-violet-200 hover:bg-violet-50 transition-colors"
              >
                {est}
              </button>
            ))}
            <div className="ml-auto flex gap-2 text-xs">
              {resumen.map((r) => (
                <span key={r.est} className="text-slate-500">
                  {r.est}: <strong className="text-slate-700">{r.count}</strong>
                </span>
              ))}
            </div>
          </div>
          <table>
            <thead>
              <tr>
                <th className="w-16">ID</th>
                <th>Alumno</th>
                <th className="w-48">Estado</th>
              </tr>
            </thead>
            <tbody>
              {alumnos.map((a) => (
                <tr key={a.id}>
                  <td className="text-slate-400">{a.id}</td>
                  <td className="font-medium">{a.nombre}</td>
                  <td>
                    <select
                      className={`w-full ${ESTADO_SELECT_CLS[estados[a.id]]}`}
                      value={estados[a.id]}
                      onChange={(e) =>
                        setEstados({ ...estados, [a.id]: e.target.value as EstadoAsistencia })
                      }
                    >
                      {ESTADOS.map((est) => (
                        <option key={est} value={est}>
                          {est}
                        </option>
                      ))}
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <button onClick={guardar} className="btn btn-primary mt-3" disabled={saving}>
            {saving ? "Guardando..." : "Guardar asistencia del día"}
          </button>
        </>
      )}
    </div>
  );
}

function HistorialTab() {
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [alumnoId, setAlumnoId] = useState("");
  const [data, setData] = useState<AsistenciaDTO[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  useEffect(() => {
    asistencia
      .listAlumnos()
      .then((as) => {
        setAlumnos(as);
        if (as.length > 0) setAlumnoId(String(as[0].id));
      })
      .catch((e) => setError((e as Error).message));
  }, []);

  async function buscar() {
    if (!alumnoId) return;
    setError(null);
    setLoading(true);
    try {
      const r = await asistencia.historialAlumno(parseInt(alumnoId, 10));
      setData(r);
      setSearched(true);
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="card">
      <h2>Historial por alumno</h2>
      <div className="row">
        <div className="flex-1 min-w-[200px]">
          <label>Alumno</label>
          <select className="w-full" value={alumnoId} onChange={(e) => setAlumnoId(e.target.value)}>
            {alumnos.length === 0 && <option value="">(sin alumnos)</option>}
            {alumnos.map((a) => (
              <option key={a.id} value={a.id}>
                {a.nombre} — {a.cursoNombre}
              </option>
            ))}
          </select>
        </div>
        <button onClick={buscar} className="btn btn-secondary" disabled={!alumnoId || loading}>
          Buscar
        </button>
      </div>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      {loading && <Spinner text="Buscando..." />}
      {!loading && searched && data.length === 0 && (
        <EmptyState emoji="🗓️" message="Sin registros de asistencia" />
      )}
      {data.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Alumno</th>
              <th>Curso</th>
              <th>Estado</th>
              <th>Registrado por</th>
            </tr>
          </thead>
          <tbody>
            {data.map((a) => (
              <tr key={a.id}>
                <td>{a.fecha}</td>
                <td className="font-medium">{a.alumnoNombre}</td>
                <td>{a.cursoNombre}</td>
                <td><AttendanceBadge status={a.estado} /></td>
                <td className="text-slate-500">{a.registradoPor?.nombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function AnotacionesTab() {
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [alumnoId, setAlumnoId] = useState("");
  const [tipo, setTipo] = useState<TipoAnotacion>("POSITIVA");
  const [descripcion, setDescripcion] = useState("");
  const [data, setData] = useState<AnotacionDTO[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    asistencia
      .listAlumnos()
      .then((as) => {
        setAlumnos(as);
        if (as.length > 0) setAlumnoId(String(as[0].id));
      })
      .catch((e) => setError((e as Error).message));
  }, []);

  async function cargar(id?: string) {
    const target = id ?? alumnoId;
    if (!target) return;
    setError(null);
    setLoading(true);
    try {
      setData(await asistencia.anotacionesAlumno(parseInt(target, 10)));
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  async function guardar(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      await asistencia.registrarAnotacion({
        alumnoId: parseInt(alumnoId, 10),
        tipo,
        descripcion,
      });
      setMsg("Anotación guardada");
      setDescripcion("");
      cargar();
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  return (
    <div className="card">
      <h2>Anotaciones</h2>
      <form onSubmit={guardar}>
        <div className="row">
          <div className="flex-1 min-w-[180px]">
            <label>Alumno</label>
            <select className="w-full" value={alumnoId} onChange={(e) => setAlumnoId(e.target.value)} required>
              {alumnos.length === 0 && <option value="">(sin alumnos)</option>}
              {alumnos.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.nombre} — {a.cursoNombre}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label>Tipo</label>
            <select value={tipo} onChange={(e) => setTipo(e.target.value as TipoAnotacion)}>
              {TIPOS.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>
          <div className="flex-1 min-w-[200px]">
            <label>Descripción</label>
            <input
              className="w-full"
              value={descripcion}
              onChange={(e) => setDescripcion(e.target.value)}
              required
            />
          </div>
        </div>
        <div className="mt-3 flex gap-2">
          <button type="submit" className="btn btn-primary">
            Registrar anotación
          </button>
          <button type="button" onClick={() => cargar()} className="btn btn-secondary">
            Ver anotaciones
          </button>
        </div>
      </form>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />
      {loading && <Spinner text="Cargando..." />}
      {!loading && data.length === 0 ? (
        <EmptyState emoji="📌" message="Sin anotaciones para este alumno" />
      ) : (
        !loading && (
          <table>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Alumno</th>
                <th>Tipo</th>
                <th>Descripción</th>
                <th>Por</th>
              </tr>
            </thead>
            <tbody>
              {data.map((a) => (
                <tr key={a.id}>
                  <td>{a.fecha}</td>
                  <td className="font-medium">{a.alumnoNombre}</td>
                  <td><AnnotationBadge type={a.tipo} /></td>
                  <td>{a.descripcion}</td>
                  <td className="text-slate-500">{a.registradoPor?.nombre}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      )}
    </div>
  );
}
