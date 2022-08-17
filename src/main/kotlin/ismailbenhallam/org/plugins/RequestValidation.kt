package ismailbenhallam.org.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import ismailbenhallam.org.requests.PersonRequest

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<PersonRequest> { personRequest ->
            when {
                personRequest.firstName.isNullOrEmpty() -> {
                    ValidationResult.Invalid("FirstName cannot be empty")
                }

                personRequest.lastName.isNullOrEmpty() -> {
                    ValidationResult.Invalid("LastName cannot be empty")
                }

                else -> ValidationResult.Valid
            }
        }
    }
}