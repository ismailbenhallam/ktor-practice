package ismailbenhallam.org

import io.ktor.server.testing.testApplication
import ismailbenhallam.org.plugins.configureRouting
import ismailbenhallam.org.plugins.configureSerialization
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }
    }
}