# Stage 1: Build dependencies
FROM node:18-alpine AS builder

WORKDIR /app

# Copy package.json first for better caching
COPY ts-ticket-office-service/package.json ./

# Install only production dependencies
RUN npm install --omit=dev --no-audit --no-fund

# Stage 2: Final image
FROM node:18-alpine

# Set environment variables
ENV NODE_ENV=production
ENV TZ=Asia/Shanghai

# Install tzdata and set timezone
RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

# Copy node_modules from builder stage
COPY --from=builder /app/node_modules ./node_modules

# Copy application files
COPY ts-ticket-office-service/bin ./bin
COPY ts-ticket-office-service/public ./public
COPY ts-ticket-office-service/app.js ./

# Use non-root user for security
RUN chown -R node:node /app
USER node

# Expose the service port
EXPOSE 16108

# Use node directly instead of npm start for better signal handling
CMD [ "node", "bin/www" ]