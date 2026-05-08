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
      <nav>
        <button onClick={() => setTab("asignaturas")} disabled={tab === "asignaturas"}>
          Asignaturas
        </button>
        <button onClick={() => setTab("cursos")} disabled={tab === "cursos"}>
          Cursos
        </button>
        <button onClick={() => setTab("notas")} disabled={tab === "notas"}>
          Notas
        </button>
        <button onClick={() => setTab("rendimiento")} disabled={tab === "rendimiento"}>
          Rendimiento
        </button>
      </nav>
      <hr />
      {tab === "asignaturas" && <AsignaturasTab />}
      {tab === "cursos" && <CursosTab />}
      {tab === "notas" && <NotasTab />}
      {tab === "rendimiento" && <RendimientoTab />}
    </div>
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
    <div>
      <h2>Asignaturas</h2>
      <form onSubmit={crear}>
        <label>Nombre</label>
        <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
        <label>Curso ID</label>
        <input
          type="number"
          value={cursoId}
          onChange={(e) => setCursoId(e.target.value)}
          required
        />
        <label>Docente ID</label>
        <input
          type="number"
          value={docenteId}
          onChange={(e) => setDocenteId(e.target.value)}
          required
        />
        <button type="submit">Crear asignatura</button>
      </form>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
      <button onClick={cargar}>Recargar lista</button>
      {data.length === 0 ? (
        <p>(sin asignaturas)</p>
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
    <div>
      <h2>Cursos</h2>
      <form onSubmit={crear}>
        <label>Nombre (ej. 8°A)</label>
        <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
        <label>Nivel (ej. Básica / Media)</label>
        <input value={nivel} onChange={(e) => setNivel(e.target.value)} required />
        <button type="submit">Crear curso</button>
      </form>
      {error && <p>Error: {error}</p>}
      <button onClick={cargar}>Recargar</button>
      {data.length === 0 ? (
        <p>(sin cursos)</p>
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
    <div>
      <h2>Notas</h2>
      <h3>Registrar nota</h3>
      <form onSubmit={registrar}>
        <label>Alumno ID</label>
        <input
          type="number"
          value={alumnoId}
          onChange={(e) => setAlumnoId(e.target.value)}
          required
        />
        <label>Asignatura ID</label>
        <input
          type="number"
          value={asignaturaId}
          onChange={(e) => setAsignaturaId(e.target.value)}
          required
        />
        <label>Nombre evaluación</label>
        <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
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
        <label>Ponderación (0.0–1.0)</label>
        <input
          type="number"
          step="0.05"
          min="0"
          max="1"
          value={ponderacion}
          onChange={(e) => setPonderacion(e.target.value)}
          required
        />
        <button type="submit">Registrar</button>
      </form>
      {msg && <p>{msg}</p>}
      <hr />
      <h3>Notas por alumno</h3>
      <label>Alumno ID</label>
      <input value={alumnoIdLista} onChange={(e) => setAlumnoIdLista(e.target.value)} />
      <button onClick={buscar}>Buscar</button>
      {error && <p>Error: {error}</p>}
      {data.length === 0 ? (
        <p>(sin notas)</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Asignatura</th>
              <th>Evaluación</th>
              <th>Nota</th>
              <th>Ponderación</th>
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
    <div>
      <h2>Rendimiento por asignatura</h2>
      <label>Asignatura ID</label>
      <input value={asignaturaId} onChange={(e) => setAsignaturaId(e.target.value)} />
      <button onClick={cargar}>Cargar</button>
      {error && <p>Error: {error}</p>}
      {data && (
        <div>
          <h3>{data.asignaturaNombre}</h3>
          <p>Promedio del curso: {data.promedioCurso}</p>
          <p>Cantidad de evaluaciones: {data.cantidadEvaluaciones}</p>
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
