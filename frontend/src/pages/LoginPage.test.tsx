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
  auth: { login: vi.fn(), logout: vi.fn() },
  getToken: vi.fn(() => null),
}));

import { auth } from "@libroclases/api-client";
import LoginPage from "./LoginPage";

const loginMock = auth.login as unknown as Mock;

function renderLogin() {
  return render(
    <MemoryRouter>
      <LoginPage />
    </MemoryRouter>,
  );
}

beforeEach(() => {
  localStorage.clear();
  vi.clearAllMocks();
});

describe("LoginPage", () => {
  it("muestra los campos de correo y contraseña", () => {
    renderLogin();
    expect(screen.getByPlaceholderText("usuario@colegio.cl")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("••••••••")).toBeInTheDocument();
  });

  it("las credenciales de prueba están ocultas por defecto", () => {
    renderLogin();
    expect(screen.queryByText(/admin@colegio\.cl/)).not.toBeInTheDocument();
  });

  it("muestra las credenciales de prueba al expandir el acordeón", async () => {
    const user = userEvent.setup();
    renderLogin();
    await user.click(screen.getByText(/Credenciales de prueba/));
    expect(screen.getByText(/admin@colegio\.cl/)).toBeInTheDocument();
  });

  it("rellena el correo al elegir una credencial de prueba", async () => {
    const user = userEvent.setup();
    renderLogin();
    await user.click(screen.getByText(/Credenciales de prueba/));
    await user.click(screen.getByText(/docente@colegio\.cl/));
    const email = screen.getByPlaceholderText("usuario@colegio.cl") as HTMLInputElement;
    expect(email.value).toBe("docente@colegio.cl");
  });

  it("envía las credenciales ingresadas a auth.login", async () => {
    loginMock.mockResolvedValue({ token: "tok", user: { id: 1, roles: [] } });
    const user = userEvent.setup();
    renderLogin();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "ana@colegio.cl");
    await user.type(screen.getByPlaceholderText("••••••••"), "secreta123");
    await user.click(screen.getByRole("button", { name: /ingresar/i }));
    await waitFor(() =>
      expect(loginMock).toHaveBeenCalledWith({
        email: "ana@colegio.cl",
        password: "secreta123",
      }),
    );
  });

  it("navega al dashboard tras un login exitoso", async () => {
    loginMock.mockResolvedValue({ token: "tok", user: { id: 1, roles: [] } });
    const user = userEvent.setup();
    renderLogin();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "ana@colegio.cl");
    await user.type(screen.getByPlaceholderText("••••••••"), "secreta123");
    await user.click(screen.getByRole("button", { name: /ingresar/i }));
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/dashboard"));
  });

  it("muestra un mensaje de error cuando el login falla", async () => {
    loginMock.mockRejectedValue({ response: { data: { error: "Credenciales inválidas" } } });
    const user = userEvent.setup();
    renderLogin();
    await user.type(screen.getByPlaceholderText("usuario@colegio.cl"), "ana@colegio.cl");
    await user.type(screen.getByPlaceholderText("••••••••"), "mala");
    await user.click(screen.getByRole("button", { name: /ingresar/i }));
    await waitFor(() =>
      expect(screen.getByText("Credenciales inválidas")).toBeInTheDocument(),
    );
  });
});
