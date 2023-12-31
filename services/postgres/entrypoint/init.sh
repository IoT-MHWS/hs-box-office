#!/usr/bin/env bash

set -e

psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "postgres" <<-EOSQL
	CREATE DATABASE $POSTGRES_DATABASE;
  CREATE USER $HS_POSTGRES_USERNAME WITH PASSWORD '$HS_POSTGRES_PASSWORD';
  GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DATABASE TO $HS_POSTGRES_USERNAME;
EOSQL

psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "$POSTGRES_DATABASE" <<-EOSQL
    GRANT USAGE, CREATE ON SCHEMA public TO $HS_POSTGRES_USERNAME;
EOSQL