## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' | sed -e 's/^/ /'

.PHONY: confirm
confirm:
	@echo 'Are you sure? [y/N] ' && read ans && [ $${ans:-N} = y ]

## start/db: start local database
.PHONY: start/db
start/db:
	@docker-compose up -d

## stop/db: stop local database
.PHONY: stop/db
stop/db:
	@docker-compose down

## generate/jooq: generate jooq classes
.PHONY: generate/jooq
generate/jooq:
	@./gradlew generateJooq

## generate/openapi: generate openapi classes
.PHONY: generate/openapi
generate/openapi:
	@./gradlew openApiGenerate

## generate/flywayMigrations: generate flyway migrations
.PHONY: generate/flywayMigrations
generate/flywayMigrations:
	@./gradlew flywayMigrate

## build/deps: build development dependencies i.e. jooq, openapi
.PHONY: build/deps
build/deps: generate/jooq generate/openapi

## build/local: build for local environment
.PHONE: build/local
build/local: |
	echo "Cleaning..."
	@./gradlew clean && \
	echo "Setting environment variable to local..." && \
	export DEMO_ENV=local && \
	echo "Generating resources..." && \
	make generate/jooq && \
	make generate/openapi

## build/prod: build for prod environment
.PHONE: build/prod
build/prod: |
	echo "Cleaning..."
	@./gradlew clean && \
	echo "Setting environment variable to prod..." && \
	export DEMO_ENV=prod && \
	echo "Generating resources..." && \
	make generate/jooq && \
	make generate/openapi

## build/image/local: build docker image for local environment
.PHONE: build/image/local
build/image/local: build/local
	@docker build -t spring-boot-demo-local -f Dockerfile-local .

## build/image/prod: build docker image for local environment
.PHONE: build/image/prod
build/image/prod: build/prod
	@docker build -t spring-boot-demo-prod -f Dockerfile-prod .


CONTAINER_NAME = sp-demo-prod
## run/image/prod: run docker image for prod environment
.PHONE: run/image/prod
run/image/prod:
	@if [ ! "$(docker ps -a | grep ${CONTAINER_NAME})" ]; then \
		echo "Stopping and removing existing container: $(CONTAINER_NAME)"; \
		docker stop $(CONTAINER_NAME); \
		docker rm $(CONTAINER_NAME); \
	fi
	docker run --network=postgres-bridge --restart=unless-stopped --name=$(CONTAINER_NAME) -p 4002:4002 spring-boot-demo-prod



ifeq ($(OS),Windows_NT)
    # Commands to run on Windows
    delay = timeout 2
else
    # Commands to run on Unix/Linux/macOS
    delay = sleep 2
endif

.PHONY: delay
delay:
	@${delay}
