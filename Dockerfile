FROM mcr.microsoft.com/windows/servercore:ltsc2019

COPY mariadb-10.6.4-winx64 /mariadb

EXPOSE 3306

WORKDIR c:/mariadb

CMD ["c:/mariadb/bin/mysqld", "--console"]