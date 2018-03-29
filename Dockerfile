FROM openjdk:8-jre-alpine

LABEL maintainer="mathan.thavachelvam@hmcts.net"

COPY build/install/div-document-generator /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=100s --timeout=100s --retries=10 CMD http_proxy="" wget -q http://localhost:4007/health || exit 1

EXPOSE 4007

ENTRYPOINT ["/opt/app/bin/div-document-generator"]
