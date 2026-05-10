FROM node:18-alpine

RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

COPY ts-ticket-office-service/package.json ./
COPY ts-ticket-office-service/public ./public
RUN npm install --production

COPY ts-ticket-office-service/bin ./bin
COPY ts-ticket-office-service/app.js ./
CMD [ "npm", "start" ]

EXPOSE 16108