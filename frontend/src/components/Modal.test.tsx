import { describe, it, expect, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import Modal from "./Modal";

const baseProps = {
  open: true,
  title: "Eliminar",
  message: "¿Seguro?",
  onConfirm: () => {},
  onCancel: () => {},
};

describe("Modal", () => {
  it("no renderiza nada cuando open es false", () => {
    const { container } = render(<Modal {...baseProps} open={false} />);
    expect(container).toBeEmptyDOMElement();
  });

  it("muestra título y mensaje cuando está abierto", () => {
    render(<Modal {...baseProps} />);
    expect(screen.getByText("Eliminar")).toBeInTheDocument();
    expect(screen.getByText("¿Seguro?")).toBeInTheDocument();
  });

  it("muestra las etiquetas por defecto", () => {
    render(<Modal {...baseProps} />);
    expect(screen.getByText("Confirmar")).toBeInTheDocument();
    expect(screen.getByText("Cancelar")).toBeInTheDocument();
  });

  it("muestra etiquetas personalizadas", () => {
    render(<Modal {...baseProps} confirmLabel="Borrar" cancelLabel="Volver" />);
    expect(screen.getByText("Borrar")).toBeInTheDocument();
    expect(screen.getByText("Volver")).toBeInTheDocument();
  });

  it("llama a onConfirm al hacer clic en confirmar", () => {
    const onConfirm = vi.fn();
    render(<Modal {...baseProps} confirmLabel="Borrar" onConfirm={onConfirm} />);
    fireEvent.click(screen.getByText("Borrar"));
    expect(onConfirm).toHaveBeenCalledTimes(1);
  });

  it("llama a onCancel al hacer clic en cancelar", () => {
    const onCancel = vi.fn();
    render(<Modal {...baseProps} cancelLabel="Volver" onCancel={onCancel} />);
    fireEvent.click(screen.getByText("Volver"));
    expect(onCancel).toHaveBeenCalledTimes(1);
  });

  it("llama a onCancel al presionar Escape", () => {
    const onCancel = vi.fn();
    render(<Modal {...baseProps} onCancel={onCancel} />);
    fireEvent.keyDown(window, { key: "Escape" });
    expect(onCancel).toHaveBeenCalledTimes(1);
  });

  it("usa el estilo primary cuando variant es primary", () => {
    render(<Modal {...baseProps} confirmLabel="Aceptar" variant="primary" />);
    expect(screen.getByText("Aceptar").className).toContain("btn-primary");
  });

  it("usa el estilo danger por defecto", () => {
    render(<Modal {...baseProps} confirmLabel="Borrar" />);
    expect(screen.getByText("Borrar").className).toContain("btn-danger");
  });
});
