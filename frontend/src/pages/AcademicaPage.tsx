import { type FormEvent, useEffect, useState } from "react";
import {
  academica,
  asistencia,
  type AlumnoDTO,
  type AsignaturaDTO,
  type CursoDTO,
  type EvaluacionDTO,
  type RendimientoDTO,
} from "@libroclases/api-client";
import Alert from "@/components/Alert";
import Spinner from "@/components/Spinner";
import EmptyState from "@/components/EmptyState";
import { GradeBadge } from "@/components/StatusBadge";

type Tab = "asignaturas" | "cursos" | "notas" | "rendimiento";

export default function AcademicaPage() {
  const [tab, setTab] = useState<Tab>("asignaturas");

  return (
    <div>
      <h1>Académica</h1>
      <div className="border-b border-violet-200 flex gap-1 mb-4 flex-wrap">
        <TabBtn active={tab === "asignaturas"} onClick={() => setTab("asignaturas")}>
          📚 Asignaturas
        </TabBtn>
        <TabBtn active={tab === "cursos"} onClick={() => setTab("cursos")}>
          🏫 Cursos
        </TabBtn>
        <TabBtn active={tab === "notas"} onClick={() => setTab("notas")}>
          📝 Notas
        </TabBtn>
        <TabBtn active={tab === "rendimiento"} onClick={() => setTab("rendimiento")}>
          📈 Rendimiento
        </TabBtn>
      </div>
      {tab === "asignaturas" && <AsignaturasTab />}
      {tab === "cursos" && <CursosTab />}
      {tab === "notas" && <NotasTab />}
      {tab === "rendimiento" && <RendimientoTab />}
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

function AsignaturasTab() {
  const [data, setData] = useState<AsignaturaDTO[]>([]);
  const [cursos, setCursos] = useState<CursoDTO[]>([]);
  const [nombre, setNombre] = useState("");
  const [cursoId, setCursoId] = useState("");
  const [docenteId, setDocenteId] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function cargar() {
    setError(null);
    setLoading(true);
    try {
      setData(await academica.listAsignaturas());
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  async function crear(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      await academica.crearAsignatura({
        nombre,
        cursoId: parseInt(cursoId, 10),
        docenteId: parseInt(docenteId, 10),
      });
      setMsg("Asignatura creada");
      setNombre("");
      setDocenteId("");
      cargar();
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  useEffect(() => {
    cargar();
    academica.listCursos().then((cs) => {
      setCursos(cs);
      if (cs.length > 0) setCursoId(String(cs[0].id));
    }).catch(() => { /* opcional */ });
  }, []);

  return (
    <div className="card">
      <h2>Asignaturas</h2>
      <form onSubmit={crear} className="mb-4">
        <div className="row">
          <div>
            <label>Nombre</label>
            <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          </div>
          <div>
            <label>Curso</label>
            <select value={cursoId} onChange={(e) => setCursoId(e.target.value)} required>
              {cursos.length === 0 && <option value="">(sin cursos)</option>}
              {cursos.map((c) => (
                <option key={c.id} value={c.id}>{c.nombre} — {c.nivel}</option>
              ))}
            </select>
          </div>
          <div>
            <label>Docente (ID)</label>
            <input
              type="number"
              placeholder="Ej: 2"
              value={docenteId}
              onChange={(e) => setDocenteId(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Crear asignatura
          </button>
        </div>
      </form>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />
      {loading && <Spinner text="Cargando asignaturas..." />}
      {!loading && data.length === 0 ? (
        <EmptyState emoji="📚" message="Sin asignaturas" />
      ) : (
        !loading && (
          <table>
            <thead>
              <tr>
                <th className="w-16">ID</th>
                <th>Nombre</th>
                <th>Curso</th>
                <th>Docente</th>
              </tr>
            </thead>
            <tbody>
              {data.map((a) => (
                <tr key={a.id}>
                  <td className="text-slate-400">{a.id}</td>
                  <td className="font-medium">{a.nombre}</td>
                  <td>{a.cursoNombre}</td>
                  <td>{a.docenteNombre}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      )}
    </div>
  );
}

function CursosTab() {
  const [data, setData] = useState<CursoDTO[]>([]);
  const [nombre, setNombre] = useState("");
  const [nivel, setNivel] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function cargar() {
    setError(null);
    setLoading(true);
    try {
      setData(await academica.listCursos());
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  async function crear(e: FormEvent) {
    e.preventDefault();
    try {
      await academica.crearCurso({ nombre, nivel });
      setMsg("Curso creado");
      setNombre("");
      setNivel("");
      cargar();
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  useEffect(() => {
    cargar();
  }, []);

  return (
    <div className="card">
      <h2>Cursos</h2>
      <form onSubmit={crear} className="mb-4">
        <div className="row">
          <div>
            <label>Nombre (ej. 8°A)</label>
            <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          </div>
          <div>
            <label>Nivel</label>
            <input value={nivel} onChange={(e) => setNivel(e.target.value)} required />
          </div>
          <button type="submit" className="btn btn-primary">
            Crear curso
          </button>
        </div>
      </form>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />
      {loading && <Spinner text="Cargando cursos..." />}
      {!loading && data.length === 0 ? (
        <EmptyState emoji="🏫" message="Sin cursos" />
      ) : (
        !loading && (
          <table>
            <thead>
              <tr>
                <th className="w-16">ID</th>
                <th>Nombre</th>
                <th>Nivel</th>
                <th>Alumnos</th>
              </tr>
            </thead>
            <tbody>
              {data.map((c) => (
                <tr key={c.id}>
                  <td className="text-slate-400">{c.id}</td>
                  <td className="font-medium">{c.nombre}</td>
                  <td>{c.nivel}</td>
                  <td>{c.cantidadAlumnos ?? "?"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      )}
    </div>
  );
}

function NotasTab() {
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [alumnoIdLista, setAlumnoIdLista] = useState("");
  const [data, setData] = useState<EvaluacionDTO[]>([]);
  const [alumnoId, setAlumnoId] = useState("");
  const [asignaturaId, setAsignaturaId] = useState("");
  const [nombre, setNombre] = useState("");
  const [nota, setNota] = useState("");
  const [ponderacion, setPonderacion] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  useEffect(() => {
    asistencia.listAlumnos().then((as) => {
      setAlumnos(as);
      if (as.length > 0) {
        setAlumnoId(String(as[0].id));
        setAlumnoIdLista(String(as[0].id));
      }
    }).catch((e) => setError((e as Error).message));
    academica.listAsignaturas().then((ass) => {
      setAsignaturas(ass);
      if (ass.length > 0) setAsignaturaId(String(ass[0].id));
    }).catch(() => { /* opcional */ });
  }, []);

  async function buscar() {
    if (!alumnoIdLista) return;
    setError(null);
    setLoading(true);
    try {
      setData(await academica.notasAlumno(parseInt(alumnoIdLista, 10)));
      setSearched(true);
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  async function registrar(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      await academica.registrarNota({
        alumnoId: parseInt(alumnoId, 10),
        asignaturaId: parseInt(asignaturaId, 10),
        nombre,
        nota: parseFloat(nota),
        ponderacion: parseFloat(ponderacion),
      });
      setMsg("Nota registrada");
      setNombre("");
      setNota("");
      setPonderacion("");
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  return (
    <div className="card">
      <h2>Registrar nota</h2>
      <form onSubmit={registrar} className="mb-3">
        <div className="row">
          <div className="min-w-[160px]">
            <label>Alumno</label>
            <select className="w-full" value={alumnoId} onChange={(e) => setAlumnoId(e.target.value)} required>
              {alumnos.length === 0 && <option value="">(sin alumnos)</option>}
              {alumnos.map((a) => (
                <option key={a.id} value={a.id}>{a.nombre}</option>
              ))}
            </select>
          </div>
          <div className="min-w-[160px]">
            <label>Asignatura</label>
            <select className="w-full" value={asignaturaId} onChange={(e) => setAsignaturaId(e.target.value)} required>
              {asignaturas.length === 0 && <option value="">(sin asignaturas)</option>}
              {asignaturas.map((a) => (
                <option key={a.id} value={a.id}>{a.nombre}</option>
              ))}
            </select>
          </div>
          <div>
            <label>Nombre evaluación</label>
            <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          </div>
          <div>
            <label>Nota (1.0–7.0)</label>
            <input
              type="number"
              step="0.1"
              min="1"
              max="7"
              value={nota}
              onChange={(e) => setNota(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Ponderación (0–1)</label>
            <input
              type="number"
              step="0.05"
              min="0"
              max="1"
              value={ponderacion}
              onChange={(e) => setPonderacion(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Registrar
          </button>
        </div>
      </form>
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />
      <hr />
      <h2>Notas por alumno</h2>
      <div className="row">
        <div className="flex-1 min-w-[200px]">
          <label>Alumno</label>
          <select className="w-full" value={alumnoIdLista} onChange={(e) => setAlumnoIdLista(e.target.value)}>
            {alumnos.length === 0 && <option value="">(sin alumnos)</option>}
            {alumnos.map((a) => (
              <option key={a.id} value={a.id}>{a.nombre} — {a.cursoNombre}</option>
            ))}
          </select>
        </div>
        <button onClick={buscar} className="btn btn-secondary" disabled={!alumnoIdLista || loading}>
          Buscar
        </button>
      </div>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      {loading && <Spinner text="Cargando notas..." />}
      {!loading && searched && data.length === 0 && (
        <EmptyState emoji="📝" message="Sin notas registradas" />
      )}
      {!loading && data.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Asignatura</th>
              <th>Evaluación</th>
              <th>Nota</th>
              <th>Pond.</th>
            </tr>
          </thead>
          <tbody>
            {data.map((n) => (
              <tr key={n.id}>
                <td>{n.fecha}</td>
                <td>{n.asignaturaNombre}</td>
                <td className="font-medium">{n.nombre}</td>
                <td><GradeBadge grade={n.nota} /></td>
                <td>{n.ponderacion}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function RendimientoTab() {
  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [asignaturaId, setAsignaturaId] = useState("");
  const [data, setData] = useState<RendimientoDTO | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    academica.listAsignaturas().then((ass) => {
      setAsignaturas(ass);
      if (ass.length > 0) setAsignaturaId(String(ass[0].id));
    }).catch((e) => setError((e as Error).message));
  }, []);

  async function cargar() {
    if (!asignaturaId) return;
    setError(null);
    setLoading(true);
    try {
      setData(await academica.rendimiento(parseInt(asignaturaId, 10)));
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="card">
      <h2>Rendimiento por asignatura</h2>
      <div className="row">
        <div className="flex-1 min-w-[200px]">
          <label>Asignatura</label>
          <select className="w-full" value={asignaturaId} onChange={(e) => setAsignaturaId(e.target.value)}>
            {asignaturas.length === 0 && <option value="">(sin asignaturas)</option>}
            {asignaturas.map((a) => (
              <option key={a.id} value={a.id}>{a.nombre} — {a.cursoNombre}</option>
            ))}
          </select>
        </div>
        <button onClick={cargar} className="btn btn-secondary" disabled={!asignaturaId || loading}>
          Cargar
        </button>
      </div>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      {loading && <Spinner text="Cargando rendimiento..." />}
      {!loading && data && (
        <div className="mt-3">
          <h3>{data.asignaturaNombre}</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3 my-3">
            <div className="card !p-4 border-l-4 border-l-violet-500">
              <div className="stat-label">Promedio del curso</div>
              <div className="stat-value">{data.promedioCurso}</div>
            </div>
            <div className="card !p-4 border-l-4 border-l-blue-500">
              <div className="stat-label">Cantidad de evaluaciones</div>
              <div className="stat-value">{data.cantidadEvaluaciones}</div>
            </div>
          </div>
          {data.alumnos.length === 0 ? (
            <EmptyState emoji="📈" message="Sin alumnos con evaluaciones" />
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Alumno</th>
                  <th>Promedio</th>
                  <th># evals</th>
                </tr>
              </thead>
              <tbody>
                {data.alumnos.map((a) => (
                  <tr key={a.alumnoId}>
                    <td className="font-medium">{a.alumnoNombre}</td>
                    <td><GradeBadge grade={a.promedio} /></td>
                    <td>{a.cantidadEvaluaciones}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}
