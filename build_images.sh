#!/bin/bash

# Find all directories containing a Dockerfile
services=$(find . -maxdepth 2 -name Dockerfile -exec dirname {} \; | sed 's|./||' | sort)

# Build Docker images for each service
for service in $services
do
    echo "--------------------------------------------------------"
    echo "Building Docker image for service: ${service}"
    echo "--------------------------------------------------------"
    docker build -f "${service}/Dockerfile" -t "local/${service}:0.1" .
done
