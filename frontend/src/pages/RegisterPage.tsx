import { type FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth, type Role } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";

const ROLES: Role[] = ["DOCENTE", "APODERADO", "ESTUDIANTE", "ADMIN"];

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
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div className="card w-full max-w-md">
        <h1>Registrar</h1>
        <form onSubmit={onSubmit}>
          <label>Email</label>
          <input
            className="w-full"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <label>Nombre</label>
          <input
            className="w-full"
            type="text"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
          <label>Password</label>
          <input
            className="w-full"
            type="password"
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
              <option key={r} value={r}>
                {r}
              </option>
            ))}
          </select>
          <label>Entity ID (opcional, alumnoId/docenteId/apoderadoId)</label>
          <input
            className="w-full"
            type="number"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
          />
          <div className="mt-4">
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? "Registrando..." : "Registrar"}
            </button>
          </div>
        </form>
        {error && (
          <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-3 text-sm">
            {error}
          </p>
        )}
        <p className="text-sm mt-3">
          <Link to="/login">Volver al login</Link>
        </p>
      </div>
    </div>
  );
}
