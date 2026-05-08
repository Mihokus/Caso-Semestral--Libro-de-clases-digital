import { type FormEvent, useEffect, useState } from "react";
import { mensajeria, type MensajeDTO } from "@libroclases/api-client";

type Tab = "inbox" | "enviados" | "nuevo";
type ModoNuevo = "directo" | "comunicado";

export default function MensajesPage() {
  const [tab, setTab] = useState<Tab>("inbox");

  return (
    <div>
      <h1>Mensajes</h1>
      <nav>
        <button onClick={() => setTab("inbox")} disabled={tab === "inbox"}>
          Inbox
        </button>
        <button onClick={() => setTab("enviados")} disabled={tab === "enviados"}>
          Enviados
        </button>
        <button onClick={() => setTab("nuevo")} disabled={tab === "nuevo"}>
          Nuevo
        </button>
      </nav>
      <hr />
      {tab === "inbox" && <ListaTab loader={mensajeria.inbox} />}
      {tab === "enviados" && <ListaTab loader={mensajeria.enviados} />}
      {tab === "nuevo" && <NuevoTab />}
    </div>
  );
}

function ListaTab({ loader }: { loader: () => Promise<MensajeDTO[]> }) {
  const [data, setData] = useState<MensajeDTO[]>([]);
  const [error, setError] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setData(await loader());
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function marcarLeido(id: number) {
    try {
      await mensajeria.marcarLeido(id);
      cargar();
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function eliminar(id: number) {
    if (!confirm("¿Eliminar mensaje?")) return;
    try {
      await mensajeria.eliminar(id);
      cargar();
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  useEffect(() => {
    cargar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loader]);

  return (
    <div>
      <button onClick={cargar}>Recargar</button>
      {error && <p>Error: {error}</p>}
      {data.length === 0 ? (
        <p>(sin mensajes)</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Tipo</th>
              <th>De</th>
              <th>Para</th>
              <th>Título</th>
              <th>Contenido</th>
              <th>Leído</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((m) => (
              <tr key={m.id}>
                <td>{m.fechaEnvio}</td>
                <td>{m.tipo}</td>
                <td>{m.remitente?.nombre}</td>
                <td>{m.destinatario?.nombre ?? `curso ${m.cursoId}`}</td>
                <td>{m.titulo}</td>
                <td>{m.contenido}</td>
                <td>{m.leido ? "✓" : ""}</td>
                <td>
                  {!m.leido && (
                    <button onClick={() => marcarLeido(m.id)}>Marcar leído</button>
                  )}
                  <button onClick={() => eliminar(m.id)}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

function NuevoTab() {
  const [modo, setModo] = useState<ModoNuevo>("directo");
  const [destinatarioId, setDestinatarioId] = useState("");
  const [cursoId, setCursoId] = useState("");
  const [titulo, setTitulo] = useState("");
  const [contenido, setContenido] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function enviar(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      if (modo === "directo") {
        await mensajeria.enviarDirecto({
          destinatarioId: parseInt(destinatarioId, 10),
          titulo,
          contenido,
        });
      } else {
        await mensajeria.enviarComunicado({
          cursoId: parseInt(cursoId, 10),
          titulo,
          contenido,
        });
      }
      setMsg("Mensaje enviado");
      setTitulo("");
      setContenido("");
      setDestinatarioId("");
      setCursoId("");
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  return (
    <div>
      <h2>Nuevo mensaje</h2>
      <div>
        <label>
          <input
            type="radio"
            checked={modo === "directo"}
            onChange={() => setModo("directo")}
          />{" "}
          Directo
        </label>
        <label>
          <input
            type="radio"
            checked={modo === "comunicado"}
            onChange={() => setModo("comunicado")}
          />{" "}
          Comunicado al curso
        </label>
      </div>
      <form onSubmit={enviar}>
        {modo === "directo" ? (
          <div>
            <label>Destinatario ID</label>
            <input
              type="number"
              value={destinatarioId}
              onChange={(e) => setDestinatarioId(e.target.value)}
              required
            />
          </div>
        ) : (
          <div>
            <label>Curso ID</label>
            <input
              type="number"
              value={cursoId}
              onChange={(e) => setCursoId(e.target.value)}
              required
            />
          </div>
        )}
        <div>
          <label>Título</label>
          <input value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
        </div>
        <div>
          <label>Contenido</label>
          <textarea
            value={contenido}
            onChange={(e) => setContenido(e.target.value)}
            required
          />
        </div>
        <button type="submit">Enviar</button>
      </form>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
    </div>
  );
}
