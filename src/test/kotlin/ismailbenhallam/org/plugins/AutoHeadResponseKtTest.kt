package ismailbenhallam.org.plugins

import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AutoHeadResponseKtTest {

    @Test
    fun `should receive response with body when sending GET to get the logo `() = testApplication {
        prepare()

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue { headers.contains(HttpHeaders.ContentDisposition) }

            val size = headers[HttpHeaders.ContentLength]?.toInt()!!
            val bodyAsChannel = bodyAsChannel()
            bodyAsChannel.readFully(ByteBuffer.allocate(size))

            assertTrue { bodyAsChannel.totalBytesRead.toInt() == size }
            assertTrue { size == headers[HttpHeaders.ContentLength]?.toInt()!! }
        }
    }

    @Test
    fun `should receive response without body when sending HEAD to get the logo `() = testApplication {
        prepare()

        client.head("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue { headers.contains(HttpHeaders.ContentDisposition) }
            assertTrue { bodyAsText().isEmpty() }
        }
    }

}
