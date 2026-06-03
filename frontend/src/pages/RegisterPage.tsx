import { type FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth, type Role } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";
import Alert from "@/components/Alert";
import Spinner from "@/components/Spinner";

const ROLES: { value: Role; label: string; emoji: string }[] = [
  { value: "DOCENTE", label: "Docente", emoji: "🧑‍🏫" },
  { value: "APODERADO", label: "Apoderado", emoji: "👨‍👧" },
  { value: "ESTUDIANTE", label: "Estudiante", emoji: "🎓" },
  { value: "ADMIN", label: "Administrador", emoji: "🔐" },
];

export default function RegisterPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nombre, setNombre] = useState("");
  const [role, setRole] = useState<Role>("ESTUDIANTE");
  const [entityId, setEntityId] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const setSession = useAuthStore((s) => s.setSession);
  const navigate = useNavigate();

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const res = await auth.register({
        email,
        password,
        nombre,
        role,
        entityId: entityId ? parseInt(entityId, 10) : null,
      });
      setSession(res.token, res.user);
      navigate("/dashboard");
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { error?: string } } })?.response?.data?.error ??
        "Error de registro";
      setError(msg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4"
      style={{ background: "linear-gradient(135deg, #6d28d9 0%, #4c1d95 100%)" }}>
      <div className="card w-full max-w-md !p-8">
        <div className="text-center mb-6">
          <div className="text-2xl font-bold text-violet-950">Crear cuenta</div>
          <div className="text-sm text-violet-500 mt-1">Libro de Clases · Colegio B. O'Higgins</div>
        </div>
        <form onSubmit={onSubmit}>
          <label>Correo electrónico</label>
          <input
            className="w-full"
            type="email"
            placeholder="usuario@colegio.cl"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <label>Nombre completo</label>
          <input
            className="w-full"
            type="text"
            placeholder="Nombre y apellido"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
          <label>Contraseña</label>
          <input
            className="w-full"
            type="password"
            placeholder="Mínimo 6 caracteres"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            minLength={6}
            required
          />
          <label>Rol</label>
          <select
            className="w-full"
            value={role}
            onChange={(e) => setRole(e.target.value as Role)}
          >
            {ROLES.map((r) => (
              <option key={r.value} value={r.value}>
                {r.emoji} {r.label}
              </option>
            ))}
          </select>
          <label>ID asociado <span className="text-slate-400 font-normal">(opcional: alumno/docente/apoderado)</span></label>
          <input
            className="w-full"
            type="number"
            placeholder="Ej: 1"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
          />
          <div className="mt-5">
            <button type="submit" className="btn btn-primary w-full !py-2.5" disabled={loading}>
              {loading ? <Spinner text="Registrando..." /> : "Registrar"}
            </button>
          </div>
        </form>
        <Alert type="error" message={error} onClose={() => setError(null)} />
        <p className="text-sm mt-4 text-center text-slate-500">
          <Link to="/login" className="text-violet-700 font-medium">← Volver al login</Link>
        </p>
      </div>
    </div>
  );
}
