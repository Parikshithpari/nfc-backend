FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/NFC-Backend-0.0.1-SNAPSHOT.jar /app/NFC-Backend.jar
COPY manual-invoices manual-invoices
COPY online-invoices online-invoices
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "NFC-Backend.jar"]