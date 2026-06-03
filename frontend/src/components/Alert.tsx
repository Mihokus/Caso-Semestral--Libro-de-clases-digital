import { useEffect, useState } from "react";

type AlertType = "success" | "error" | "warning";

const CONFIG: Record<AlertType, { emoji: string; bg: string; border: string; text: string }> = {
  success: { emoji: "✅", bg: "bg-green-50", border: "border-green-300", text: "text-green-800" },
  error: { emoji: "❌", bg: "bg-red-50", border: "border-red-300", text: "text-red-800" },
  warning: { emoji: "⚠️", bg: "bg-yellow-50", border: "border-yellow-300", text: "text-yellow-800" },
};

export default function Alert({
  type,
  message,
  onClose,
  autoClose = 5000,
}: {
  type: AlertType;
  message: string | null;
  onClose?: () => void;
  autoClose?: number | false;
}) {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    setVisible(true);
  }, [message]);

  useEffect(() => {
    if (!autoClose || !message) return;
    const t = setTimeout(() => {
      setVisible(false);
      onClose?.();
    }, autoClose);
    return () => clearTimeout(t);
  }, [message, autoClose, onClose]);

  if (!message || !visible) return null;

  const c = CONFIG[type];
  return (
    <div className={`${c.bg} ${c.border} ${c.text} border rounded-lg px-4 py-3 mt-3 flex items-start gap-3 text-sm`}>
      <span className="text-lg leading-none">{c.emoji}</span>
      <span className="flex-1">{message}</span>
      {onClose && (
        <button
          onClick={() => { setVisible(false); onClose(); }}
          className="text-current opacity-50 hover:opacity-100 text-lg leading-none"
        >
          &times;
        </button>
      )}
    </div>
  );
}
