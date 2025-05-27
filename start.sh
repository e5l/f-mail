#!/bin/bash

# Start backend and frontend for F-Mail application

echo "Starting F-Mail application..."

# Function to cleanup on exit
cleanup() {
    echo -e "\nShutting down services..."
    kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
    exit
}

# Set trap to cleanup on script exit
trap cleanup EXIT INT TERM

# Start backend
echo "Starting backend server..."
./gradlew :backend:run &
BACKEND_PID=$!

# Wait a bit for backend to start
sleep 5

# Start frontend
echo "Starting frontend..."
./gradlew :frontend:wasmJsBrowserRun &
FRONTEND_PID=$!

echo -e "\nF-Mail is running!"
echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo -e "\nPress Ctrl+C to stop all services"

# Wait for both processes
wait