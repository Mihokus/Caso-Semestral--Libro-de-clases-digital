const ATTENDANCE: Record<string, string> = {
  PRESENTE: "bg-green-100 text-green-800",
  AUSENTE: "bg-red-100 text-red-800",
  JUSTIFICADO: "bg-yellow-100 text-yellow-800",
};

const ANNOTATION: Record<string, string> = {
  POSITIVA: "bg-green-100 text-green-800",
  NEGATIVA: "bg-red-100 text-red-800",
  NEUTRAL: "bg-slate-100 text-slate-700",
};

export function AttendanceBadge({ status }: { status: string }) {
  const cls = ATTENDANCE[status] ?? "bg-slate-100 text-slate-700";
  return <span className={`${cls} text-xs font-semibold px-2.5 py-1 rounded-full`}>{status}</span>;
}

export function AnnotationBadge({ type }: { type: string }) {
  const cls = ANNOTATION[type] ?? "bg-slate-100 text-slate-700";
  return <span className={`${cls} text-xs font-semibold px-2.5 py-1 rounded-full`}>{type}</span>;
}

export function GradeBadge({ grade }: { grade: number }) {
  let cls = "bg-red-100 text-red-800";
  if (grade >= 5.0) cls = "bg-green-100 text-green-800";
  else if (grade >= 4.0) cls = "bg-yellow-100 text-yellow-800";
  return <span className={`${cls} text-xs font-bold px-2.5 py-1 rounded-full`}>{grade}</span>;
}
