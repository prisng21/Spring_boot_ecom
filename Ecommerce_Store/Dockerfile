# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache Maven dependencies in a separate layer (faster rebuilds)
COPY pom.xml .
RUN mvn -B dependency:go-offline || true

COPY src ./src
RUN mvn -B clean package -DskipTests

# Explode the WAR: the app writes uploaded images into classpath folders
# (static/img/...), which only works when classes live on the filesystem,
# not inside a nested jar/war.
RUN mkdir -p /app/exploded && cd /app/exploded && jar -xf /app/target/Ecommerce_Store-0.0.1-SNAPSHOT.war

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/exploded .

EXPOSE 8080

# Run from the exploded classpath so ClassPathResource("static/img") resolves
# to a real folder and uploaded product/category/profile images are served.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-cp", "WEB-INF/classes:WEB-INF/lib/*:WEB-INF/lib-provided/*", "com.mdtalalwasim.ecommerce.EcommerceStoreApplication"]
