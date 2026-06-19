import { create } from "zustand";
import { auth, getToken, type UserDTO } from "@libroclases/api-client";

const USER_KEY = "libroclases.user";

function readUser(): UserDTO | null {
  if (typeof window === "undefined") return null;
  const raw = window.localStorage.getItem(USER_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as UserDTO;
  } catch {
    return null;
  }
}

function writeUser(user: UserDTO | null): void {
  if (typeof window === "undefined") return;
  if (user) {
    window.localStorage.setItem(USER_KEY, JSON.stringify(user));
  } else {
    window.localStorage.removeItem(USER_KEY);
  }
}

interface AuthState {
  token: string | null;
  user: UserDTO | null;
  setSession: (token: string, user: UserDTO) => void;
  clear: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: getToken(),
  user: readUser(),
  setSession: (token, user) => {
    writeUser(user);
    set({ token, user });
  },
  clear: () => {
    auth.logout();
    writeUser(null);
    set({ token: null, user: null });
  },
}));

export const useUser = () => useAuthStore((s) => s.user);
export const useToken = () => useAuthStore((s) => s.token);
export const useIsAuthenticated = () =>
  useAuthStore((s) => Boolean(s.token) && Boolean(s.user));
export const useHasRole = (role: string) =>
  useAuthStore((s) => s.user?.roles?.includes(role) ?? false);
export const useHasAnyRole = (roles: string[]) =>
  useAuthStore((s) => s.user?.roles?.some((r) => roles.includes(r)) ?? false);
