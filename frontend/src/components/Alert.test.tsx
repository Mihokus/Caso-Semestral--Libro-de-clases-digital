import { describe, it, expect, vi, afterEach } from "vitest";
import { render, screen, fireEvent, act } from "@testing-library/react";
import Alert from "./Alert";

afterEach(() => {
  vi.useRealTimers();
});

describe("Alert", () => {
  it("no renderiza nada cuando el mensaje es null", () => {
    const { container } = render(<Alert type="success" message={null} />);
    expect(container).toBeEmptyDOMElement();
  });

  it("muestra el mensaje cuando hay texto", () => {
    render(<Alert type="success" message="Guardado" autoClose={false} />);
    expect(screen.getByText("Guardado")).toBeInTheDocument();
  });

  it("muestra el emoji de éxito", () => {
    render(<Alert type="success" message="Ok" autoClose={false} />);
    expect(screen.getByText("✅")).toBeInTheDocument();
  });

  it("muestra el emoji de error", () => {
    render(<Alert type="error" message="Falló" autoClose={false} />);
    expect(screen.getByText("❌")).toBeInTheDocument();
  });

  it("muestra el emoji de advertencia", () => {
    render(<Alert type="warning" message="Cuidado" autoClose={false} />);
    expect(screen.getByText("⚠️")).toBeInTheDocument();
  });

  it("no muestra botón de cerrar si no hay onClose", () => {
    render(<Alert type="success" message="Sin cerrar" autoClose={false} />);
    expect(screen.queryByRole("button")).not.toBeInTheDocument();
  });

  it("llama a onClose al hacer clic en cerrar", () => {
    const onClose = vi.fn();
    render(<Alert type="error" message="Error" onClose={onClose} autoClose={false} />);
    fireEvent.click(screen.getByRole("button"));
    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("se cierra automáticamente después del tiempo indicado", () => {
    vi.useFakeTimers();
    const onClose = vi.fn();
    render(<Alert type="success" message="Auto" onClose={onClose} autoClose={3000} />);
    expect(onClose).not.toHaveBeenCalled();
    act(() => {
      vi.advanceTimersByTime(3000);
    });
    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("no se cierra solo cuando autoClose es false", () => {
    vi.useFakeTimers();
    const onClose = vi.fn();
    render(<Alert type="success" message="Persistente" onClose={onClose} autoClose={false} />);
    act(() => {
      vi.advanceTimersByTime(10000);
    });
    expect(onClose).not.toHaveBeenCalled();
  });
});
