package ismailbenhallam.org

import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ismailbenhallam.org.plugins.configureExceptionsHandler
import ismailbenhallam.org.plugins.configureMonitoring
import ismailbenhallam.org.plugins.configureRequestValidation
import ismailbenhallam.org.plugins.configureRouting
import ismailbenhallam.org.plugins.configureSecurity
import ismailbenhallam.org.plugins.configureSerialization
import ismailbenhallam.org.plugins.configureShutdownUrl
import ismailbenhallam.org.plugins.custom.GoodByeLogger

private const val DEFAULT_PORT = 8080

fun main() {
    embeddedServer(
        Netty, host = "0.0.0.0",
        port = System.getProperty("port")?.toInt() ?: DEFAULT_PORT,
    ) {
        configureMonitoring()
        configureSerialization()
        configureSecurity()
        configureRequestValidation()
        configureRouting()
        configureShutdownUrl()
        configureExceptionsHandler()
        install(GoodByeLogger) {
            this.message = "See u later ;)"
        }
    }.start(wait = true)
}

