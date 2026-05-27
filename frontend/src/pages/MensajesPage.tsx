import { type FormEvent, useEffect, useState } from "react";
import { mensajeria, type MensajeDTO } from "@libroclases/api-client";

type Tab = "inbox" | "enviados" | "nuevo";
type ModoNuevo = "directo" | "comunicado";

export default function MensajesPage() {
  const [tab, setTab] = useState<Tab>("inbox");

  return (
    <div>
      <h1>Mensajes</h1>
      <div className="border-b border-gray-300 flex gap-1 mb-4">
        <TabBtn active={tab === "inbox"} onClick={() => setTab("inbox")}>
          Inbox
        </TabBtn>
        <TabBtn active={tab === "enviados"} onClick={() => setTab("enviados")}>
          Enviados
        </TabBtn>
        <TabBtn active={tab === "nuevo"} onClick={() => setTab("nuevo")}>
          Nuevo
        </TabBtn>
      </div>
      {tab === "inbox" && <ListaTab loader={mensajeria.inbox} />}
      {tab === "enviados" && <ListaTab loader={mensajeria.enviados} />}
      {tab === "nuevo" && <NuevoTab />}
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
    <div className="card">
      <button onClick={cargar} className="btn btn-secondary mb-2">
        Recargar
      </button>
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-2 text-sm">
          {error}
        </p>
      )}
      {data.length === 0 ? (
        <p className="text-sm text-gray-600">(sin mensajes)</p>
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
                  <div className="flex gap-1">
                    {!m.leido && (
                      <button
                        onClick={() => marcarLeido(m.id)}
                        className="btn btn-secondary"
                      >
                        Leído
                      </button>
                    )}
                    <button onClick={() => eliminar(m.id)} className="btn btn-danger">
                      Eliminar
                    </button>
                  </div>
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
    <div className="card">
      <h2>Nuevo mensaje</h2>
      <div className="flex gap-4 mb-3">
        <label className="flex items-center gap-1">
          <input
            type="radio"
            checked={modo === "directo"}
            onChange={() => setModo("directo")}
          />
          <span className="text-sm">Directo</span>
        </label>
        <label className="flex items-center gap-1">
          <input
            type="radio"
            checked={modo === "comunicado"}
            onChange={() => setModo("comunicado")}
          />
          <span className="text-sm">Comunicado al curso</span>
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
        <label>Título</label>
        <input
          className="w-full"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
          required
        />
        <label>Contenido</label>
        <textarea
          className="w-full"
          rows={4}
          value={contenido}
          onChange={(e) => setContenido(e.target.value)}
          required
        />
        <div className="mt-3">
          <button type="submit" className="btn btn-primary">
            Enviar
          </button>
        </div>
      </form>
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
    </div>
  );
}
