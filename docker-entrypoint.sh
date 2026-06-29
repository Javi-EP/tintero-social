#!/bin/bash
set -e

echo "========================================"
echo "  Tintero Social - Starting Services"
echo "========================================"

JAVA_OPTS="-Xmx48m -Xms24m -XX:+UseSerialGC -XX:+ExitOnOutOfMemoryError"

java $JAVA_OPTS -jar /app/user-service.jar &
echo "[OK] user-service :8081"

java $JAVA_OPTS -jar /app/book-service.jar &
echo "[OK] book-service :8082"

java $JAVA_OPTS -jar /app/review-service.jar &
echo "[OK] review-service :8083"

java $JAVA_OPTS -jar /app/reading-list-service.jar &
echo "[OK] reading-list-service :8084"

java $JAVA_OPTS -jar /app/social-service.jar &
echo "[OK] social-service :8085"

java $JAVA_OPTS -jar /app/gateway-service.jar &
echo "[OK] gateway-service :8080"

echo "========================================"
echo "  Gateway ready at http://localhost:8080"
echo "========================================"

wait
