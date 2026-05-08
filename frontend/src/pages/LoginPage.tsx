import { type FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const setSession = useAuthStore((s) => s.setSession);
  const navigate = useNavigate();

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const res = await auth.login({ email, password });
      setSession(res.token, res.user);
      navigate("/dashboard");
    } catch (err: unknown) {
      const msg =
        (err as { response?: { data?: { error?: string } } })?.response?.data?.error ??
        "Error de login";
      setError(msg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={onSubmit}>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoComplete="email"
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            autoComplete="current-password"
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Ingresando..." : "Ingresar"}
        </button>
      </form>
      {error && <p>Error: {error}</p>}
      <p>
        ¿No tenés cuenta? <Link to="/register">Registrarme</Link>
      </p>
      <hr />
      <p>Demo creds:</p>
      <ul>
        <li>admin@colegio.cl / admin123</li>
        <li>docente@colegio.cl / docente123</li>
        <li>apoderado@colegio.cl / apoderado123</li>
        <li>estudiante@colegio.cl / estudiante123</li>
      </ul>
    </div>
  );
}
