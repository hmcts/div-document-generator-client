ARG APP_INSIGHTS_AGENT_VERSION=3.7.0
FROM hmctspublic.azurecr.io/base/java:11-distroless

ENV APP div-document-generator.jar
COPY lib/applicationinsights.json /opt/app/

COPY build/libs/$APP /opt/app/

EXPOSE 4007

CMD ["div-document-generator.jar"]
