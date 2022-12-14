package ismailbenhallam.org.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import ismailbenhallam.org.responses.ResponseEntity

fun Application.configureExceptionsHandler() {
    install(StatusPages) {
        exception<NotFoundException> { call, exception ->
            call.application.log.error(exception.message)
            call.sendResponseEntity(exception, HttpStatusCode.NotFound)
        }
        exception<BadRequestException> { call, exception ->
            call.application.log.error(exception.message)
            call.sendResponseEntity(exception, HttpStatusCode.BadRequest)
        }
        exception<RequestValidationException> { call, exception ->
            call.application.log.error(exception.message)
            call.application.log.error(exception.reasons.joinToString())
            call.sendResponseEntity(BadRequestException(exception.reasons.joinToString()), HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, exception ->
            call.application.log.info(exception.message)
            call.application.log.debug(exception.stackTrace.joinToString())
            call.sendResponseEntity(Throwable("Internal Server Error"), HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.application.log.error(status.toString())
            call.sendResponseEntity(NotFoundException("Resource not found"), HttpStatusCode.NotFound)
        }
    }
}

private suspend fun ApplicationCall.sendResponseEntity(exception: Throwable, status: HttpStatusCode) {
    respond(
        message = ResponseEntity(exception.message ?: status.description, status.value),
        status = status
    )
}