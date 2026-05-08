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
    <div>
      <h1>Registrar</h1>
      <form onSubmit={onSubmit}>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Nombre</label>
          <input
            type="text"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            minLength={6}
            required
          />
        </div>
        <div>
          <label>Rol</label>
          <select value={role} onChange={(e) => setRole(e.target.value as Role)}>
            {ROLES.map((r) => (
              <option key={r} value={r}>
                {r}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label>Entity ID (opcional, alumnoId/docenteId/apoderadoId)</label>
          <input
            type="number"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Registrando..." : "Registrar"}
        </button>
      </form>
      {error && <p>Error: {error}</p>}
      <p>
        <Link to="/login">Volver al login</Link>
      </p>
    </div>
  );
}
