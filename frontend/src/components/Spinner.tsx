export default function Spinner({ text = "Cargando...", large = false }: { text?: string; large?: boolean }) {
  return (
    <div className="flex items-center gap-3 py-4 text-slate-500">
      <div className={`spinner ${large ? "spinner-lg" : ""}`} />
      <span className="text-sm">{text}</span>
    </div>
  );
}
