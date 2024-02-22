# Spring boot starter template

This starter template is configured with Java 21, Gradle 8.5, and uses Kotlin.

## Installation

Make sure to have the correct JDK and gradle versions, as well as postgresql instance running.

## Usage
- To start the project locally, run

```bash
# this does a few things:
# Generates the flyway database migrations (right now there is one)
# Generates the JOOQ classes
# Generates OpenAPI 3 docs
make build/local && make run/local
```

- Start the application in your IDE (I use IntelliJ IDEA)
- Go to `localhost:4001/greeting` to see the response
- Go to `localhost:4001/swagger-ui.html` to see the API document in Swagger UI

## Notes
- You must use the `make` command to run db migrations as flyway will not run on application startup
- There is a compose.yml to run postgresql locally, you can start it by `make start/db`
## License

[MIT](https://choosealicense.com/licenses/mit/)
