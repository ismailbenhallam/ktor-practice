package ismailbenhallam.org.plugins

import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import ismailbenhallam.org.requests.PersonRequest
import ismailbenhallam.org.responses.ResponseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RoutingKtTest {

    @Test
    fun testPostPerson() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureSecurity()
        }

        val person = PersonRequest("FirstName", "LASTNAME")
        client.post("/api/persons") {
            basicAuth("admin", "admin")
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

    @Test
    fun testPostPerson_withNullFirstName() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureRequestValidation()
            configureSecurity()
            configureExceptionsHandler()
        }

        val person = PersonRequest(null, "name")
        client.post("/api/persons") {
            contentType(ContentType.Application.Json)
            basicAuth("admin", "admin")
            setBody(Json.encodeToString(person))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>()
                .let {
                    Json.decodeFromString<ResponseEntity>(it)
                }
                .let {
                    assertContains(
                        charSequence = it.message,
                        regex = Regex.fromLiteral("FirstName").apply { options.plus(RegexOption.IGNORE_CASE) })
                }
        }
    }

    @Test
    fun testPostPerson_withEmptyLastName() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureRequestValidation()
            configureSecurity()
            configureExceptionsHandler()
        }

        val person = PersonRequest("FirstName", "")
        client.post("/api/persons") {
            contentType(ContentType.Application.Json)
            basicAuth("admin", "admin")
            setBody(Json.encodeToString(person))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>()
                .let {
                    Json.decodeFromString<ResponseEntity>(it)
                }
                .let {
                    assertContains(
                        charSequence = it.message,
                        regex = Regex.fromLiteral("LastName").apply { options.plus(RegexOption.IGNORE_CASE) })
                }
        }
    }
}