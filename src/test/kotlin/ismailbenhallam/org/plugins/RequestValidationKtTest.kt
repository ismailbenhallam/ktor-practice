package ismailbenhallam.org.plugins

import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import ismailbenhallam.org.requests.PersonRequest
import ismailbenhallam.org.responses.ResponseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestValidationKtTest {

    @Test
    fun `POST Person with null firstName`() = testApplication {
        prepare()

        val person = PersonRequest(null, "name")
        postPerson(person).apply {
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
    fun `POST Person with empty lastName`() = testApplication {
        prepare()

        val person = PersonRequest("FirstName", "")
        postPerson(person).apply {
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

    @Test
    fun `POST Person Ok`() = testApplication {
        prepare()

        val person = PersonRequest("first", "name")
        postPerson(person).apply {
            assertEquals(HttpStatusCode.Created, status)
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
