import { type FormEvent, useEffect, useState } from "react";
import { academica, mensajeria, type CursoDTO, type MensajeDTO } from "@libroclases/api-client";
import Alert from "@/components/Alert";
import Spinner from "@/components/Spinner";
import EmptyState from "@/components/EmptyState";
import Modal from "@/components/Modal";

type Tab = "inbox" | "enviados" | "nuevo";
type ModoNuevo = "directo" | "comunicado";

export default function MensajesPage() {
  const [tab, setTab] = useState<Tab>("inbox");

  return (
    <div>
      <h1>Mensajes</h1>
      <div className="border-b border-violet-200 flex gap-1 mb-4">
        <TabBtn active={tab === "inbox"} onClick={() => setTab("inbox")}>
          📥 Inbox
        </TabBtn>
        <TabBtn active={tab === "enviados"} onClick={() => setTab("enviados")}>
          📤 Enviados
        </TabBtn>
        <TabBtn active={tab === "nuevo"} onClick={() => setTab("nuevo")}>
          ✏️ Nuevo
        </TabBtn>
      </div>
      {tab === "inbox" && <ListaTab loader={mensajeria.inbox} showRead />}
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

function ListaTab({ loader, showRead = false }: { loader: () => Promise<MensajeDTO[]>; showRead?: boolean }) {
  const [data, setData] = useState<MensajeDTO[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [expanded, setExpanded] = useState<number | null>(null);
  const [toDelete, setToDelete] = useState<number | null>(null);

  async function cargar() {
    setError(null);
    setLoading(true);
    try {
      setData(await loader());
    } catch (e: unknown) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
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

  async function eliminar() {
    if (toDelete == null) return;
    try {
      await mensajeria.eliminar(toDelete);
      setToDelete(null);
      cargar();
    } catch (e: unknown) {
      setError((e as Error).message);
      setToDelete(null);
    }
  }

  useEffect(() => {
    cargar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loader]);

  const noLeidos = data.filter((m) => !m.leido).length;

  return (
    <div className="card">
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center gap-2">
          {showRead && noLeidos > 0 && (
            <span className="bg-violet-100 text-violet-700 text-xs font-semibold px-2.5 py-1 rounded-full">
              {noLeidos} sin leer
            </span>
          )}
        </div>
        <button onClick={cargar} className="btn btn-secondary !py-1.5 !text-xs">
          🔄 Recargar
        </button>
      </div>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      {loading && <Spinner text="Cargando mensajes..." />}
      {!loading && data.length === 0 && <EmptyState emoji="📭" message="No hay mensajes" />}
      {!loading && data.length > 0 && (
        <div className="divide-y divide-violet-100 border border-violet-100 rounded-lg overflow-hidden">
          {data.map((m) => {
            const isUnread = showRead && !m.leido;
            const isOpen = expanded === m.id;
            return (
              <div
                key={m.id}
                className={`px-4 py-3 transition-colors cursor-pointer ${
                  isUnread ? "bg-violet-50 hover:bg-violet-100" : "bg-white hover:bg-slate-50"
                }`}
                onClick={() => setExpanded(isOpen ? null : m.id)}
              >
                <div className="flex items-center gap-2">
                  {isUnread && <span className="w-2 h-2 bg-violet-600 rounded-full flex-shrink-0" />}
                  <span className={`text-sm ${isUnread ? "font-bold text-slate-900" : "font-medium text-slate-700"}`}>
                    {m.titulo}
                  </span>
                  <span className={`text-[10px] px-1.5 py-0.5 rounded-full ${
                    m.tipo === "COMUNICADO" ? "bg-blue-100 text-blue-700" : "bg-slate-100 text-slate-600"
                  }`}>
                    {m.tipo}
                  </span>
                  <span className="text-xs text-slate-400 ml-auto whitespace-nowrap">{m.fechaEnvio}</span>
                </div>
                <div className="flex items-center gap-2 mt-1 text-xs text-slate-500">
                  <span>De: <strong className="text-slate-600">{m.remitente?.nombre}</strong></span>
                  <span>·</span>
                  <span>Para: {m.destinatario?.nombre ?? `Curso ${m.cursoId}`}</span>
                </div>
                {isOpen && (
                  <div className="mt-3 pt-3 border-t border-violet-100" onClick={(e) => e.stopPropagation()}>
                    <p className="text-sm text-slate-700 whitespace-pre-wrap mb-3">{m.contenido}</p>
                    <div className="flex gap-2">
                      {showRead && !m.leido && (
                        <button onClick={() => marcarLeido(m.id)} className="btn btn-secondary !py-1.5 !text-xs">
                          ✓ Marcar leído
                        </button>
                      )}
                      <button onClick={() => setToDelete(m.id)} className="btn btn-danger !py-1.5 !text-xs">
                        🗑 Eliminar
                      </button>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}

      <Modal
        open={toDelete !== null}
        title="Eliminar mensaje"
        message="¿Estás seguro de que quieres eliminar este mensaje? Esta acción no se puede deshacer."
        confirmLabel="Eliminar"
        variant="danger"
        onConfirm={eliminar}
        onCancel={() => setToDelete(null)}
      />
    </div>
  );
}

function NuevoTab() {
  const [modo, setModo] = useState<ModoNuevo>("directo");
  const [destinatarioId, setDestinatarioId] = useState("");
  const [cursos, setCursos] = useState<CursoDTO[]>([]);
  const [cursoId, setCursoId] = useState("");
  const [titulo, setTitulo] = useState("");
  const [contenido, setContenido] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    academica
      .listCursos()
      .then((cs) => {
        setCursos(cs);
        if (cs.length > 0) setCursoId(String(cs[0].id));
      })
      .catch(() => { /* cursos opcionales */ });
  }, []);

  async function enviar(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    setSending(true);
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
      setMsg("Mensaje enviado correctamente");
      setTitulo("");
      setContenido("");
      setDestinatarioId("");
    } catch (err: unknown) {
      setError((err as Error).message);
    } finally {
      setSending(false);
    }
  }

  return (
    <div className="card">
      <h2>Nuevo mensaje</h2>
      <div className="flex gap-2 mb-4">
        <button
          type="button"
          onClick={() => setModo("directo")}
          className={`flex-1 px-4 py-2.5 rounded-lg text-sm font-medium border transition-colors ${
            modo === "directo"
              ? "bg-violet-100 border-violet-300 text-violet-800"
              : "bg-white border-slate-200 text-slate-500 hover:bg-slate-50"
          }`}
        >
          ✉️ Directo
        </button>
        <button
          type="button"
          onClick={() => setModo("comunicado")}
          className={`flex-1 px-4 py-2.5 rounded-lg text-sm font-medium border transition-colors ${
            modo === "comunicado"
              ? "bg-violet-100 border-violet-300 text-violet-800"
              : "bg-white border-slate-200 text-slate-500 hover:bg-slate-50"
          }`}
        >
          📢 Comunicado al curso
        </button>
      </div>
      <form onSubmit={enviar}>
        {modo === "directo" ? (
          <div>
            <label>Destinatario (ID de usuario)</label>
            <input
              type="number"
              placeholder="Ej: 5"
              value={destinatarioId}
              onChange={(e) => setDestinatarioId(e.target.value)}
              required
            />
          </div>
        ) : (
          <div>
            <label>Curso</label>
            <select className="w-full" value={cursoId} onChange={(e) => setCursoId(e.target.value)} required>
              {cursos.length === 0 && <option value="">(sin cursos)</option>}
              {cursos.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.nombre} — {c.nivel}
                </option>
              ))}
            </select>
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
          rows={5}
          value={contenido}
          onChange={(e) => setContenido(e.target.value)}
          required
        />
        <div className="mt-4">
          <button type="submit" className="btn btn-primary" disabled={sending}>
            {sending ? "Enviando..." : "📨 Enviar mensaje"}
          </button>
        </div>
      </form>
      <Alert type="error" message={error} onClose={() => setError(null)} />
      <Alert type="success" message={msg} onClose={() => setMsg(null)} />
    </div>
  );
}
