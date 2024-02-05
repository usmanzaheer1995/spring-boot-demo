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

## jooq/generate: generate jooq classes
.PHONY: jooq/generate
jooq/generate:
	@./gradlew generateJooq

## openapi/generate: generate openapi classes
.PHONY: openapi/generate
openapi/generate:
	@./gradlew openApiGenerate

## build/dev-deps: build development dependencies i.e. flyway, jooq, openapi
.PHONY: build/dev-deps
build/dev-deps: jooq/generate openapi/generate
