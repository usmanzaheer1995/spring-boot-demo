# Spring boot starter template

This starter template is configured with Java 21, Gradle 8.5, and uses Kotlin.

## Installation

Make sure to have the correct JDK and gradle versions, as well as docker-compose installed.

## Usage
- To start the project, run

```bash
# this does a few things:
# Starts the PostgreSQL db in a docker container
# Generates the flyway database migrations (right now there is one)
# Generates the JOOQ classes
# Generates OpenAPI 3 docs
make build/dev-deps
```

- Start the application in your IDE (I use IntelliJ IDEA)
- Go to `localhost:4001/greeting` to see the response
- Go to `localhost:4001/swagger-ui.html` to see the API document in Swagger UI

## Notes
- You must use the make command to run db migrations as flyway will not run on application startup

## License

[MIT](https://choosealicense.com/licenses/mit/)
