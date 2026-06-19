import { useState } from "react";
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useAuthStore, useUser } from "@/store/auth";

const MENU_ICONS: Record<string, string> = {
  "/dashboard": "📊",
  "/asistencia": "📋",
  "/asistencia/tomar": "📋",
  "/academica": "📚",
  "/asignaturas": "📚",
  "/notas/registrar": "📝",
  "/rendimiento": "📈",
  "/mensajes": "✉️",
  "/admin/users": "👥",
  "/admin/roles": "🔐",
  "/admin/menus": "📑",
  "/pupilos": "👨‍👧",
  "/pupilos/asistencia": "📋",
  "/pupilos/notas": "📚",
  "/mi-asistencia": "📋",
  "/mis-notas": "📚",
};

export default function Layout() {
  const user = useUser();
  const clear = useAuthStore((s) => s.clear);
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  function onLogout() {
    clear();
    navigate("/login");
  }

  const menus = (user?.menus ?? []).slice().sort((a, b) => (a.orden ?? 0) - (b.orden ?? 0));

  const initials = user?.nombre
    ? user.nombre.split(" ").map((w) => w[0]).join("").toUpperCase().slice(0, 2)
    : "?";

  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-violet-700 px-4 py-3 flex items-center justify-between shadow-md relative z-30">
        <div className="flex items-center gap-3">
          <button
            className="lg:hidden text-white text-xl leading-none p-1"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            {sidebarOpen ? "✕" : "☰"}
          </button>
          <div className="text-lg font-bold text-white">Libro de Clases</div>
          <div className="hidden sm:block text-xs text-violet-200">Colegio B. O'Higgins</div>
        </div>
        {user && (
          <div className="flex items-center gap-3">
            <div className="hidden sm:block text-right">
              <div className="text-sm font-medium text-white">{user.nombre}</div>
              <div className="text-xs text-violet-200">{user.roles.join(", ")}</div>
            </div>
            <div className="w-9 h-9 bg-violet-200 rounded-full flex items-center justify-center text-violet-800 text-sm font-bold">
              {initials}
            </div>
            <button className="btn btn-secondary !py-1.5 !px-3 !text-xs" onClick={onLogout}>
              Salir
            </button>
          </div>
        )}
      </header>

      <div className="flex flex-1 relative">
        {sidebarOpen && (
          <div
            className="fixed inset-0 bg-black/30 z-10 lg:hidden"
            onClick={() => setSidebarOpen(false)}
          />
        )}

        <nav
          className={`
            w-60 bg-white border-r border-violet-100 p-3 flex-shrink-0
            fixed top-[52px] bottom-0 z-20 transition-transform lg:static lg:translate-x-0
            ${sidebarOpen ? "translate-x-0" : "-translate-x-full"}
          `}
        >
          <div className="text-xs font-semibold text-violet-400 uppercase tracking-wider px-3 mb-2 mt-1">
            Menú
          </div>
          <ul className="space-y-0.5 list-none p-0 m-0">
            {menus.map((m) => {
              const active = location.pathname === m.path;
              const icon = MENU_ICONS[m.path] ?? "📄";
              return (
                <li key={m.id}>
                  <Link
                    to={m.path}
                    onClick={() => setSidebarOpen(false)}
                    className={`flex items-center gap-3 px-3 py-2 rounded-lg text-sm no-underline transition-colors ${
                      active
                        ? "bg-violet-100 text-violet-800 font-medium"
                        : "text-slate-600 hover:bg-violet-50 hover:text-violet-700"
                    }`}
                  >
                    <span className="text-base">{icon}</span>
                    <span>{m.label}</span>
                    {m.path === "/mensajes" && (
                      <span className="ml-auto bg-red-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full leading-none">
                        !
                      </span>
                    )}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        <main className="flex-1 p-4 lg:p-6 min-w-0">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
