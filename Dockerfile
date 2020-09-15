ARG APP_INSIGHTS_AGENT_VERSION=2.5.1
FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.4

ENV APP div-document-generator.jar

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/$APP /opt/app/

EXPOSE 4007

CMD ["div-document-generator.jar"]
