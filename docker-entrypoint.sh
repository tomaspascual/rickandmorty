#!/bin/sh

fERROR(){
 echo "[ERROR]: $*"
 exit 1
}
set -e

exec "$@"

echo lets print java version

java --version

echo lets print all environment variables

set

echo now lets execute the application

java -jar /app.jar --server.port=8080 -Dhttps.protocols=TLSv1.2,TLSv1.1,TLSv1