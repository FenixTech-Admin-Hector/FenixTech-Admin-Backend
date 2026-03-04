@echo off

echo [!] Deteniendo contenedores anteriores...
docker-compose down

echo [>] Compilando el codigo de Spring Boot (Java 21)...
:: Usamos call para que el script no se detenga al terminar maven
call mvnw.cmd clean package -DskipTests

echo [+] Levantando la infraestructura con Docker...
docker-compose up -d --build

echo [OK] Todo listo! Mostrando los logs de Spring Boot...
docker-compose logs -f app-spring