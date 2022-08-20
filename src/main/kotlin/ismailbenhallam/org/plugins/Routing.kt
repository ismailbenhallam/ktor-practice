package ismailbenhallam.org.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.resources.Resources
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ismailbenhallam.org.plugins.custom.HeaderAppender
import ismailbenhallam.org.routing.api.personRouting
import ismailbenhallam.org.routing.serveFiles
import ismailbenhallam.org.routing.serverAssets

fun Application.configureRouting() {
    install(IgnoreTrailingSlash)
    install(AutoHeadResponse)
    install(Resources)
    routing {
        authenticate("auth-basic") {
            route("/api") {
                install(HeaderAppender) {
                    this.header("Best-Api", "This one :)")
                }
                personRouting()
            }
        }
        serveFiles()
        serverAssets()
    }
}



