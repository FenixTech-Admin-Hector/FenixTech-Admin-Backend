@echo off
color 07

echo [!] Deteniendo contenedores anteriores...
docker-compose down

echo [!] Destruyendo la base de datos antigua para empezar de cero...
if exist data_mariadb (
    rmdir /s /q data_mariadb
    echo [-] Memoria de MariaDB borrada con exito.
)

echo [>] Compilando el codigo de Spring Boot...
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    color 4F
    echo.
    echo [ERROR FATAL] Maven no pudo compilar el codigo. Revisa el error arriba.
    pause
    exit /b %ERRORLEVEL%
)

echo [+] Levantando la infraestructura con Docker (sin cache)...
docker-compose build --no-cache
if %ERRORLEVEL% neq 0 (
    color 4F
    echo.
    echo [ERROR FATAL] Docker no pudo construir la imagen del Dockerfile.
    pause
    exit /b %ERRORLEVEL%
)

docker-compose up -d --force-recreate
echo [OK] Todo listo! Mostrando los logs de Spring Boot...
docker-compose logs -f app-spring