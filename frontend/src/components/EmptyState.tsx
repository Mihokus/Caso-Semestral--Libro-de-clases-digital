export default function EmptyState({ emoji = "📫", message = "Sin datos" }: { emoji?: string; message?: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-10 text-slate-400">
      <span className="text-4xl mb-2">{emoji}</span>
      <span className="text-sm">{message}</span>
    </div>
  );
}
