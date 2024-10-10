# Etapa de construção
FROM ubuntu:latest AS build

# Atualiza e instala o OpenJDK 21
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

# Copia o código-fonte do projeto
COPY . .

# Instala o Maven
RUN apt-get install maven -y


# Compila o projeto
RUN mvn clean install

# Etapa final
FROM openjdk:21-jdk-slim

# Definição das variáveis de ambiente
ENV JAVA_OPTS="-Xms256m -Xmx512m" \
    APP_NAME="TecFlux" \
    BRASIL_API_URL="https://brasilapi.com.br/api/cnpj/v1/" \
    CRYPTO_PASSWORD="X3#fR8@9lK!wZq2&" \
    EMAIL="noreply.tecflux@gmail.com" \
    EMAIL_PASSWORD="bjfvwnljvefhwyjn" \
    JWT_PASSWORD="l8s$Km2@Qw9#Np3*Vr7&Tj4#Bx6!Zy1"

# Expõe a porta 8080
EXPOSE 8080

# Copia o JAR gerado para o contêiner
COPY --from=build /target/tecflux-0.0.1-SNAPSHOT.jar app.jar

# Comando de inicialização
ENTRYPOINT [ "java", "-jar", "app.jar" ]
