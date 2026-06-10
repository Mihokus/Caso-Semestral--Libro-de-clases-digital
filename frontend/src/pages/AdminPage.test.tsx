import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
  admin: {
    listUsers: vi.fn(),
    listRoles: vi.fn(),
    listMenus: vi.fn(),
    updateUserRoles: vi.fn(),
    deleteUser: vi.fn(),
    createRole: vi.fn(),
    createMenu: vi.fn(),
  },
}));

import { admin } from "@libroclases/api-client";
import AdminPage from "./AdminPage";

const users = [{ id: 1, email: "ana@colegio.cl", nombre: "Ana Díaz", entityId: null, roles: ["DOCENTE"], menus: [] }];
const roles = [{ id: 1, nombre: "DOCENTE", descripcion: "Profesor" }];
const menus = [{ id: 1, label: "Dashboard", path: "/dashboard", parentId: null, orden: 1, roles: ["DOCENTE"] }];

beforeEach(() => {
  vi.clearAllMocks();
  (admin.listUsers as Mock).mockResolvedValue(users);
  (admin.listRoles as Mock).mockResolvedValue(roles);
  (admin.listMenus as Mock).mockResolvedValue(menus);
});

describe("AdminPage", () => {
  it("muestra la lista de usuarios por defecto", async () => {
    render(<AdminPage />);
    expect(await screen.findByText("ana@colegio.cl")).toBeInTheDocument();
  });

  it("elimina un usuario tras confirmar en el modal", async () => {
    (admin.deleteUser as Mock).mockResolvedValue(undefined);
    const user = userEvent.setup();
    render(<AdminPage />);
    await screen.findByText("ana@colegio.cl");
    await user.click(screen.getByRole("button", { name: "Eliminar" }));
    expect(screen.getByText(/no se puede deshacer/i)).toBeInTheDocument();
    const botones = screen.getAllByRole("button", { name: "Eliminar" });
    await user.click(botones[botones.length - 1]);
    await waitFor(() => expect(admin.deleteUser).toHaveBeenCalledWith(1));
  });

  it("edita los roles de un usuario", async () => {
    (admin.updateUserRoles as Mock).mockResolvedValue(undefined);
    const user = userEvent.setup();
    render(<AdminPage />);
    await screen.findByText("ana@colegio.cl");
    await user.click(screen.getByRole("button", { name: /editar roles/i }));
    const checkbox = screen.getByRole("checkbox");
    await user.click(checkbox);
    await user.click(screen.getByRole("button", { name: /guardar/i }));
    await waitFor(() => expect(admin.updateUserRoles).toHaveBeenCalled());
  });

  it("muestra los roles al cambiar de pestaña", async () => {
    const user = userEvent.setup();
    render(<AdminPage />);
    await user.click(screen.getByRole("button", { name: /roles/i }));
    expect(await screen.findByText("Profesor")).toBeInTheDocument();
  });

  it("crea un rol nuevo", async () => {
    (admin.createRole as Mock).mockResolvedValue({});
    const user = userEvent.setup();
    render(<AdminPage />);
    await user.click(screen.getByRole("button", { name: /roles/i }));
    await screen.findByText("Profesor");
    const inputs = screen.getAllByRole("textbox");
    await user.type(inputs[0], "APODERADO");
    await user.click(screen.getByRole("button", { name: /crear rol/i }));
    await waitFor(() => expect(admin.createRole).toHaveBeenCalled());
  });

  it("muestra los menús al cambiar de pestaña", async () => {
    const user = userEvent.setup();
    render(<AdminPage />);
    await user.click(screen.getByRole("button", { name: /menús/i }));
    expect(await screen.findByText("/dashboard")).toBeInTheDocument();
  });
});
