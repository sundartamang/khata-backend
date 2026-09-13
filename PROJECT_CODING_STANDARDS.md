# Project Coding Standards

## Core Principles

This project is a small-to-mid-sized Spring Boot application. Prefer code that is clear, correct, easy to test, and easy to change.

- Prefer the simplest design that keeps responsibilities clear.
- Follow the existing architecture and naming conventions.
- Do not add layers, interfaces, factories, or utilities without a real benefit.
- Fix the root cause instead of hiding symptoms.
- Choose correctness and readability before optimization.

## 1. Design for Practical Scalability

Before implementing a feature, consider whether it will be easy to extend without creating unnecessary duplication or coupling. Do not design for hypothetical large-scale problems before the product needs that complexity.

## 2. One Method, One Responsibility

Each method should perform one clear task and should be explainable in one sentence. Extract private methods when a method becomes difficult to read, but do not extract trivial code only to reduce line count.

## 3. Use Descriptive Names

Names should communicate intent.

Prefer:

```java
findUserById()
validateUserRequest()
calculateTotalAmount()
```

Avoid vague names such as `process()`, `getData()`, `check()`, and `doSomething()`.

## 4. Use Descriptive Class Names

Classes should represent a clear responsibility. Prefer names such as `UserService`, `UserController`, `UserMapper`, and `ResourceNotFoundException`.

Avoid generic names such as `Helper`, `Utils`, `Manager`, and `Processor` unless the responsibility is genuinely justified.

## 5. Do Not Repeat Code

Reuse existing logic when the responsibilities match. Extract shared behavior only when the abstraction makes the code clearer. Do not blindly extract every repeated line.

## 6. Keep Related Methods Together

Keep public methods for the same feature together, followed by the private methods that support them. This makes classes easier to navigate.

## 7. Keep Private Methods at the Bottom

Keep the public or protected API above private implementation details unless placing a tightly coupled helper nearby clearly improves readability.

## 8. Follow the Existing Architecture

Use the established request flow:

```text
Controller -> Facade -> Service -> Repository
```

- Controllers handle HTTP concerns and request binding.
- Facades coordinate API operations and build standard response envelopes.
- Services contain business rules and transaction boundaries.
- Repositories contain persistence and query logic.
- Mappers convert between entities and DTOs.

A facade is optional for internal operations, but API-facing operations should follow the existing project pattern.

Keep feature code inside its feature package, using the established subpackages such as `controller`, `facade`, `service`, `repository`, `mapper`, and `model`.

## 9. Keep Business Logic in Services

Business rules, state changes, duplicate checks, authorization decisions, and resource lookups belong in the service layer. Controllers, facades, and repositories should not make business decisions.

## 10. Use Constants Carefully

Use constants for values that are meaningful or shared. Do not create constants merely to rename simple, self-explanatory literals.

## 11. Keep Methods Readable

Prefer clear, named steps over dense or clever code. A method should be understandable without mentally executing every line.

## 12. Naming Conventions

Follow standard Java naming conventions:

```text
Class        -> PascalCase
Method       -> camelCase
Variable     -> camelCase
Constant     -> UPPER_SNAKE_CASE
Package      -> lowercase
Enum         -> PascalCase
Enum value   -> UPPER_SNAKE_CASE
```

## 13. API Response Consistency

API endpoints should use the project response format consistently.

- Use the correct HTTP status for the operation.
- Return a stable response shape and meaningful message.
- Do not expose persistence entities directly from controllers.
- Keep DTOs aligned with the API contract.
- Handle errors through the global exception handler.
- Use `BaseResponse<T>` for successful responses and handled application errors, following the existing project convention.
- Use `ResponseBuilder` when a `ResponseEntity` must be constructed from a response object.

## 14. DTOs, IDs, and Pagination

- Use DTOs for API input and output. Do not bind request bodies directly to JPA entities.
- Use `UUID` for resource identifiers where the entity uses UUID identifiers.
- Use `Pageable` for pageable list endpoints and return `Page<T>` when pagination is part of the endpoint contract.
- Validate null IDs and null request payloads before calling the service when the facade owns that API validation.

## 15. Validation and Exceptions

Use Bean Validation such as `@NotBlank`, `@NotNull`, and `@Positive` for request-shape validation. Keep cross-field, uniqueness, authorization, and state-transition rules in services.

Use meaningful domain exceptions such as `ResourceNotFoundException`, `DuplicateResourceException`, and `InvalidDataException`. Handle predictable business failures consistently through the global exception handler.

## 16. Database and Transactions

Use appropriate types, nullability constraints, relationships, and database indexes.

- Rules that must be unique must have a database unique constraint.
- Service-level duplicate checks may provide better error messages but cannot prevent race conditions alone.
- Use `@Transactional` at the service boundary for business operations involving persistence.
- Use `@Transactional(readOnly = true)` for read-only queries.
- Keep audit fields in a shared base entity only when they genuinely apply consistently.

Entities that require common audit data should extend `BaseEntity` and use the existing JPA auditing configuration. Do not set `createdBy`, `updatedBy`, `createdAt`, or `updatedAt` manually unless there is a documented exception.

## 17. Testing

Add focused tests for important behavior.

- Test service business rules and exception cases.
- Test API validation, response status, and response structure when relevant.
- Add a regression test for reproducible bug fixes.
- Do not test trivial getters, setters, or framework wiring without project-specific behavior.

## 18. Security

- Never store or log plaintext passwords.
- Never log JWTs, secrets, or authorization headers.
- Do not trust client-supplied ownership, user, tenant, or audit identifiers.
- Keep public endpoints explicit and review every newly added public endpoint.
- Keep the API stateless and use the existing JWT security flow for authenticated requests.
- Do not bypass the configured security filter chain for convenience.

## 19. Logging

Log meaningful application events and troubleshooting information. Avoid logs that only announce method entry or exit. Never log passwords, tokens, secrets, private credentials, or full authentication headers.
