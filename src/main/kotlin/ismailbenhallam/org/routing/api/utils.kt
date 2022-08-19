package ismailbenhallam.org.routing.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import ismailbenhallam.org.responses.ResponseEntity

internal suspend fun ApplicationCall.respondSuccessfully(
    message: String,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Map<String, String>? = null
) {
    headers?.forEach { (header, value) ->
        response.headers.append(header, value)
    }
    respond(status, ResponseEntity(message, status.value))
}