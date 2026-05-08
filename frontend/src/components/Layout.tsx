import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useAuthStore, useUser } from "@/store/auth";

export default function Layout() {
  const user = useUser();
  const clear = useAuthStore((s) => s.clear);
  const navigate = useNavigate();
  const location = useLocation();

  function onLogout() {
    clear();
    navigate("/login");
  }

  const menus = (user?.menus ?? []).slice().sort((a, b) => (a.orden ?? 0) - (b.orden ?? 0));

  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-white border-b border-gray-300 px-4 py-3 flex items-center justify-between">
        <div className="text-xl font-semibold text-blue-700">Libro de Clases Digital</div>
        {user && (
          <div className="flex items-center gap-3">
            <div className="text-right">
              <div className="text-sm font-medium">{user.nombre}</div>
              <div className="text-xs text-gray-600">{user.roles.join(", ")}</div>
            </div>
            <button className="btn btn-secondary" onClick={onLogout}>
              Cerrar sesión
            </button>
          </div>
        )}
      </header>

      <div className="flex flex-1">
        <nav className="w-56 bg-white border-r border-gray-300 p-3">
          <ul className="space-y-1 list-none p-0 m-0">
            {menus.map((m) => {
              const active = location.pathname === m.path;
              return (
                <li key={m.id}>
                  <Link
                    to={m.path}
                    className={`block px-2 py-1 rounded text-sm no-underline ${
                      active
                        ? "bg-blue-100 text-blue-800 font-medium"
                        : "text-gray-700 hover:bg-gray-100"
                    }`}
                  >
                    {m.label}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        <main className="flex-1 p-4">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
