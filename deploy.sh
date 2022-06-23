#!/usr/bin/env bash
set -ex

mvn clean package
aws ecr get-login-password | docker login --username AWS --password-stdin https://693416317310.dkr.ecr.ca-central-1.amazonaws.com
docker-compose build
docker-compose push
