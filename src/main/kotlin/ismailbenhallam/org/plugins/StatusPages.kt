package ismailbenhallam.org.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import ismailbenhallam.org.responses.ResponseEntity

fun Application.configureExceptionsHandler() {
    install(StatusPages) {
        exception<NotFoundException> { call, exception ->
            call.application.environment.log.error(exception.message)
            call.sendResponseEntity(exception, HttpStatusCode.NotFound)
        }
        exception<BadRequestException> { call, exception ->
            call.application.environment.log.error(exception.message)
            call.sendResponseEntity(exception, HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, exception ->
            call.application.environment.log.info(exception.message)
            call.application.environment.log.debug(exception.stackTrace.joinToString())
            call.respond(
                message = ResponseEntity("Internal server error", HttpStatusCode.InternalServerError.value),
                status = HttpStatusCode.InternalServerError
            )
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.application.environment.log.error(status.toString())
            call.sendResponseEntity(NotFoundException("Resource not found"), HttpStatusCode.NotFound)
        }
    }
}

private suspend fun ApplicationCall.sendResponseEntity(exception: Exception, status: HttpStatusCode) {
    respond(
        message = ResponseEntity(exception.message ?: status.description, status.value),
        status = status
    )
}