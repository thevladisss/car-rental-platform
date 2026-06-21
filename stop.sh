#!/usr/bin/env bash
set -euo pipefail

PORT="${PORT:-8080}"

PIDS=$(lsof -ti ":$PORT" 2>/dev/null || true)

if [[ -z "$PIDS" ]]; then
  echo "No process listening on port $PORT."
  exit 0
fi

echo "Stopping process(es) on port $PORT: $PIDS"
kill $PIDS 2>/dev/null || true

sleep 2
PIDS=$(lsof -ti ":$PORT" 2>/dev/null || true)
if [[ -n "$PIDS" ]]; then
  echo "Force stopping: $PIDS"
  kill -9 $PIDS 2>/dev/null || true
fi

echo "Stopped."
