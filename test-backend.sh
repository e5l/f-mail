#!/bin/bash
echo "Starting backend server..."
./gradlew :backend:run &
SERVER_PID=$!

echo "Waiting for server to start..."
sleep 5

echo "Testing API endpoints..."
echo "1. Root endpoint:"
curl -s http://localhost:8080/ || echo "Failed"

echo -e "\n2. Emails endpoint:"
curl -s http://localhost:8080/api/emails || echo "Failed"

echo -e "\nKilling server..."
kill $SERVER_PID