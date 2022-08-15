package ismailbenhallam.org.plugins

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import ismailbenhallam.org.models.Person
import ismailbenhallam.org.responses.ResponseEntity
import ismailbenhallam.org.services.PersonService

fun Application.configureRouting() {
    install(IgnoreTrailingSlash)
    routing {
        personRouting()
    }
}

private fun Routing.personRouting() {
    val service = PersonService()

    route("/persons") {
        authenticate("auth-basic") {
            get {
                call.respond(service.getAll())
            }

            get("{id}") {
                val id =
                    call.parameters["id"]!!
                try {
                    val person =
                        service.get(id) ?: return@get call.respondError(
                            "No person with id '$id'",
                            HttpStatusCode.NotFound
                        )
                    call.respond(person)
                } catch (exception: Exception) {
                    call.respondError("Incorrect id format", HttpStatusCode.BadRequest)
                }
            }

            post {
                val person = call.receive(Person::class)
                service.add(person)
                call.respondSuccessfully(
                    "Person saved",
                    HttpStatusCode.Created,
                    mapOf(HttpHeaders.Location to "${call.request.path()}/${person.id}")
                )
            }

            delete("{id}") {
                val id =
                    call.parameters["id"]!!
                service.remove(id) ?: return@delete call.respondError(
                    "No Person found with id $id",
                    HttpStatusCode.NotFound
                )
                call.respondSuccessfully("Person deleted", HttpStatusCode.Accepted)
            }
        }
    }
}

private suspend fun ApplicationCall.respondSuccessfully(
    message: String,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Map<String, String>? = null
) {
    headers?.forEach { (header, value) ->
        response.headers.append(header, value)
    }
    respond(status, ResponseEntity(message, status.value))
}

private suspend fun ApplicationCall.respondError(message: String, status: HttpStatusCode) {
    respond(status, ResponseEntity(message, status.value))
}

