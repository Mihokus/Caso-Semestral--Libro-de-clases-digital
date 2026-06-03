import { type FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";
import Alert from "@/components/Alert";
import Spinner from "@/components/Spinner";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [showDemo, setShowDemo] = useState(false);
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

  function fillDemo(demoEmail: string, demoPass: string) {
    setEmail(demoEmail);
    setPassword(demoPass);
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4"
      style={{ background: "linear-gradient(135deg, #6d28d9 0%, #4c1d95 100%)" }}>
      <div className="card w-full max-w-md !p-8">
        <div className="text-center mb-6">
          <div className="text-2xl font-bold text-violet-950">Libro de Clases</div>
          <div className="text-sm text-violet-500 mt-1">Colegio Bernardo O'Higgins</div>
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
            autoComplete="email"
          />
          <label>Contraseña</label>
          <input
            className="w-full"
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            autoComplete="current-password"
          />
          <div className="mt-5">
            <button type="submit" className="btn btn-primary w-full !py-2.5" disabled={loading}>
              {loading ? <Spinner text="Ingresando..." /> : "Ingresar"}
            </button>
          </div>
        </form>
        <Alert type="error" message={error} onClose={() => setError(null)} />
        <p className="text-sm mt-4 text-center text-slate-500">
          ¿No tienes cuenta?{" "}
          <Link to="/register" className="text-violet-700 font-medium">Registrarme</Link>
        </p>
        <hr />
        <div>
          <button
            onClick={() => setShowDemo(!showDemo)}
            className="text-xs text-violet-500 hover:text-violet-700 flex items-center gap-1 mx-auto transition-colors"
          >
            <span>{showDemo ? "▼" : "▶"}</span> Credenciales de prueba
          </button>
          {showDemo && (
            <div className="mt-2 bg-violet-50 rounded-lg p-3 space-y-1.5">
              {[
                { label: "Admin", email: "admin@colegio.cl", pass: "admin123" },
                { label: "Docente", email: "docente@colegio.cl", pass: "docente123" },
                { label: "Apoderado", email: "apoderado@colegio.cl", pass: "apoderado123" },
                { label: "Estudiante", email: "estudiante@colegio.cl", pass: "estudiante123" },
              ].map((d) => (
                <button
                  key={d.email}
                  type="button"
                  onClick={() => fillDemo(d.email, d.pass)}
                  className="w-full text-left text-xs px-2 py-1.5 rounded hover:bg-violet-100 transition-colors text-slate-600"
                >
                  <span className="font-medium text-violet-700">{d.label}:</span> {d.email}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
