FROM mrrm/web.go

RUN mkdir -p /app
WORKDIR /app

COPY ts-news-service/src/main/main.go /app/main.go
RUN go build -o app main.go
CMD [ "./app" ]

EXPOSE 12862