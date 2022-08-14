package ismailbenhallam.org

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ismailbenhallam.org.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureMonitoring()
        configureTemplating()
        configureSerialization()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
