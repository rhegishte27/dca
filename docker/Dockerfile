FROM mcr.microsoft.com/mssql/server:2019-latest

ARG SA_PASSWORD
ENV SA_PASSWORD=Test@12345
ARG ACCEPT_EULA
ENV ACCEPT_EULA=Y

# Copy SQL scripts
COPY ./create-database.sql ./tmp/
COPY ./create-table.sql ./tmp/
COPY ./insert-data.sql ./tmp/

# Run database build scripts
# Sleep for 30 seconds to make sure SQL has spun up.
RUN /opt/mssql/bin/sqlservr &\
    sleep 30s &&\
    /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -i ./tmp/create-database.sql &&\
    /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -i ./tmp/create-table.sql &&\
    /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -i ./tmp/insert-data.sql

CMD /opt/mssql/bin/sqlservr

#### Docker commands
# cd data-conversion-architect\docker\database
# docker build --rm -t dcadb:latest .
# docker run -e 'ACCEPT_EULA=Y' -e 'SA_Password=Test@12345' -p 1433:1433 --name=sqlserver -d dcadb:latest
#
# docker image ls - list images
# docker container ls - list containers
# docker container stop <container-id> - stop running container

### SQL Server commands
# sqlcmd -S localhost,1433 -U sa -P "Test@12345"
