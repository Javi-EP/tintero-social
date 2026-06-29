#!/bin/bash
set -e

echo "========================================"
echo "  Tintero Social - Starting All Services"
echo "========================================"

java -jar /app/user-service.jar &
echo "[OK] user-service :8081"

java -jar /app/book-service.jar &
echo "[OK] book-service :8082"

java -jar /app/review-service.jar &
echo "[OK] review-service :8083"

java -jar /app/reading-list-service.jar &
echo "[OK] reading-list-service :8084"

java -jar /app/social-service.jar &
echo "[OK] social-service :8085"

java -jar /app/recommendation-service.jar &
echo "[OK] recommendation-service :8086"

java -jar /app/notification-service.jar &
echo "[OK] notification-service :8087"

java -jar /app/search-service.jar &
echo "[OK] search-service :8088"

java -jar /app/stats-service.jar &
echo "[OK] stats-service :8089"

java -jar /app/audit-service.jar &
echo "[OK] audit-service :8090"

java -jar /app/gateway-service.jar &
echo "[OK] gateway-service :8080"

echo "========================================"
echo "  Gateway ready at http://localhost:8080"
echo "========================================"

wait
