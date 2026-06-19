import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import type { UserDTO } from "@libroclases/api-client";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
}));

import { auth } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";
import Layout from "./Layout";

const fakeUser: UserDTO = {
  id: 1,
  email: "ana@colegio.cl",
  nombre: "Ana Díaz",
  entityId: null,
  roles: ["DOCENTE"],
  menus: [
    { id: 1, label: "Dashboard", path: "/dashboard", parentId: null, orden: 1, roles: ["DOCENTE"] },
    { id: 2, label: "Asistencia", path: "/asistencia", parentId: null, orden: 2, roles: ["DOCENTE"] },
  ],
};

function renderLayout() {
  return render(
    <MemoryRouter initialEntries={["/dashboard"]}>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/dashboard" element={<div>CONTENIDO INTERNO</div>} />
        </Route>
        <Route path="/login" element={<div>PAGINA LOGIN</div>} />
      </Routes>
    </MemoryRouter>,
  );
}

beforeEach(() => {
  vi.clearAllMocks();
  useAuthStore.setState({ token: "t", user: fakeUser });
});

describe("Layout", () => {
  it("muestra el nombre y las iniciales del usuario", () => {
    renderLayout();
    expect(screen.getByText("Ana Díaz")).toBeInTheDocument();
    expect(screen.getByText("AD")).toBeInTheDocument();
  });

  it("muestra los items del menú del usuario", () => {
    renderLayout();
    expect(screen.getByText("Dashboard")).toBeInTheDocument();
    expect(screen.getByText("Asistencia")).toBeInTheDocument();
  });

  it("renderiza el contenido de la ruta hija (Outlet)", () => {
    renderLayout();
    expect(screen.getByText("CONTENIDO INTERNO")).toBeInTheDocument();
  });

  it("cierra sesión al hacer clic en Salir", async () => {
    const user = userEvent.setup();
    renderLayout();
    await user.click(screen.getByRole("button", { name: /salir/i }));
    await waitFor(() => expect(auth.logout).toHaveBeenCalled());
  });

  it("alterna el menú lateral en móvil con el botón hamburguesa", async () => {
    const user = userEvent.setup();
    renderLayout();
    const toggle = screen.getByRole("button", { name: "☰" });
    await user.click(toggle);
    expect(screen.getByRole("button", { name: "✕" })).toBeInTheDocument();
  });
});
