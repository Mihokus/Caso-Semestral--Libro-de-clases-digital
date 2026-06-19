import { describe, it, expect, vi, beforeEach } from "vitest";
import { renderHook } from "@testing-library/react";
import type { UserDTO } from "@libroclases/api-client";

vi.mock("@libroclases/api-client", () => ({
  auth: { logout: vi.fn() },
  getToken: vi.fn(() => null),
}));

import { auth } from "@libroclases/api-client";
import {
  useAuthStore,
  useUser,
  useIsAuthenticated,
  useHasRole,
  useHasAnyRole,
} from "./auth";

const fakeUser: UserDTO = {
  id: 1,
  email: "ana@colegio.cl",
  nombre: "Ana Díaz",
  entityId: null,
  roles: ["DOCENTE"],
  menus: [],
};

beforeEach(() => {
  localStorage.clear();
  useAuthStore.setState({ token: null, user: null });
  vi.clearAllMocks();
});

describe("authStore", () => {
  it("setSession guarda token y usuario en el estado", () => {
    useAuthStore.getState().setSession("tok123", fakeUser);
    expect(useAuthStore.getState().token).toBe("tok123");
    expect(useAuthStore.getState().user).toEqual(fakeUser);
  });

  it("setSession persiste el usuario en localStorage", () => {
    useAuthStore.getState().setSession("tok123", fakeUser);
    expect(localStorage.getItem("libroclases.user")).toContain("Ana Díaz");
  });

  it("clear deja token y usuario en null", () => {
    useAuthStore.getState().setSession("tok123", fakeUser);
    useAuthStore.getState().clear();
    expect(useAuthStore.getState().token).toBeNull();
    expect(useAuthStore.getState().user).toBeNull();
  });

  it("clear llama a auth.logout y limpia el localStorage", () => {
    useAuthStore.getState().setSession("tok123", fakeUser);
    useAuthStore.getState().clear();
    expect(auth.logout).toHaveBeenCalledTimes(1);
    expect(localStorage.getItem("libroclases.user")).toBeNull();
  });
});

describe("selectores", () => {
  it("useUser devuelve el usuario actual", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useUser());
    expect(result.current).toEqual(fakeUser);
  });

  it("useIsAuthenticated es true con token y usuario", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useIsAuthenticated());
    expect(result.current).toBe(true);
  });

  it("useIsAuthenticated es false sin token", () => {
    useAuthStore.setState({ token: null, user: fakeUser });
    const { result } = renderHook(() => useIsAuthenticated());
    expect(result.current).toBe(false);
  });

  it("useHasRole es true cuando el usuario tiene el rol", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useHasRole("DOCENTE"));
    expect(result.current).toBe(true);
  });

  it("useHasRole es false cuando el usuario no tiene el rol", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useHasRole("ADMIN"));
    expect(result.current).toBe(false);
  });

  it("useHasAnyRole es true si tiene al menos uno de los roles", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useHasAnyRole(["ADMIN", "DOCENTE"]));
    expect(result.current).toBe(true);
  });

  it("useHasAnyRole es false si no tiene ninguno", () => {
    useAuthStore.setState({ token: "t", user: fakeUser });
    const { result } = renderHook(() => useHasAnyRole(["ADMIN", "APODERADO"]));
    expect(result.current).toBe(false);
  });
});
