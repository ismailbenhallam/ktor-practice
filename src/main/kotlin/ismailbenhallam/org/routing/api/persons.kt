package ismailbenhallam.org.routing.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import ismailbenhallam.org.requests.PersonRequest
import ismailbenhallam.org.services.PersonService
import kotlinx.serialization.Serializable

@Serializable
@Resource("/persons")
class Persons {

    @Serializable
    @Resource("/{id}")
    data class ById(val parent: Persons, val id: String)
}


fun Route.personRouting() {
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
