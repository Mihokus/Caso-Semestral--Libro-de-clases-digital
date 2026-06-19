import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import type { ReactNode } from "react";
import type { UserDTO } from "@libroclases/api-client";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
}));

import { useAuthStore } from "@/store/auth";
import PrivateRoute from "./PrivateRoute";

const fakeUser: UserDTO = {
  id: 1,
  email: "ana@colegio.cl",
  nombre: "Ana",
  entityId: null,
  roles: ["DOCENTE"],
  menus: [],
};

function renderGuard(node: ReactNode) {
  return render(
    <MemoryRouter initialEntries={["/protegido"]}>
      <Routes>
        <Route path="/protegido" element={node} />
        <Route path="/login" element={<div>PAGINA LOGIN</div>} />
        <Route path="/dashboard" element={<div>PAGINA DASHBOARD</div>} />
      </Routes>
    </MemoryRouter>,
  );
}

beforeEach(() => {
  useAuthStore.setState({ token: null, user: null });
});

describe("PrivateRoute", () => {
  it("redirige a login si no está autenticado", () => {
    renderGuard(
      <PrivateRoute>
        <div>CONTENIDO PRIVADO</div>
      </PrivateRoute>,
    );
    expect(screen.getByText("PAGINA LOGIN")).toBeInTheDocument();
  });

  it("muestra el contenido si está autenticado y no se exigen roles", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    renderGuard(
      <PrivateRoute>
        <div>CONTENIDO PRIVADO</div>
      </PrivateRoute>,
    );
    expect(screen.getByText("CONTENIDO PRIVADO")).toBeInTheDocument();
  });

  it("muestra el contenido si el usuario tiene el rol requerido", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    renderGuard(
      <PrivateRoute roles={["DOCENTE"]}>
        <div>CONTENIDO PRIVADO</div>
      </PrivateRoute>,
    );
    expect(screen.getByText("CONTENIDO PRIVADO")).toBeInTheDocument();
  });

  it("redirige al dashboard si no tiene el rol requerido", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    renderGuard(
      <PrivateRoute roles={["ADMIN"]}>
        <div>CONTENIDO PRIVADO</div>
      </PrivateRoute>,
    );
    expect(screen.getByText("PAGINA DASHBOARD")).toBeInTheDocument();
  });
});
