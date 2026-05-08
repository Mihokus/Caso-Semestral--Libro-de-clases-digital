import { type ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useIsAuthenticated, useHasAnyRole } from "@/store/auth";

interface Props {
  children: ReactNode;
  roles?: string[];
}

export default function PrivateRoute({ children, roles }: Props) {
  const isAuthenticated = useIsAuthenticated();
  const allowed = useHasAnyRole(roles ?? []);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (roles && roles.length > 0 && !allowed) {
    return <Navigate to="/dashboard" replace />;
  }
  return <>{children}</>;
}
