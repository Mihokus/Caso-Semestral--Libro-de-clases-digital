import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async (importOriginal) => {
  const actual = await importOriginal<typeof import("react-router-dom")>();
  return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock("@libroclases/api-client", () => ({
  auth: { register: vi.fn(), logout: vi.fn() },
  getToken: vi.fn(() => null),
}));

import { auth } from "@libroclases/api-client";
import RegisterPage from "./RegisterPage";

const registerMock = auth.register as unknown as Mock;

function renderRegister() {
  return render(
    <MemoryRouter>
      <RegisterPage />
    </MemoryRouter>,
  );
}

beforeEach(() => {
  localStorage.clear();
  vi.clearAllMocks();
});

describe("RegisterPage", () => {
  it("muestra los campos del formulario", () => {
    renderRegister();
    expect(screen.getByPlaceholderText("usuario@colegio.cl")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Nombre y apellido")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Mínimo 6 caracteres")).toBeInTheDocument();
  });

  it("envía los datos del formulario a auth.register", async () => {
    registerMock.mockResolvedValue({ token: "tok", user: { id: 1, roles: [] } });
    const user = userEvent.setup();
    renderRegister();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "nuevo@colegio.cl");
    await user.type(screen.getByPlaceholderText("Nombre y apellido"), "Nuevo Alumno");
    await user.type(screen.getByPlaceholderText("Mínimo 6 caracteres"), "clave123");
    await user.click(screen.getByRole("button", { name: /registrar/i }));
    await waitFor(() =>
      expect(registerMock).toHaveBeenCalledWith(
        expect.objectContaining({
          email: "nuevo@colegio.cl",
          nombre: "Nuevo Alumno",
          password: "clave123",
          role: "ESTUDIANTE",
        }),
      ),
    );
  });

  it("navega al dashboard tras registrarse correctamente", async () => {
    registerMock.mockResolvedValue({ token: "tok", user: { id: 1, roles: [] } });
    const user = userEvent.setup();
    renderRegister();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "nuevo@colegio.cl");
    await user.type(screen.getByPlaceholderText("Nombre y apellido"), "Nuevo");
    await user.type(screen.getByPlaceholderText("Mínimo 6 caracteres"), "clave123");
    await user.click(screen.getByRole("button", { name: /registrar/i }));
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/dashboard"));
  });

  it("muestra error cuando el registro falla", async () => {
    registerMock.mockRejectedValue({ response: { data: { error: "Email ya registrado" } } });
    const user = userEvent.setup();
    renderRegister();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "dup@colegio.cl");
    await user.type(screen.getByPlaceholderText("Nombre y apellido"), "Dup");
    await user.type(screen.getByPlaceholderText("Mínimo 6 caracteres"), "clave123");
    await user.click(screen.getByRole("button", { name: /registrar/i }));
    await waitFor(() => expect(screen.getByText("Email ya registrado")).toBeInTheDocument());
  });
});
