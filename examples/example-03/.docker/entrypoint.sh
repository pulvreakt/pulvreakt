#!/usr/bin/env bash

if (($# < 1)) then
  echo "No executable name found!"
  return 255
fi

until nc -z "${RABBITMQ_HOST:-rabbitmq}" "${RABBITMQ_PORT:-5672}"; do
  echo "$(date) - waiting for rabbitmq..."
  sleep 5
done

java -jar ./"$1"
