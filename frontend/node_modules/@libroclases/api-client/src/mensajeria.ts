import { client } from "./client";
import type { MensajeComunicadoRequest, MensajeDTO, MensajeDirectoRequest } from "./types";

export async function inbox(): Promise<MensajeDTO[]> {
  const { data } = await client.get<MensajeDTO[]>("/api/mensajes");
  return data;
}

export async function enviados(): Promise<MensajeDTO[]> {
  const { data } = await client.get<MensajeDTO[]>("/api/mensajes/enviados");
  return data;
}

export async function byId(id: number): Promise<MensajeDTO> {
  const { data } = await client.get<MensajeDTO>(`/api/mensajes/${id}`);
  return data;
}

export async function enviarDirecto(req: MensajeDirectoRequest): Promise<MensajeDTO> {
  const { data } = await client.post<MensajeDTO>("/api/mensajes/directo", req);
  return data;
}

export async function enviarComunicado(req: MensajeComunicadoRequest): Promise<MensajeDTO> {
  const { data } = await client.post<MensajeDTO>("/api/mensajes/comunicado", req);
  return data;
}

export async function marcarLeido(id: number): Promise<MensajeDTO> {
  const { data } = await client.put<MensajeDTO>(`/api/mensajes/${id}/leido`);
  return data;
}

export async function eliminar(id: number): Promise<void> {
  await client.delete(`/api/mensajes/${id}`);
}
