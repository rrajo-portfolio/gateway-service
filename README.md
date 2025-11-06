# Gateway Service

Spring Cloud Gateway que expone un edge service único para el portfolio.

## Características
- TokenRelay: reenvía el JWT emitido por Keycloak a los microservicios.
- Circuit breaker con Resilience4j (`defaultCircuitBreaker`).
- Métricas y health checks en `/actuator/**` (Prometheus scrapea `/actuator/prometheus`).
- Rutas configuradas en `application.yml`:
  - `/catalog/**` → `catalog-service:8081`
  - `/users/**` → `users-service:8082`
  - `/orders/**` → `orders-service:8084`

## Configuración
Variables de entorno clave:
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI`
- `SECURITY_OAUTH2_EXPECTED_ISSUER`
- `GATEWAY_CATALOG_URI`, `GATEWAY_USERS_URI`, `GATEWAY_ORDERS_URI` (opcionales, defaults internos)

## Ejecución local
```bash
./mvnw spring-boot:run
```

## Docker
```bash
docker build -t gateway-service:dev .
docker run --rm -p 8080:8080 gateway-service:dev
```
