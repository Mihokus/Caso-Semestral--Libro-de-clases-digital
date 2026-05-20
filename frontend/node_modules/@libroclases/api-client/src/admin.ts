import { client } from "./client";
import type { MenuDTO, MenuRequest, RoleDTO, UpdateRolesRequest, UserDTO } from "./types";

export async function listUsers(): Promise<UserDTO[]> {
  const { data } = await client.get<UserDTO[]>("/api/admin/users");
  return data;
}

export async function updateUserRoles(id: number, req: UpdateRolesRequest): Promise<UserDTO> {
  const { data } = await client.put<UserDTO>(`/api/admin/users/${id}/roles`, req);
  return data;
}

export async function deleteUser(id: number): Promise<void> {
  await client.delete(`/api/admin/users/${id}`);
}

export async function listRoles(): Promise<RoleDTO[]> {
  const { data } = await client.get<RoleDTO[]>("/api/admin/roles");
  return data;
}

export async function createRole(req: { nombre: string; descripcion?: string }): Promise<RoleDTO> {
  const { data } = await client.post<RoleDTO>("/api/admin/roles", req);
  return data;
}

export async function listMenus(): Promise<MenuDTO[]> {
  const { data } = await client.get<MenuDTO[]>("/api/admin/menus");
  return data;
}

export async function createMenu(req: MenuRequest): Promise<MenuDTO> {
  const { data } = await client.post<MenuDTO>("/api/admin/menus", req);
  return data;
}

export async function updateMenuRoles(id: number, req: UpdateRolesRequest): Promise<MenuDTO> {
  const { data } = await client.put<MenuDTO>(`/api/admin/menus/${id}/roles`, req);
  return data;
}
