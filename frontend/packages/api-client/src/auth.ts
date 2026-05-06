import { client, setToken, clearToken } from "./client";
import type { LoginRequest, LoginResponse, RegisterRequest, UserDTO } from "./types";

export async function login(req: LoginRequest): Promise<LoginResponse> {
  const { data } = await client.post<LoginResponse>("/api/login/login", req);
  setToken(data.token);
  return data;
}

export async function register(req: RegisterRequest): Promise<LoginResponse> {
  const { data } = await client.post<LoginResponse>("/api/register", req);
  setToken(data.token);
  return data;
}

export async function me(): Promise<UserDTO> {
  const { data } = await client.get<UserDTO>("/api/me");
  return data;
}

export function logout(): void {
  clearToken();
}

export interface JwtPayload {
  sub?: string;
  userId?: number;
  nombre?: string;
  entityId?: number | null;
  roles?: string[];
  iat?: number;
  exp?: number;
}

export function parseJwt(token: string): JwtPayload | null {
  try {
    const segment = token.split(".")[1];
    if (!segment) return null;
    const decoded = atob(segment.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(decoded) as JwtPayload;
  } catch {
    return null;
  }
}

export function isExpired(token: string): boolean {
  const p = parseJwt(token);
  if (!p?.exp) return true;
  return p.exp * 1000 < Date.now();
}
