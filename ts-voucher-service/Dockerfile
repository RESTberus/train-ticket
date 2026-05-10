FROM python:3.14.4-alpine3.23

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

WORKDIR /app

COPY ts-voucher-service/requirements.txt ./
RUN pip install --no-cache-dir cryptography && \
    pip install --no-cache-dir -r requirements.txt

COPY ts-voucher-service/server.py ./
CMD [ "python", "-u", "server.py" ]

EXPOSE 16101