REM Copy SQL files
copy ..\database\sqlserver\create-database.sql
copy ..\database\sqlserver\create-table.sql
copy ..\database\sqlserver\insert-data.sql

REM Delete Docker container and image if created
FOR /f "tokens=*" %%i IN ('docker ps -aq') DO docker stop %%i && docker rm %%i
docker image rm -f dcadb:latest

REM Build new Docker image and container
docker build --rm -t dcadb:latest .
docker run -e 'ACCEPT_EULA=Y' -e 'SA_Password=Test@12345' -p 1433:1433 --name=sqlserver -d dcadb:latest

REM Delete SQL files
del create-database.sql
del create-table.sql
del insert-data.sql
