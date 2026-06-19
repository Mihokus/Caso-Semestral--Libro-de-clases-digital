import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { AttendanceBadge, AnnotationBadge, GradeBadge } from "./StatusBadge";

describe("AttendanceBadge", () => {
  it("muestra el texto del estado", () => {
    render(<AttendanceBadge status="PRESENTE" />);
    expect(screen.getByText("PRESENTE")).toBeInTheDocument();
  });

  it("usa verde para PRESENTE", () => {
    render(<AttendanceBadge status="PRESENTE" />);
    expect(screen.getByText("PRESENTE").className).toContain("bg-green-100");
  });

  it("usa rojo para AUSENTE", () => {
    render(<AttendanceBadge status="AUSENTE" />);
    expect(screen.getByText("AUSENTE").className).toContain("bg-red-100");
  });

  it("usa amarillo para JUSTIFICADO", () => {
    render(<AttendanceBadge status="JUSTIFICADO" />);
    expect(screen.getByText("JUSTIFICADO").className).toContain("bg-yellow-100");
  });

  it("usa un estilo neutro para un estado desconocido", () => {
    render(<AttendanceBadge status="OTRO" />);
    expect(screen.getByText("OTRO").className).toContain("bg-slate-100");
  });
});

describe("AnnotationBadge", () => {
  it("usa verde para POSITIVA", () => {
    render(<AnnotationBadge type="POSITIVA" />);
    expect(screen.getByText("POSITIVA").className).toContain("bg-green-100");
  });

  it("usa rojo para NEGATIVA", () => {
    render(<AnnotationBadge type="NEGATIVA" />);
    expect(screen.getByText("NEGATIVA").className).toContain("bg-red-100");
  });

  it("usa gris para NEUTRAL", () => {
    render(<AnnotationBadge type="NEUTRAL" />);
    expect(screen.getByText("NEUTRAL").className).toContain("bg-slate-100");
  });
});

describe("GradeBadge", () => {
  it("muestra el valor de la nota", () => {
    render(<GradeBadge grade={5.5} />);
    expect(screen.getByText("5.5")).toBeInTheDocument();
  });

  it("usa rojo para notas bajo 4.0", () => {
    render(<GradeBadge grade={3.9} />);
    expect(screen.getByText("3.9").className).toContain("bg-red-100");
  });

  it("usa amarillo en el límite inferior 4.0", () => {
    render(<GradeBadge grade={4.0} />);
    expect(screen.getByText("4").className).toContain("bg-yellow-100");
  });

  it("usa amarillo justo bajo 5.0", () => {
    render(<GradeBadge grade={4.9} />);
    expect(screen.getByText("4.9").className).toContain("bg-yellow-100");
  });

  it("usa verde en el límite 5.0", () => {
    render(<GradeBadge grade={5.0} />);
    expect(screen.getByText("5").className).toContain("bg-green-100");
  });

  it("usa verde para notas altas", () => {
    render(<GradeBadge grade={6.8} />);
    expect(screen.getByText("6.8").className).toContain("bg-green-100");
  });
});
