import { createBrowserRouter, Navigate } from "react-router-dom";
import Layout from "@/components/Layout";
import PrivateRoute from "@/components/PrivateRoute";
import LoginPage from "@/pages/LoginPage";
import RegisterPage from "@/pages/RegisterPage";
import DashboardPage from "@/pages/DashboardPage";
import AsistenciaPage from "@/pages/AsistenciaPage";
import MensajesPage from "@/pages/MensajesPage";
import AcademicaPage from "@/pages/AcademicaPage";
import AdminPage from "@/pages/AdminPage";

export const router = createBrowserRouter([
  { path: "/login", element: <LoginPage /> },
  { path: "/register", element: <RegisterPage /> },
  {
    path: "/",
    element: (
      <PrivateRoute>
        <Layout />
      </PrivateRoute>
    ),
    children: [
      { index: true, element: <Navigate to="/dashboard" replace /> },
      { path: "dashboard", element: <DashboardPage /> },
      { path: "asistencia", element: <AsistenciaPage /> },
      { path: "asistencia/tomar", element: <AsistenciaPage /> },
      { path: "mensajes", element: <MensajesPage /> },
      { path: "academica", element: <AcademicaPage /> },
      { path: "asignaturas", element: <AcademicaPage /> },
      { path: "notas/registrar", element: <AcademicaPage /> },
      { path: "rendimiento", element: <AcademicaPage /> },
      { path: "pupilos", element: <DashboardPage /> },
      { path: "pupilos/asistencia", element: <AsistenciaPage /> },
      { path: "pupilos/notas", element: <AcademicaPage /> },
      { path: "mi-asistencia", element: <AsistenciaPage /> },
      { path: "mis-notas", element: <AcademicaPage /> },
      {
        path: "admin/users",
        element: (
          <PrivateRoute roles={["ADMIN"]}>
            <AdminPage />
          </PrivateRoute>
        ),
      },
      {
        path: "admin/roles",
        element: (
          <PrivateRoute roles={["ADMIN"]}>
            <AdminPage />
          </PrivateRoute>
        ),
      },
      {
        path: "admin/menus",
        element: (
          <PrivateRoute roles={["ADMIN"]}>
            <AdminPage />
          </PrivateRoute>
        ),
      },
    ],
  },
  { path: "*", element: <Navigate to="/dashboard" replace /> },
]);
