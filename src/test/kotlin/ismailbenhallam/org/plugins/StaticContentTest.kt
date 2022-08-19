package ismailbenhallam.org.plugins

import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val IMG1 = "img1.jpeg"
private const val DEFAULT_IMG = "hello.png"

class StaticContentTest {

    @Test
    fun `serving img1`() = testApplication {
        prepare()

        client.get("/assets/$IMG1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(headers[HttpHeaders.ContentType]!!, "image/jpeg")

            val length = File(javaClass.classLoader.getResource("assets/$IMG1")!!.file).length()
            assertTrue { headers.contains(HttpHeaders.ContentLength) }
            assertEquals(headers[HttpHeaders.ContentLength]!!.toLong(), length)
        }
    }

    @Test
    fun `default serving`() = testApplication {
        prepare()

        client.get("/assets").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(headers[HttpHeaders.ContentType]!!, "image/png")

            val length = File(javaClass.classLoader.getResource("assets/$DEFAULT_IMG")!!.file).length()
            assertTrue { headers.contains(HttpHeaders.ContentLength) }
            assertEquals(headers[HttpHeaders.ContentLength]!!.toLong(), length)
        }
    }

}
