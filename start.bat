@echo off
chcp 65001 > nul
echo =======================================================
echo 🚀 INICIANDO FENIXTECH BACKEND (Spring Boot + MariaDB)
echo =======================================================
echo.

echo [1/5] 🧹 Deteniendo contenedores anteriores...
:: Añadimos -v por seguridad, para borrar redes huérfanas o volúmenes internos
docker-compose down -v

echo.
echo [2/5] 🗑️  Eliminando base de datos antigua (data_mariadb)...
IF EXIST "data_mariadb" (
    rmdir /s /q "data_mariadb"
    echo     [OK] Carpeta data_mariadb eliminada con exito.
) ELSE (
    echo     [INFO] La carpeta data_mariadb no existia.
)

echo.
echo [3/5] ☕ Compilando el codigo de Spring Boot (Java 21)...
call mvnw.cmd clean package -DskipTests

echo.
echo [4/5] 🐳 Levantando la infraestructura con Docker...
docker-compose up -d --build

echo.
echo [5/5] ✅ ¡Todo listo! 
echo.
echo Mostrando los logs de Spring Boot (Presiona Ctrl+C para salir)...
echo -------------------------------------------------------
docker-compose logs -f app-spring