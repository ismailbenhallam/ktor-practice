package ismailbenhallam.org.plugins.custom

import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.response.header

class HeaderAppenderConfiguration {
    val headers = mutableMapOf<String, String>()
    fun header(name: String, value: String) = headers.put(name, value)
}

val HeaderAppender =
    createRouteScopedPlugin(
        name = "HeaderAppender",
        createConfiguration = ::HeaderAppenderConfiguration
    ) {
        onCallRespond { call ->
            pluginConfig.headers.forEach {
                call.response.header(it.key, it.value)
            }
        }
    }
