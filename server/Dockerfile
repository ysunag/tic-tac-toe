FROM ubuntu:16.04

RUN apt-get update && apt-get install -y \
  libmicrohttpd-dev \
  libjansson-dev \
  libcurl4-gnutls-dev \
  libgnutls28-dev \
  libgcrypt20-dev 

# Copy the server C binary and the JSON library into the container
COPY ./rest_server /rest_server

COPY ./libs/libjansson.so.4 /usr/lib/libjansson.so.4
COPY ./libs/libulfius.so.2.3 /usr/lib/libulfius.so.2.3
#COPY ./libs/libmicrohttpd.so.10.34.0 /usr/lib/libmicrohttpd.so.10.34.0
#COPY ./libs/libmicrohttpd.so.10.34.0 /usr/lib/libmicrohttpd.so.10
#COPY ./libs/libmicrohttpd.a /usr/lib/libmicrohttpd.a
#COPY ./libs/libyder.so.1.3.3 /usr/lib/libyder.so.1.3.3
COPY ./libs/libyder.so /usr/lib/libyder.so
COPY ./libs/liborcania.so /usr/lib/liborcania.so

# Expose the port that the server will listen on
EXPOSE 8085

# Set the LD_LIBRARY_PATH so that the server binary will find the JSON library
ENV LD_LIBRARY_PATH ${LD_LIBRARY_PATH}:/usr/lib

CMD ["/rest_server"]