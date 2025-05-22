ARG APP_INSIGHTS_AGENT_VERSION=3.7.0
FROM hmctspublic.azurecr.io/base/java${PLATFORM}:17-distroless

ENV APP div-document-generator.jar
COPY lib/applicationinsights.json /opt/app/

COPY lib/AI-Agent.xml /opt/app/
COPY build/libs/$APP /opt/app/

EXPOSE 4007

CMD ["div-document-generator.jar"]
