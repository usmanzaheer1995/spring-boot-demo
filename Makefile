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
