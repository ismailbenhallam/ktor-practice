package ismailbenhallam.org

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ismailbenhallam.org.plugins.configureExceptionsHandler
import ismailbenhallam.org.plugins.configureMonitoring
import ismailbenhallam.org.plugins.configureRouting
import ismailbenhallam.org.plugins.configureSecurity
import ismailbenhallam.org.plugins.configureSerialization
import ismailbenhallam.org.plugins.configureShutdownUrl

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureMonitoring()
        configureSerialization()
        configureSecurity()
        configureRouting()
        configureShutdownUrl()
        configureExceptionsHandler()
    }.start(wait = true)
}

