import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
  academica: {
    listAsignaturas: vi.fn(),
    listCursos: vi.fn(),
    crearAsignatura: vi.fn(),
    crearCurso: vi.fn(),
    registrarNota: vi.fn(),
    notasAlumno: vi.fn(),
    rendimiento: vi.fn(),
  },
  asistencia: { listAlumnos: vi.fn() },
}));

import { academica, asistencia } from "@libroclases/api-client";
import AcademicaPage from "./AcademicaPage";

const cursos = [{ id: 1, nombre: "8°A", nivel: "Básica", cantidadAlumnos: 2 }];
const asignaturas = [{ id: 1, nombre: "Matemáticas", cursoId: 1, cursoNombre: "8°A", docenteId: 2, docenteNombre: "Prof" }];
const alumnos = [{ id: 10, nombre: "Pedro Soto", rut: "1-1", cursoId: 1, cursoNombre: "8°A", apoderados: [] }];

beforeEach(() => {
  vi.clearAllMocks();
  (academica.listAsignaturas as Mock).mockResolvedValue(asignaturas);
  (academica.listCursos as Mock).mockResolvedValue(cursos);
  (asistencia.listAlumnos as Mock).mockResolvedValue(alumnos);
});

describe("AcademicaPage", () => {
  it("muestra la lista de asignaturas por defecto", async () => {
    render(<AcademicaPage />);
    expect(await screen.findByText("Matemáticas")).toBeInTheDocument();
  });

  it("muestra los cursos al cambiar de pestaña", async () => {
    const user = userEvent.setup();
    render(<AcademicaPage />);
    await user.click(screen.getByRole("button", { name: /cursos/i }));
    expect(await screen.findByText("Básica")).toBeInTheDocument();
  });

  it("busca las notas de un alumno y muestra la nota con color", async () => {
    (academica.notasAlumno as Mock).mockResolvedValue([
      { id: 1, alumnoId: 10, alumnoNombre: "Pedro Soto", asignaturaId: 1, asignaturaNombre: "Matemáticas", nombre: "Prueba 1", nota: 6.5, ponderacion: 0.3, fecha: "2026-06-01", registradoPor: { id: 2, nombre: "Prof" } },
    ]);
    const user = userEvent.setup();
    render(<AcademicaPage />);
    await user.click(screen.getByRole("button", { name: /notas/i }));
    await screen.findAllByRole("option", { name: /Pedro Soto/ });
    await user.click(screen.getByRole("button", { name: /buscar/i }));
    expect(await screen.findByText("6.5")).toBeInTheDocument();
  });

  it("carga el rendimiento de una asignatura", async () => {
    (academica.rendimiento as Mock).mockResolvedValue({
      asignaturaId: 1,
      asignaturaNombre: "Matemáticas",
      promedioCurso: 5.8,
      cantidadEvaluaciones: 3,
      alumnos: [{ alumnoId: 10, alumnoNombre: "Pedro Soto", promedio: 6.2, cantidadEvaluaciones: 3 }],
    });
    const user = userEvent.setup();
    render(<AcademicaPage />);
    await user.click(screen.getByRole("button", { name: /rendimiento/i }));
    await screen.findByRole("option", { name: /Matemáticas/ });
    await user.click(screen.getByRole("button", { name: /cargar/i }));
    expect(await screen.findByText("Pedro Soto")).toBeInTheDocument();
    expect(screen.getByText("6.2")).toBeInTheDocument();
  });

  it("crea un curso nuevo", async () => {
    (academica.crearCurso as Mock).mockResolvedValue({});
    const user = userEvent.setup();
    render(<AcademicaPage />);
    await user.click(screen.getByRole("button", { name: /cursos/i }));
    await screen.findByText("Básica");
    const inputs = screen.getAllByRole("textbox");
    await user.type(inputs[0], "7°B");
    await user.type(inputs[1], "Básica");
    await user.click(screen.getByRole("button", { name: /crear curso/i }));
    await waitFor(() => expect(academica.crearCurso).toHaveBeenCalled());
  });
});
