# Escolhe a imagem do Java
FROM openjdk:17-jdk-slim

# Cria pasta dentro do container
WORKDIR /app

# Copia o JAR gerado para o container
COPY target/orcamento-system-0.0.1-SNAPSHOT.jar

# Expõe a porta
EXPOSE $PORT

# Comando para rodar
CMD ["java", "-jar", "app.jar"]