import { describe, it, expect, vi, beforeEach, type Mock } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
  academica: { listCursos: vi.fn() },
  mensajeria: {
    inbox: vi.fn(),
    enviados: vi.fn(),
    marcarLeido: vi.fn(),
    eliminar: vi.fn(),
    enviarDirecto: vi.fn(),
    enviarComunicado: vi.fn(),
  },
}));

import { academica, mensajeria } from "@libroclases/api-client";
import MensajesPage from "./MensajesPage";

const mensaje = {
  id: 1,
  tipo: "DIRECTO",
  titulo: "Reunión de apoderados",
  contenido: "Los esperamos el viernes",
  remitente: { id: 2, nombre: "Prof. García", rol: "DOCENTE" },
  destinatario: { id: 1, nombre: "Ana", rol: "APODERADO" },
  cursoId: null,
  fechaEnvio: "2026-06-01",
  leido: false,
};

beforeEach(() => {
  vi.clearAllMocks();
  (mensajeria.inbox as Mock).mockResolvedValue([mensaje]);
  (mensajeria.enviados as Mock).mockResolvedValue([]);
  (academica.listCursos as Mock).mockResolvedValue([{ id: 1, nombre: "8°A", nivel: "Básica", cantidadAlumnos: 0 }]);
});

describe("MensajesPage", () => {
  it("muestra los mensajes del inbox", async () => {
    render(<MensajesPage />);
    expect(await screen.findByText("Reunión de apoderados")).toBeInTheDocument();
  });

  it("expande un mensaje para ver su contenido", async () => {
    const user = userEvent.setup();
    render(<MensajesPage />);
    await user.click(await screen.findByText("Reunión de apoderados"));
    expect(screen.getByText("Los esperamos el viernes")).toBeInTheDocument();
  });

  it("pide confirmación y elimina un mensaje", async () => {
    (mensajeria.eliminar as Mock).mockResolvedValue(undefined);
    const user = userEvent.setup();
    render(<MensajesPage />);
    await user.click(await screen.findByText("Reunión de apoderados"));
    await user.click(screen.getByRole("button", { name: /eliminar/i }));
    expect(screen.getByText(/no se puede deshacer/i)).toBeInTheDocument();
    const confirmar = screen.getAllByRole("button", { name: /eliminar/i });
    await user.click(confirmar[confirmar.length - 1]);
    await waitFor(() => expect(mensajeria.eliminar).toHaveBeenCalledWith(1));
  });

  it("marca un mensaje como leído", async () => {
    (mensajeria.marcarLeido as Mock).mockResolvedValue({});
    const user = userEvent.setup();
    render(<MensajesPage />);
    await user.click(await screen.findByText("Reunión de apoderados"));
    await user.click(screen.getByRole("button", { name: /marcar leído/i }));
    await waitFor(() => expect(mensajeria.marcarLeido).toHaveBeenCalledWith(1));
  });

  it("permite redactar y enviar un mensaje directo", async () => {
    (mensajeria.enviarDirecto as Mock).mockResolvedValue({});
    const user = userEvent.setup();
    render(<MensajesPage />);
    await user.click(screen.getByRole("button", { name: /nuevo/i }));
    await user.type(screen.getByPlaceholderText("Ej: 5"), "7");
    const textboxes = screen.getAllByRole("textbox");
    await user.type(textboxes[0], "Hola");
    await user.type(textboxes[1], "Contenido de prueba");
    await user.click(screen.getByRole("button", { name: /enviar mensaje/i }));
    await waitFor(() =>
      expect(mensajeria.enviarDirecto).toHaveBeenCalledWith(
        expect.objectContaining({ destinatarioId: 7, titulo: "Hola", contenido: "Contenido de prueba" }),
      ),
    );
  });

  it("cambia a la pestaña de enviados", async () => {
    const user = userEvent.setup();
    render(<MensajesPage />);
    await user.click(screen.getByRole("button", { name: /enviados/i }));
    await waitFor(() => expect(mensajeria.enviados).toHaveBeenCalled());
  });
});
