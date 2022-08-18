package ismailbenhallam.org.plugins

import io.ktor.server.testing.ApplicationTestBuilder

internal fun ApplicationTestBuilder.prepare() {
    application {
        configureMonitoring()
        configureSerialization()
        configureSecurity()
        configureRequestValidation()
        configureRouting()
        configureShutdownUrl()
        configureExceptionsHandler()
    }
}