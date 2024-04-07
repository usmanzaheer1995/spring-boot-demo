# Spring boot starter template

This starter template is configured with Java 21, Gradle 8.5, and uses Kotlin.

## Installation

Make sure to have the correct JDK and gradle versions, as well as postgresql instance running.

## Usage
- To start the project locally, first run this:

```bash
# this does a few things:
# Generates the JOOQ classes
# Generates OpenAPI 3 docs
make build/local
```

- Start the application in your IDE with local profile (I use IntelliJ IDEA, and set the `Active Profile` to `local` in the run configuration)
- Go to `localhost:4001/greeting` to see the response
- Go to `localhost:4001/swagger-ui-local.html` to see the API document in Swagger UI

## Build docker image
To build the production docker image, run:
```bash
make build/image/prod
```

To run the production docker image,
you need a postgresql instance running in a docker container
with a network bridge named `postgres-bridge`.
```bash
make run/image/prod
```

## Notes
- There is a compose.yml to run postgresql locally, you can start it by `make start/db`

# Next Steps
- [ ] Add a CI/CD pipeline with Github Actions
- [ ] Figure out testing, both unit and integration tests (using testcontainers)
- [ ] Figure out how to test on the CI

## License
[MIT](https://choosealicense.com/licenses/mit/)
