import { useEffect, useRef } from "react";

export default function Modal({
  open,
  title,
  message,
  confirmLabel = "Confirmar",
  cancelLabel = "Cancelar",
  variant = "danger",
  onConfirm,
  onCancel,
}: {
  open: boolean;
  title: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  variant?: "danger" | "primary";
  onConfirm: () => void;
  onCancel: () => void;
}) {
  const overlayRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!open) return;
    const handler = (e: KeyboardEvent) => { if (e.key === "Escape") onCancel(); };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [open, onCancel]);

  if (!open) return null;

  const btnClass = variant === "danger" ? "btn btn-danger" : "btn btn-primary";

  return (
    <div
      ref={overlayRef}
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/40"
      onClick={(e) => { if (e.target === overlayRef.current) onCancel(); }}
    >
      <div className="bg-white rounded-xl shadow-xl p-6 w-full max-w-sm mx-4">
        <h3 className="text-lg font-semibold text-slate-900 mt-0 mb-2">{title}</h3>
        <p className="text-sm text-slate-600 mb-5">{message}</p>
        <div className="flex justify-end gap-2">
          <button onClick={onCancel} className="btn btn-secondary">{cancelLabel}</button>
          <button onClick={onConfirm} className={btnClass}>{confirmLabel}</button>
        </div>
      </div>
    </div>
  );
}
