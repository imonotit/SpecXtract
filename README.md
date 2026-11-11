# SpecXtract

**Automatic REST API Documentation Extractor**

SpecXtract is a static analysis tool that reads the source code of a Java Spring Boot
backend, without running it, and will automatically produce an OpenAPI description of its
REST API: endpoints, parameters and responses.

SPL1 project, Institute of Information Technology (IIT), University of Dhaka.
Student: Abdul Zohor Imon. Supervisor: Toukir Ahammed.

## Pipeline

```
Java source files -> Lexer -> Parser -> Analyzer -> Writer -> openapi.json
```

| Stage | What it does | Status |
|---|---|---|
| Lexer | Splits Java source text into tokens, skips comments | Done |
| Parser | Builds class, method, field and annotation objects | Planned |
| Analyzer | Finds endpoints, parameters and responses from Spring annotations | Planned |
| Writer | Writes the result as an OpenAPI JSON file | Planned |

## Example

Input:

```java
@PostMapping("/login")
```

Lexer output:

```
AT  IDENT(PostMapping)  LPAREN  STRING(/login)  RPAREN
```

## Build and run

Requires JDK 17 or newer.

```bash
javac -d out $(find src -name "*.java")
java -cp out specxtract.Main samples
```

You can also pass a single file: `java -cp out specxtract.Main samples/AuthController.java`
