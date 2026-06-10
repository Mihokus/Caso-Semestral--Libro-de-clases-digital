import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import EmptyState from "./EmptyState";

describe("EmptyState", () => {
  it("muestra el mensaje por defecto", () => {
    render(<EmptyState />);
    expect(screen.getByText("Sin datos")).toBeInTheDocument();
  });

  it("muestra un mensaje personalizado", () => {
    render(<EmptyState message="Sin mensajes" />);
    expect(screen.getByText("Sin mensajes")).toBeInTheDocument();
  });

  it("muestra el emoji indicado", () => {
    render(<EmptyState emoji="📭" message="Vacío" />);
    expect(screen.getByText("📭")).toBeInTheDocument();
  });
});
