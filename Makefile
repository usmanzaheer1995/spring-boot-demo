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

## generate/resources: generate all resources i.e. flyway, jooq, openapi
.PHONE: generate/resources
generate/resources: generate/flywayMigrations generate/jooq generate/openapi

## build/dev-deps: build development dependencies i.e. flyway, jooq, openapi
.PHONY: build/dev-deps
build/dev-deps: start/db delay generate/flywayMigrations generate/jooq generate/openapi

## build/local: build for local environment
.PHONE: build/local
build/local: |
	echo "Cleaning..."
	@./gradlew clean && \
	echo "Setting environment variable to local..." && \
	export DEMO_ENV=local && \
	echo "Generating resources..." && \
	make generate/resources && \
	echo "Building..." && \
	./gradlew build

## build/prod: build for prod environment
.PHONE: build/prod
build/prod: |
	echo "Cleaning..."
	@./gradlew clean && \
	echo "Setting environment variable to prod..." && \
	export DEMO_ENV=prod && \
	echo "Generating resources..." && \
	make generate/resources && \
	echo "Building..." && \
	./gradlew build --no-daemon

## build/image/local: build docker image for local environment
.PHONE: build/image/local
build/image/local: build/image/local
	@docker build -t spring-boot-demo-local -f Dockerfile-local .

## build/image/prod: build docker image for local environment
.PHONE: build/image/prod
build/image/prod: build/image/prod
	@docker build -t spring-boot-demo-prod -f Dockerfile-prod .

## run/image/prod: run docker image for prod environment
.PHONE: run/image/prod
run/image/prod: run/image/prod
	@docker run --network postgres-bridge --name sp-demo-prod -p 4002:4002 spring-boot-demo-prod

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
