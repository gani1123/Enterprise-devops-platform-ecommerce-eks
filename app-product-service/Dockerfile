FROM tomcat:10-jdk17-temurin

RUN groupadd -r appgroup && useradd -r -g appgroup -u 1001 appuser

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/ecommerce-api.war /usr/local/tomcat/webapps/ecommerce-api.war

RUN chown -R appuser:appgroup /usr/local/tomcat

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ecommerce-api/health || exit 1

CMD ["catalina.sh", "run"]
