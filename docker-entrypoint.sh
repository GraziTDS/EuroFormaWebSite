#!/bin/sh
set -e

# Algumas plataformas (ex.: Fly.io "postgres attach") fornecem a conexão como uma única
# DATABASE_URL (postgres://usuario:senha@host:porta/banco) em vez das variáveis DB_HOST/DB_PORT/
# DB_NAME/DB_USER/DB_PASSWORD separadas que o Render usa. Quando DATABASE_URL existir, decompomos
# ela aqui para preencher essas mesmas variáveis, sem precisar de configuração manual por plataforma.
if [ -n "$DATABASE_URL" ]; then
  sem_protocolo="${DATABASE_URL#*://}"
  userpass="${sem_protocolo%@*}"
  resto="${sem_protocolo##*@}"
  hostporta="${resto%%/*}"
  banco="${resto#*/}"

  export DB_USER="${userpass%%:*}"
  export DB_PASSWORD="${userpass#*:}"
  export DB_HOST="${hostporta%%:*}"
  export DB_PORT="${hostporta#*:}"
  export DB_NAME="${banco%%\?*}"
fi

exec java -jar /app/app.jar
