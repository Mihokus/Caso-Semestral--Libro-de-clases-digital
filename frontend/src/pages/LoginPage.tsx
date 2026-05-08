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
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div className="card w-full max-w-md">
        <h1>Login</h1>
        <form onSubmit={onSubmit}>
          <label>Email</label>
          <input
            className="w-full"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoComplete="email"
          />
          <label>Password</label>
          <input
            className="w-full"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            autoComplete="current-password"
          />
          <div className="mt-4">
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? "Ingresando..." : "Ingresar"}
            </button>
          </div>
        </form>
        {error && (
          <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-3 text-sm">
            {error}
          </p>
        )}
        <p className="text-sm mt-3">
          ¿No tenés cuenta? <Link to="/register">Registrarme</Link>
        </p>
        <hr />
        <div className="text-xs text-gray-600">
          <div className="font-medium mb-1">Demo creds:</div>
          <ul className="list-disc list-inside space-y-0.5">
            <li>admin@colegio.cl / admin123</li>
            <li>docente@colegio.cl / docente123</li>
            <li>apoderado@colegio.cl / apoderado123</li>
            <li>estudiante@colegio.cl / estudiante123</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
