### Build Step
FROM node:23-alpine AS builder

# change working directory
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build
RUN npm prune --production

### Serve Step
FROM node:23-alpine

WORKDIR /app
RUN echo "['SIGINT', 'SIGTERM', 'SIGQUIT'].forEach(signal => process.on(signal, () => process.exit())); await import('./index.js')" > docker-entrypoint.js
COPY --from=builder /app/package*.json .
RUN npm ci --production --ignore-scripts
COPY --from=builder /app/build .

EXPOSE 3000
CMD ["node", "./docker-entrypoint.js"]