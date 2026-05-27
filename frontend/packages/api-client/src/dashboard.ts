import { client } from "./client";
import type { DashboardDTO } from "./types";

export async function get(): Promise<DashboardDTO> {
  const { data } = await client.get<DashboardDTO>("/api/dashboard");
  return data;
}
