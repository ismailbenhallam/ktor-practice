package ismailbenhallam.org.plugins

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ismailbenhallam.org.requests.PersonRequest
import ismailbenhallam.org.responses.ResponseEntity
import ismailbenhallam.org.routing.Persons
import ismailbenhallam.org.services.PersonService
import java.io.File

fun Application.configureRouting() {
    install(IgnoreTrailingSlash)
    install(AutoHeadResponse)
    install(Resources)
    routing {
        authenticate("auth-basic") {
            route("/api") {
                personRouting()
            }
        }
        get("/") {
            val file = File(javaClass.classLoader.getResource("hello.png")!!.file)
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline
                    .withParameter(ContentDisposition.Parameters.FileName, "hello.png")
                    .withParameter(ContentDisposition.Parameters.Size, file.length().toString())
                    .toString()
            )
            call.respondFile(file)
        }
    }
}

private fun Route.personRouting() {
    val service = PersonService()

    get<Persons> {
        call.respond(service.getAll())
    }

    get<Persons.ById> {
        val id = it.id
        try {
            val person = service.get(id)!!  // caught bellow
            call.respond(person)
        } catch (exception: Exception) {
            throw NotFoundException("No person with id '$id'")
        }
    }

    post<Persons> {
        val personRequest = call.receive(PersonRequest::class)
        val person = personRequest.toPerson()
        service.add(person)
        call.respondSuccessfully(
            "Person saved",
            HttpStatusCode.Created,
            mapOf(HttpHeaders.Location to "${call.request.path()}/${person.id}")
        )
    }

    delete<Persons.ById> {
        service.remove(it.id) ?: throw NotFoundException("No Person found with id ${it.id}")
        call.respondSuccessfully("Person deleted", HttpStatusCode.Accepted)
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


