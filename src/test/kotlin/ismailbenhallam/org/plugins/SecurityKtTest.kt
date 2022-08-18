package ismailbenhallam.org.plugins

import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class SecurityKtTest {

    @Test
    fun `should get UNAUTHORIZED when trying to access the API without login`() = testApplication {
        prepare()

        client.get("/api/persons").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `should get OK when trying to access the API with valid basic auth`() = testApplication {
        prepare()

        client.get("/api/persons") {
            basicAuth("admin", "admin")
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
