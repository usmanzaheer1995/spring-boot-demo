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

## run/local: run app for local environment
.PHONE: run/local
run/local:
	@java -Dspring.profiles.active=local -jar build/libs/spring-boot-demo.jar

## run/prod: run app for prod environment
.PHONE: run/prod
run/prod:
	@java -Dspring.profiles.active=prod -jar build/libs/spring-boot-demo.jar

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
