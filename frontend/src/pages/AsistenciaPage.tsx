import { type FormEvent, useEffect, useState } from "react";
import {
  asistencia,
  type AlumnoDTO,
  type AnotacionDTO,
  type AsistenciaDTO,
  type EstadoAsistencia,
  type TipoAnotacion,
} from "@libroclases/api-client";

type Tab = "tomar" | "historial" | "anotaciones";

const ESTADOS: EstadoAsistencia[] = ["PRESENTE", "AUSENTE", "JUSTIFICADO"];
const TIPOS: TipoAnotacion[] = ["POSITIVA", "NEGATIVA", "NEUTRAL"];

export default function AsistenciaPage() {
  const [tab, setTab] = useState<Tab>("tomar");

  return (
    <div>
      <h1>Asistencia</h1>
      <nav>
        <button onClick={() => setTab("tomar")} disabled={tab === "tomar"}>
          Tomar asistencia
        </button>
        <button onClick={() => setTab("historial")} disabled={tab === "historial"}>
          Historial
        </button>
        <button onClick={() => setTab("anotaciones")} disabled={tab === "anotaciones"}>
          Anotaciones
        </button>
      </nav>
      <hr />
      {tab === "tomar" && <TomarTab />}
      {tab === "historial" && <HistorialTab />}
      {tab === "anotaciones" && <AnotacionesTab />}
    </div>
  );
}

function TomarTab() {
  const [cursoId, setCursoId] = useState("1");
  const [fecha, setFecha] = useState(new Date().toISOString().slice(0, 10));
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [estados, setEstados] = useState<Record<number, EstadoAsistencia>>({});
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function cargarAlumnos() {
    setError(null);
    setMsg(null);
    try {
      const data = await asistencia.listByCurso(parseInt(cursoId, 10));
      setAlumnos(data);
      const init: Record<number, EstadoAsistencia> = {};
      data.forEach((a) => (init[a.id] = "PRESENTE"));
      setEstados(init);
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function guardar() {
    setError(null);
    setMsg(null);
    try {
      await asistencia.registrarBulk({
        cursoId: parseInt(cursoId, 10),
        fecha,
        asistencias: alumnos.map((a) => ({ alumnoId: a.id, estado: estados[a.id] })),
      });
      setMsg("Asistencia guardada");
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h2>Tomar asistencia</h2>
      <div>
        <label>Curso ID</label>
        <input value={cursoId} onChange={(e) => setCursoId(e.target.value)} />
        <label>Fecha</label>
        <input type="date" value={fecha} onChange={(e) => setFecha(e.target.value)} />
        <button onClick={cargarAlumnos}>Cargar alumnos</button>
      </div>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
      {alumnos.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Alumno</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {alumnos.map((a) => (
              <tr key={a.id}>
                <td>{a.id}</td>
                <td>{a.nombre}</td>
                <td>
                  <select
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
      )}
      {alumnos.length > 0 && <button onClick={guardar}>Guardar asistencia del día</button>}
    </div>
  );
}

function HistorialTab() {
  const [alumnoId, setAlumnoId] = useState("1");
  const [data, setData] = useState<AsistenciaDTO[]>([]);
  const [error, setError] = useState<string | null>(null);

  async function buscar() {
    setError(null);
    try {
      const r = await asistencia.historialAlumno(parseInt(alumnoId, 10));
      setData(r);
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h2>Historial de asistencia (por alumno)</h2>
      <label>Alumno ID</label>
      <input value={alumnoId} onChange={(e) => setAlumnoId(e.target.value)} />
      <button onClick={buscar}>Buscar</button>
      {error && <p>Error: {error}</p>}
      {data.length === 0 ? (
        <p>(sin registros)</p>
      ) : (
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
                <td>{a.alumnoNombre}</td>
                <td>{a.cursoNombre}</td>
                <td>{a.estado}</td>
                <td>{a.registradoPor?.nombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function AnotacionesTab() {
  const [alumnoId, setAlumnoId] = useState("1");
  const [tipo, setTipo] = useState<TipoAnotacion>("POSITIVA");
  const [descripcion, setDescripcion] = useState("");
  const [data, setData] = useState<AnotacionDTO[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setData(await asistencia.anotacionesAlumno(parseInt(alumnoId, 10)));
    } catch (e: unknown) {
      setError((e as Error).message);
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

  useEffect(() => {
    cargar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div>
      <h2>Anotaciones</h2>
      <form onSubmit={guardar}>
        <label>Alumno ID</label>
        <input value={alumnoId} onChange={(e) => setAlumnoId(e.target.value)} required />
        <label>Tipo</label>
        <select value={tipo} onChange={(e) => setTipo(e.target.value as TipoAnotacion)}>
          {TIPOS.map((t) => (
            <option key={t} value={t}>
              {t}
            </option>
          ))}
        </select>
        <label>Descripción</label>
        <input
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
          required
        />
        <button type="submit">Registrar anotación</button>
        <button type="button" onClick={cargar}>
          Recargar lista
        </button>
      </form>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
      {data.length === 0 ? (
        <p>(sin anotaciones)</p>
      ) : (
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
                <td>{a.alumnoNombre}</td>
                <td>{a.tipo}</td>
                <td>{a.descripcion}</td>
                <td>{a.registradoPor?.nombre}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
