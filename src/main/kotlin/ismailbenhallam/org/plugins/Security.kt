package ismailbenhallam.org.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.basic

private val ADMIN = UserPasswordCredential("admin", "admin")

fun Application.configureSecurity() {
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access Person admin API"
            validate { credential ->
                if (credential == ADMIN)
                    UserIdPrincipal("admin")
                else null
            }
        }
    }
}
