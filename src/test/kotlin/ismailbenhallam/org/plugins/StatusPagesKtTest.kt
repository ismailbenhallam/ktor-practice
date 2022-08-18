package ismailbenhallam.org.plugins

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import ismailbenhallam.org.responses.ResponseEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class StatusPagesKtTest {

    @Test
    fun `Not found`() = testApplication {
        prepare()

        client.get("/not-found") {
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            body<String>().let {
                Json.decodeFromString<ResponseEntity>(it)
            }.let {
                assertEquals("Resource not found", it.message)
                assertEquals(HttpStatusCode.NotFound.value, it.statusCode)
            }
        }
    }

}
