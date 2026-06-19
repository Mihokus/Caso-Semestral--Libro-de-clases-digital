import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import Spinner from "./Spinner";

describe("Spinner", () => {
  it("muestra el texto por defecto", () => {
    render(<Spinner />);
    expect(screen.getByText("Cargando...")).toBeInTheDocument();
  });

  it("muestra un texto personalizado", () => {
    render(<Spinner text="Buscando..." />);
    expect(screen.getByText("Buscando...")).toBeInTheDocument();
  });

  it("aplica la clase grande cuando large es true", () => {
    const { container } = render(<Spinner large />);
    const spinner = container.querySelector(".spinner");
    expect(spinner?.className).toContain("spinner-lg");
  });

  it("no aplica la clase grande por defecto", () => {
    const { container } = render(<Spinner />);
    const spinner = container.querySelector(".spinner");
    expect(spinner?.className).not.toContain("spinner-lg");
  });
});
