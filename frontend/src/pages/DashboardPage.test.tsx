import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen } from "@testing-library/react";
import type { UserDTO } from "@libroclases/api-client";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
  dashboard: { get: vi.fn() },
}));

import { dashboard } from "@libroclases/api-client";
import { useAuthStore } from "@/store/auth";
import DashboardPage from "./DashboardPage";

const getMock = dashboard.get as unknown as Mock;

const fakeUser: UserDTO = {
  id: 1,
  email: "ana@colegio.cl",
  nombre: "Ana Díaz",
  entityId: 1,
  roles: ["DOCENTE"],
  menus: [],
};

beforeEach(() => {
  vi.clearAllMocks();
  useAuthStore.setState({ token: "t", user: fakeUser });
});

describe("DashboardPage", () => {
  it("saluda al usuario por su nombre", async () => {
    getMock.mockResolvedValue({ rol: "DOCENTE", user: {}, asignaturas: [], alumnosACargo: 0, mensajesNoLeidos: 0, evaluacionesPendientes: 0 });
    render(<DashboardPage />);
    expect(await screen.findByText("Ana Díaz")).toBeInTheDocument();
  });

  it("muestra las asignaturas de un docente", async () => {
    getMock.mockResolvedValue({
      rol: "DOCENTE",
      user: {},
      asignaturas: [{ id: 1, nombre: "Matemáticas", cursoId: 1, cursoNombre: "8°A", docenteId: 2, docenteNombre: "Prof" }],
      alumnosACargo: 30,
      mensajesNoLeidos: 2,
      evaluacionesPendientes: 1,
    });
    render(<DashboardPage />);
    expect(await screen.findByText("Mis asignaturas")).toBeInTheDocument();
    expect(screen.getByText("Matemáticas")).toBeInTheDocument();
  });

  it("muestra los totales de un admin", async () => {
    getMock.mockResolvedValue({ rol: "ADMIN", user: {}, totalUsuarios: 10, totalCursos: 5, totalAsignaturas: 8, mensajesNoLeidos: 0 });
    render(<DashboardPage />);
    expect(await screen.findByText("Usuarios")).toBeInTheDocument();
    expect(screen.getByText("10")).toBeInTheDocument();
  });

  it("muestra los datos de un estudiante", async () => {
    getMock.mockResolvedValue({
      rol: "ESTUDIANTE",
      user: {},
      alumno: { id: 1, nombre: "Ana Díaz", rut: "11.111.111-1", cursoId: 1, cursoNombre: "8°A", apoderados: [] },
      asistenciaPorcentaje: 95,
      promedioGeneral: 6.0,
      mensajesNoLeidos: 0,
    });
    render(<DashboardPage />);
    expect(await screen.findByText("Mis datos")).toBeInTheDocument();
  });

  it("muestra los pupilos de un apoderado", async () => {
    getMock.mockResolvedValue({
      rol: "APODERADO",
      user: {},
      pupilos: [{ alumno: { id: 1, nombre: "Hijo Uno", rut: "1-1", cursoId: 1, cursoNombre: "8°A", apoderados: [] }, asistenciaPorcentaje: 90, promedioGeneral: 5.5, ultimasAnotaciones: [] }],
      mensajesNoLeidos: 0,
    });
    render(<DashboardPage />);
    expect(await screen.findByText("Hijo Uno")).toBeInTheDocument();
  });

  it("muestra un error si falla la carga", async () => {
    getMock.mockRejectedValue(new Error("Sin conexión"));
    render(<DashboardPage />);
    expect(await screen.findByText("Sin conexión")).toBeInTheDocument();
  });
});
