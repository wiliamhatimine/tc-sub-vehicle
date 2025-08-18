# Vehicle API (Fase 3 — Tech Challenge)
Requisitos atendidos: 
- CRUD de veículos; listagem **à venda** e **vendidos** ordenada por preço (asc).
- Compra end-to-end (valida comprador existente).
- Auth **separado** via `auth-service` (JWT HS256). 
- OpenAPI/Swagger.
- Dockerfile + docker-compose.

## Rodando local
```bash
cd vehicle-api
./mvnw spring-boot:run
# Swagger: http://localhost:8080/swagger-ui/index.html
```

## Docker Compose (com auth-service)
```bash
docker compose up --build
```

## Fluxo de teste
1. Criar comprador:
```bash
curl -X POST http://localhost:8080/api/buyers -H "Content-Type: application/json" -d '{"name":"Ana","email":"ana@x.com","externalUserId":"user-123"}'
```
2. Criar veículo:
```bash
curl -X POST http://localhost:8080/api/vehicles -H "Authorization: Bearer <TOKEN>" -H "Content-Type: application/json" -d '{"brand":"VW","model":"Gol","year":2020,"color":"preto","price":45000}'
```
3. Listar à venda ordenado por preço:
```bash
curl http://localhost:8080/api/vehicles/available
```
4. Comprar:
```bash
curl -X POST http://localhost:8080/api/purchases -H "Authorization: Bearer <TOKEN>" -H "Content-Type: application/json" -d '{"vehicleId":1,"buyerId":1}'
```
5. Listar vendidos (ordenado):
```bash
curl http://localhost:8080/api/vehicles/sold
```

> Para gerar `<TOKEN>`, use o `auth-service` abaixo.
