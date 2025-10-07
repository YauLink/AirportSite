#!/bin/bash
set -e
echo "Restoring database from dump..."
# Drop DB before restore to avoid duplicates (optional)
psql -U "$POSTGRES_USER" -c "DROP DATABASE IF EXISTS $POSTGRES_DB;"
psql -U "$POSTGRES_USER" -c "CREATE DATABASE $POSTGRES_DB;"
pg_restore -U "$POSTGRES_USER" -d "$POSTGRES_DB" /docker-entrypoint-initdb.d/demo.dump
echo "Database restored."