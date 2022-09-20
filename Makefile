VERSION=0.5

BUILD := `git rev-parse HEAD`

# Operating System Default (LINUX)
TARGETOS=linux

LDFLAGS=-ldflags "-s -w -X=main.Version=$(VERSION) -X=main.Build=$(BUILD) -extldflags -static"

build_db:
    docker container run --name=ptithcmoj -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=online_judge -d mysql:8

build-oj-service:
	bash run_judgelite.sh
DOCKER_COMPOSE ?= cd devstack && docker compose -p $(PROJECT_NAME)
