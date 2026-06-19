# Frontend - React + TypeScript + Vite (servidor de desarrollo)
FROM node:20-alpine

WORKDIR /app

# Instala dependencias (incluye los workspaces de packages/*)
COPY package.json package-lock.json ./
COPY packages ./packages
RUN npm install

# Copia el resto del codigo fuente
COPY . .

# Puerto del dev server de Vite (configurado en vite.config.ts: host 0.0.0.0)
EXPOSE 5173

CMD ["npm", "run", "dev"]
