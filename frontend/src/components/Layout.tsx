import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuthStore, useUser } from "@/store/auth";

export default function Layout() {
  const user = useUser();
  const clear = useAuthStore((s) => s.clear);
  const navigate = useNavigate();

  function onLogout() {
    clear();
    navigate("/login");
  }

  return (
    <div>
      <header>
        <h2>Libro de Clases Digital</h2>
        {user && (
          <div>
            <span>
              {user.nombre} ({user.roles.join(", ")})
            </span>
            <button onClick={onLogout}>Cerrar sesión</button>
          </div>
        )}
      </header>

      <nav>
        <ul>
          {user?.menus?.map((m) => (
            <li key={m.id}>
              <Link to={m.path}>{m.label}</Link>
            </li>
          ))}
        </ul>
      </nav>

      <main>
        <Outlet />
      </main>
    </div>
  );
}
