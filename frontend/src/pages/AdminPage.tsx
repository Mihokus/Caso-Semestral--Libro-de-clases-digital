import { type FormEvent, useEffect, useState } from "react";
import {
  admin,
  type MenuDTO,
  type RoleDTO,
  type UserDTO,
} from "@libroclases/api-client";

type Tab = "usuarios" | "roles" | "menus";

export default function AdminPage() {
  const [tab, setTab] = useState<Tab>("usuarios");

  return (
    <div>
      <h1>Administración</h1>
      <div className="border-b border-gray-300 flex gap-1 mb-4">
        <TabBtn active={tab === "usuarios"} onClick={() => setTab("usuarios")}>
          Usuarios
        </TabBtn>
        <TabBtn active={tab === "roles"} onClick={() => setTab("roles")}>
          Roles
        </TabBtn>
        <TabBtn active={tab === "menus"} onClick={() => setTab("menus")}>
          Menús
        </TabBtn>
      </div>
      {tab === "usuarios" && <UsersTab />}
      {tab === "roles" && <RolesTab />}
      {tab === "menus" && <MenusTab />}
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

function UsersTab() {
  const [users, setUsers] = useState<UserDTO[]>([]);
  const [roles, setRoles] = useState<RoleDTO[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [pickedRoleIds, setPickedRoleIds] = useState<number[]>([]);
  const [error, setError] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      const [u, r] = await Promise.all([admin.listUsers(), admin.listRoles()]);
      setUsers(u);
      setRoles(r);
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  function startEdit(user: UserDTO) {
    setEditingId(user.id);
    const ids = roles.filter((r) => user.roles.includes(r.nombre)).map((r) => r.id);
    setPickedRoleIds(ids);
  }

  async function saveRoles() {
    if (editingId == null) return;
    try {
      await admin.updateUserRoles(editingId, { roleIds: pickedRoleIds });
      setEditingId(null);
      cargar();
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function eliminar(id: number) {
    if (!confirm(`¿Eliminar user ${id}?`)) return;
    try {
      await admin.deleteUser(id);
      cargar();
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  function toggleRole(roleId: number) {
    setPickedRoleIds((prev) =>
      prev.includes(roleId) ? prev.filter((id) => id !== roleId) : [...prev, roleId],
    );
  }

  useEffect(() => {
    cargar();
  }, []);

  return (
    <div className="card">
      <div className="flex justify-between items-center">
        <h2>Usuarios</h2>
        <button onClick={cargar} className="btn btn-secondary">
          Recargar
        </button>
      </div>
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-2 text-sm">
          {error}
        </p>
      )}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Nombre</th>
            <th>Roles</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.email}</td>
              <td>{u.nombre}</td>
              <td>
                {editingId === u.id ? (
                  <div className="flex flex-col gap-1">
                    {roles.map((r) => (
                      <label key={r.id} className="flex items-center gap-1">
                        <input
                          type="checkbox"
                          checked={pickedRoleIds.includes(r.id)}
                          onChange={() => toggleRole(r.id)}
                        />
                        <span className="text-sm">{r.nombre}</span>
                      </label>
                    ))}
                  </div>
                ) : (
                  u.roles.join(", ")
                )}
              </td>
              <td>
                <div className="flex gap-1">
                  {editingId === u.id ? (
                    <>
                      <button onClick={saveRoles} className="btn btn-primary">
                        Guardar
                      </button>
                      <button
                        onClick={() => setEditingId(null)}
                        className="btn btn-secondary"
                      >
                        Cancelar
                      </button>
                    </>
                  ) : (
                    <>
                      <button onClick={() => startEdit(u)} className="btn btn-secondary">
                        Editar roles
                      </button>
                      <button onClick={() => eliminar(u.id)} className="btn btn-danger">
                        Eliminar
                      </button>
                    </>
                  )}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function RolesTab() {
  const [roles, setRoles] = useState<RoleDTO[]>([]);
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      setRoles(await admin.listRoles());
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function crear(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      await admin.createRole({ nombre, descripcion });
      setMsg("Rol creado");
      setNombre("");
      setDescripcion("");
      cargar();
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  useEffect(() => {
    cargar();
  }, []);

  return (
    <div className="card">
      <h2>Roles</h2>
      <form onSubmit={crear} className="mb-3">
        <div className="row">
          <div>
            <label>Nombre</label>
            <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          </div>
          <div className="flex-1 min-w-[200px]">
            <label>Descripción</label>
            <input
              className="w-full"
              value={descripcion}
              onChange={(e) => setDescripcion(e.target.value)}
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Crear rol
          </button>
        </div>
      </form>
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-2 text-sm">
          {error}
        </p>
      )}
      {msg && (
        <p className="text-green-700 bg-green-100 border border-green-300 rounded px-2 py-1 mt-2 text-sm">
          {msg}
        </p>
      )}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Descripción</th>
          </tr>
        </thead>
        <tbody>
          {roles.map((r) => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.nombre}</td>
              <td>{r.descripcion}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function MenusTab() {
  const [menus, setMenus] = useState<MenuDTO[]>([]);
  const [roles, setRoles] = useState<RoleDTO[]>([]);
  const [label, setLabel] = useState("");
  const [path, setPath] = useState("");
  const [orden, setOrden] = useState("");
  const [pickedRoleIds, setPickedRoleIds] = useState<number[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [msg, setMsg] = useState<string | null>(null);

  async function cargar() {
    setError(null);
    try {
      const [m, r] = await Promise.all([admin.listMenus(), admin.listRoles()]);
      setMenus(m);
      setRoles(r);
    } catch (e: unknown) {
      setError((e as Error).message);
    }
  }

  async function crear(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setMsg(null);
    try {
      await admin.createMenu({
        label,
        path,
        orden: orden ? parseInt(orden, 10) : null,
        roleIds: pickedRoleIds,
      });
      setMsg("Menú creado");
      setLabel("");
      setPath("");
      setOrden("");
      setPickedRoleIds([]);
      cargar();
    } catch (err: unknown) {
      setError((err as Error).message);
    }
  }

  function toggleRole(roleId: number) {
    setPickedRoleIds((prev) =>
      prev.includes(roleId) ? prev.filter((id) => id !== roleId) : [...prev, roleId],
    );
  }

  useEffect(() => {
    cargar();
  }, []);

  return (
    <div className="card">
      <h2>Menús</h2>
      <form onSubmit={crear} className="mb-3">
        <div className="row">
          <div>
            <label>Label</label>
            <input value={label} onChange={(e) => setLabel(e.target.value)} required />
          </div>
          <div>
            <label>Path</label>
            <input value={path} onChange={(e) => setPath(e.target.value)} required />
          </div>
          <div>
            <label>Orden</label>
            <input
              type="number"
              value={orden}
              onChange={(e) => setOrden(e.target.value)}
            />
          </div>
        </div>
        <fieldset>
          <legend>Roles que ven el menú</legend>
          <div className="flex flex-wrap gap-2">
            {roles.map((r) => (
              <label key={r.id} className="flex items-center gap-1">
                <input
                  type="checkbox"
                  checked={pickedRoleIds.includes(r.id)}
                  onChange={() => toggleRole(r.id)}
                />
                <span className="text-sm">{r.nombre}</span>
              </label>
            ))}
          </div>
        </fieldset>
        <button type="submit" className="btn btn-primary mt-2">
          Crear menú
        </button>
      </form>
      {error && (
        <p className="text-red-700 bg-red-100 border border-red-300 rounded px-2 py-1 mt-2 text-sm">
          {error}
        </p>
      )}
      {msg && (
        <p className="text-green-700 bg-green-100 border border-green-300 rounded px-2 py-1 mt-2 text-sm">
          {msg}
        </p>
      )}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Label</th>
            <th>Path</th>
            <th>Orden</th>
            <th>Roles</th>
          </tr>
        </thead>
        <tbody>
          {menus.map((m) => (
            <tr key={m.id}>
              <td>{m.id}</td>
              <td>{m.label}</td>
              <td>{m.path}</td>
              <td>{m.orden}</td>
              <td>{m.roles.join(", ")}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
