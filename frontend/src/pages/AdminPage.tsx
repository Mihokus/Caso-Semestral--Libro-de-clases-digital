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
      <nav>
        <button onClick={() => setTab("usuarios")} disabled={tab === "usuarios"}>
          Usuarios
        </button>
        <button onClick={() => setTab("roles")} disabled={tab === "roles"}>
          Roles
        </button>
        <button onClick={() => setTab("menus")} disabled={tab === "menus"}>
          Menús
        </button>
      </nav>
      <hr />
      {tab === "usuarios" && <UsersTab />}
      {tab === "roles" && <RolesTab />}
      {tab === "menus" && <MenusTab />}
    </div>
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
    <div>
      <h2>Usuarios</h2>
      <button onClick={cargar}>Recargar</button>
      {error && <p>Error: {error}</p>}
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
                  <div>
                    {roles.map((r) => (
                      <label key={r.id}>
                        <input
                          type="checkbox"
                          checked={pickedRoleIds.includes(r.id)}
                          onChange={() => toggleRole(r.id)}
                        />
                        {r.nombre}
                      </label>
                    ))}
                  </div>
                ) : (
                  u.roles.join(", ")
                )}
              </td>
              <td>
                {editingId === u.id ? (
                  <>
                    <button onClick={saveRoles}>Guardar</button>
                    <button onClick={() => setEditingId(null)}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => startEdit(u)}>Editar roles</button>
                    <button onClick={() => eliminar(u.id)}>Eliminar</button>
                  </>
                )}
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
    <div>
      <h2>Roles</h2>
      <form onSubmit={crear}>
        <label>Nombre</label>
        <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
        <label>Descripción</label>
        <input value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />
        <button type="submit">Crear rol</button>
      </form>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
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
    <div>
      <h2>Menús</h2>
      <form onSubmit={crear}>
        <label>Label</label>
        <input value={label} onChange={(e) => setLabel(e.target.value)} required />
        <label>Path</label>
        <input value={path} onChange={(e) => setPath(e.target.value)} required />
        <label>Orden</label>
        <input
          type="number"
          value={orden}
          onChange={(e) => setOrden(e.target.value)}
        />
        <fieldset>
          <legend>Roles que ven el menú</legend>
          {roles.map((r) => (
            <label key={r.id}>
              <input
                type="checkbox"
                checked={pickedRoleIds.includes(r.id)}
                onChange={() => toggleRole(r.id)}
              />
              {r.nombre}
            </label>
          ))}
        </fieldset>
        <button type="submit">Crear menú</button>
      </form>
      {error && <p>Error: {error}</p>}
      {msg && <p>{msg}</p>}
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
