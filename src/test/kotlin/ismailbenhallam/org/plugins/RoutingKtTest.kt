package ismailbenhallam.org.plugins

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import ismailbenhallam.org.models.Person
import ismailbenhallam.org.responses.ResponseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingKtTest {

    @Test
    fun testPostPersons() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val person = Person("FirstName", "LASTNAME")
        client.post("/persons") {
            contentType(ContentType.Application.Json)
//            setBody(person)
            setBody(Json.encodeToString(person))
        }.apply {
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
}