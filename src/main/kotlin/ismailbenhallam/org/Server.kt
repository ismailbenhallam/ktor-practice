package ismailbenhallam.org

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ismailbenhallam.org.plugins.configureMonitoring
import ismailbenhallam.org.plugins.configureRouting
import ismailbenhallam.org.plugins.configureSecurity
import ismailbenhallam.org.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureMonitoring()
        configureSerialization()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}

