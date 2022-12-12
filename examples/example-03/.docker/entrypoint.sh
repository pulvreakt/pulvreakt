#!/usr/bin/env bash

if [[ $# -lt 1 ]]
then
  echo "No executable name found!"
  exit 255
fi

until nc -z "${RABBITMQ_HOST:-rabbitmq}" "${RABBITMQ_PORT:-5672}"; do
  echo "$(date) - waiting for rabbitmq..."
  sleep 5
done

java -jar ./"$1"
