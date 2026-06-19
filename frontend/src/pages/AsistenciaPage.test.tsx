import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
  academica: { listCursos: vi.fn() },
  asistencia: {
    listByCurso: vi.fn(),
    registrarBulk: vi.fn(),
    listAlumnos: vi.fn(),
    historialAlumno: vi.fn(),
    anotacionesAlumno: vi.fn(),
    registrarAnotacion: vi.fn(),
  },
}));

import { academica, asistencia } from "@libroclases/api-client";
import AsistenciaPage from "./AsistenciaPage";

const cursos = [{ id: 1, nombre: "8°A", nivel: "Básica", cantidadAlumnos: 2 }];
const alumnos = [{ id: 10, nombre: "Pedro Soto", rut: "1-1", cursoId: 1, cursoNombre: "8°A", apoderados: [] }];

beforeEach(() => {
  vi.clearAllMocks();
  (academica.listCursos as Mock).mockResolvedValue(cursos);
  (asistencia.listAlumnos as Mock).mockResolvedValue(alumnos);
});

describe("AsistenciaPage", () => {
  it("muestra las tres pestañas", () => {
    render(<AsistenciaPage />);
    expect(screen.getByRole("button", { name: /tomar asistencia/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /historial/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /anotaciones/i })).toBeInTheDocument();
  });

  it("carga los alumnos de un curso al tomar asistencia", async () => {
    (asistencia.listByCurso as Mock).mockResolvedValue(alumnos);
    const user = userEvent.setup();
    render(<AsistenciaPage />);
    await screen.findByRole("option", { name: /8°A/ });
    await user.click(screen.getByRole("button", { name: /cargar alumnos/i }));
    expect(await screen.findByText("Pedro Soto")).toBeInTheDocument();
  });

  it("guarda la asistencia del día", async () => {
    (asistencia.listByCurso as Mock).mockResolvedValue(alumnos);
    (asistencia.registrarBulk as Mock).mockResolvedValue([]);
    const user = userEvent.setup();
    render(<AsistenciaPage />);
    await screen.findByRole("option", { name: /8°A/ });
    await user.click(screen.getByRole("button", { name: /cargar alumnos/i }));
    await screen.findByText("Pedro Soto");
    await user.click(screen.getByRole("button", { name: /guardar asistencia/i }));
    await waitFor(() => expect(asistencia.registrarBulk).toHaveBeenCalled());
  });

  it("marca a todos los alumnos con un mismo estado", async () => {
    (asistencia.listByCurso as Mock).mockResolvedValue(alumnos);
    const user = userEvent.setup();
    render(<AsistenciaPage />);
    await screen.findByRole("option", { name: /8°A/ });
    await user.click(screen.getByRole("button", { name: /cargar alumnos/i }));
    await screen.findByText("Pedro Soto");
    const selects = screen.getAllByRole("combobox");
    const estado = selects[selects.length - 1] as HTMLSelectElement;
    expect(estado.value).toBe("PRESENTE");
    await user.click(screen.getByRole("button", { name: "AUSENTE" }));
    expect(estado.value).toBe("AUSENTE");
  });

  it("muestra el historial con el estado de asistencia", async () => {
    (asistencia.historialAlumno as Mock).mockResolvedValue([
      { id: 1, alumnoId: 10, alumnoNombre: "Pedro Soto", cursoId: 1, cursoNombre: "8°A", fecha: "2026-06-01", estado: "PRESENTE", registradoPor: { id: 2, nombre: "Prof" } },
    ]);
    const user = userEvent.setup();
    render(<AsistenciaPage />);
    await user.click(screen.getByRole("button", { name: /historial/i }));
    await screen.findByRole("option", { name: /Pedro Soto/ });
    await user.click(screen.getByRole("button", { name: /buscar/i }));
    expect(await screen.findByText("PRESENTE")).toBeInTheDocument();
  });

  it("lista las anotaciones de un alumno", async () => {
    (asistencia.anotacionesAlumno as Mock).mockResolvedValue([
      { id: 1, alumnoId: 10, alumnoNombre: "Pedro Soto", tipo: "POSITIVA", descripcion: "Buen trabajo", fecha: "2026-06-01", registradoPor: { id: 2, nombre: "Prof" } },
    ]);
    const user = userEvent.setup();
    render(<AsistenciaPage />);
    await user.click(screen.getByRole("button", { name: /anotaciones/i }));
    await screen.findByRole("option", { name: /Pedro Soto/ });
    await user.click(screen.getByRole("button", { name: /ver anotaciones/i }));
    expect(await screen.findByText("Buen trabajo")).toBeInTheDocument();
  });
});
