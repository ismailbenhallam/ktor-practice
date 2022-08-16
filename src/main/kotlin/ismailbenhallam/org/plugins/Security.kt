package ismailbenhallam.org.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.util.getDigestFunction

private val DIGEST_FUNCTION = getDigestFunction("SHA-256") { "salt-function${it.length}" }
private val HASHED_USER_TABLE = UserHashedTableAuth(
    table = mapOf(
        "admin" to DIGEST_FUNCTION("admin"),
    ),
    digester = DIGEST_FUNCTION
)

fun Application.configureSecurity() {
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access Person admin API"
            validate(validate())
        }
        form("form") {
            userParamName = "username"
            passwordParamName = "password"
            validate(validate())
        }
    }
}

private fun validate(): suspend ApplicationCall.(UserPasswordCredential) -> Principal? =
    { credential ->
        HASHED_USER_TABLE.authenticate(credential)
    }
