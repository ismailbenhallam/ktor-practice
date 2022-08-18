package ismailbenhallam.org.plugins

import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import ismailbenhallam.org.models.Person
import ismailbenhallam.org.requests.PersonRequest
import ismailbenhallam.org.responses.ResponseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoutingKtTest {

    @Test
    fun `POST Person`() = testApplication {
        prepare()

        val person = PersonRequest("FirstName", "LASTNAME")
        postPerson(person).apply {
            assertEquals(HttpStatusCode.Created, status)
            body/*<ResponseEntity>*/<String>()
                .let {
                    Json.decodeFromString<ResponseEntity>(it)
                }
                .let {
                    assertEquals("Person saved", it.message)
                    assertEquals(HttpStatusCode.Created.value, it.statusCode)
                }
        }
    }

    @Test
    fun `GET all persons`() = testApplication {
        prepare()

        client.get("/api/persons") {
            basicAuth("admin", "admin")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body/*<ResponseEntity>*/<String>()
                .let {
                    Json.decodeFromString<Array<Person>>(it)
                }
                .let {
                    assertTrue { it.isEmpty() }
                }
        }

        val person = PersonRequest("FirstName", "LASTNAME")
        postPerson(person)
        client.get("/api/persons") {
            basicAuth("admin", "admin")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body/*<ResponseEntity>*/<String>()
                .let {
                    Json.decodeFromString<Array<Person>>(it)
                }
                .let {
                    assertTrue { it.size == 1 }
                    assertTrue { it[0].lastName == person.lastName }
                    assertTrue { it[0].firstName == person.firstName }
                }
        }
    }

    private suspend fun ApplicationTestBuilder.postPerson(person: PersonRequest) =
        client.post("/api/persons") {
            basicAuth("admin", "admin")
            contentType(ContentType.Application.Json)
            //            setBody(person)
            setBody(Json.encodeToString(person))
        }

}
