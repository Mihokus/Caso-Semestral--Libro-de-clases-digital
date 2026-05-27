import { type FormEvent, useEffect, useState } from "react";
import {
  academica,
  type AsignaturaDTO,
  type CursoDTO,
  type EvaluacionDTO,
  type RendimientoDTO,
} from "@libroclases/api-client";

type Tab = "asignaturas" | "cursos" | "notas" | "rendimiento";

export default function AcademicaPage() {
  const [tab, setTab] = useState<Tab>("asignaturas");

  return (
    <div>
      <h1>Académica</h1>
      <div className="border-b border-gray-300 flex gap-1 mb-4">
        <TabBtn active={tab === "asignaturas"} onClick={() => setTab("asignaturas")}>
          Asignaturas
        </TabBtn>
        <TabBtn active={tab === "cursos"} onClick={() => setTab("cursos")}>
          Cursos
        </TabBtn>
        <TabBtn active={tab === "notas"} onClick={() => setTab("notas")}>
          Notas
        </TabBtn>
        <TabBtn active={tab === "rendimiento"} onClick={() => setTab("rendimiento")}>
          Rendimiento
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
  const [nombre, setNombre] = useState("");
  const [cursoId, setCursoId] = useState("");
  const [docenteId, setDocenteId] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setData(await academica.listAsignaturas());
    } catch (e: unknown) {
      setError((e as Error).message);
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
      setCursoId("");
      setDocenteId("");
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
      <h2>Asignaturas</h2>
      <form onSubmit={crear} className="mb-3">
        <div className="row">
          <div>
            <label>Nombre</label>
            <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          </div>
          <div>
            <label>Curso ID</label>
            <input
              type="number"
              value={cursoId}
              onChange={(e) => setCursoId(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Docente ID</label>
            <input
              type="number"
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
      <Box error={error} msg={msg} />
      <button onClick={cargar} className="btn btn-secondary mb-2">
        Recargar lista
      </button>
      {data.length === 0 ? (
        <p className="text-sm text-gray-600">(sin asignaturas)</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Curso</th>
              <th>Docente</th>
            </tr>
          </thead>
          <tbody>
            {data.map((a) => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{a.nombre}</td>
                <td>{a.cursoNombre}</td>
                <td>{a.docenteNombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function CursosTab() {
  const [data, setData] = useState<CursoDTO[]>([]);
  const [nombre, setNombre] = useState("");
  const [nivel, setNivel] = useState("");
  const [error, setError] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setData(await academica.listCursos());
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function crear(e: FormEvent) {
    e.preventDefault();
    try {
      await academica.crearCurso({ nombre, nivel });
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
      <form onSubmit={crear} className="mb-3">
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
      <Box error={error} msg={null} />
      <button onClick={cargar} className="btn btn-secondary mb-2">
        Recargar
      </button>
      {data.length === 0 ? (
        <p className="text-sm text-gray-600">(sin cursos)</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Nivel</th>
              <th>Alumnos</th>
            </tr>
          </thead>
          <tbody>
            {data.map((c) => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.nombre}</td>
                <td>{c.nivel}</td>
                <td>{c.cantidadAlumnos ?? "?"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function NotasTab() {
  const [alumnoIdLista, setAlumnoIdLista] = useState("1");
  const [data, setData] = useState<EvaluacionDTO[]>([]);
  const [alumnoId, setAlumnoId] = useState("");
  const [asignaturaId, setAsignaturaId] = useState("");
  const [nombre, setNombre] = useState("");
  const [nota, setNota] = useState("");
  const [ponderacion, setPonderacion] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function buscar() {
    setError(null);
    try {
      setData(await academica.notasAlumno(parseInt(alumnoIdLista, 10)));
    } catch (e: unknown) {
      setError((e as Error).message);
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
          <div>
            <label>Alumno ID</label>
            <input
              type="number"
              value={alumnoId}
              onChange={(e) => setAlumnoId(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Asignatura ID</label>
            <input
              type="number"
              value={asignaturaId}
              onChange={(e) => setAsignaturaId(e.target.value)}
              required
            />
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
      <Box error={null} msg={msg} />
      <hr />
      <h2>Notas por alumno</h2>
      <div className="row">
        <div>
          <label>Alumno ID</label>
          <input value={alumnoIdLista} onChange={(e) => setAlumnoIdLista(e.target.value)} />
        </div>
        <button onClick={buscar} className="btn btn-secondary">
          Buscar
        </button>
      </div>
      <Box error={error} msg={null} />
      {data.length === 0 ? (
        <p className="text-sm text-gray-600 mt-2">(sin notas)</p>
      ) : (
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
                <td>{n.nombre}</td>
                <td>{n.nota}</td>
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
  const [asignaturaId, setAsignaturaId] = useState("1");
  const [data, setData] = useState<RendimientoDTO | null>(null);
  const [error, setError] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setData(await academica.rendimiento(parseInt(asignaturaId, 10)));
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  return (
    <div className="card">
      <h2>Rendimiento por asignatura</h2>
      <div className="row">
        <div>
          <label>Asignatura ID</label>
          <input value={asignaturaId} onChange={(e) => setAsignaturaId(e.target.value)} />
        </div>
        <button onClick={cargar} className="btn btn-secondary">
          Cargar
        </button>
      </div>
      <Box error={error} msg={null} />
      {data && (
        <div className="mt-3">
          <h3>{data.asignaturaNombre}</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-3 my-3">
            <div className="card">
              <div className="stat-label">Promedio del curso</div>
              <div className="stat-value">{data.promedioCurso}</div>
            </div>
            <div className="card">
              <div className="stat-label">Cantidad de evaluaciones</div>
              <div className="stat-value">{data.cantidadEvaluaciones}</div>
            </div>
          </div>
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
                  <td>{a.alumnoNombre}</td>
                  <td>{a.promedio}</td>
                  <td>{a.cantidadEvaluaciones}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function Box({ error, msg }: { error: string | null; msg: string | null }) {
  return (
    <>
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-2 text-sm">
          {error}
        </p>
      )}
      {msg && (
        <p className="text-green-700 bg-green-100 border border-green-300 rounded px-2 py-1 mt-2 text-sm">
          {msg}
        </p>
      )}
    </>
  );
}
