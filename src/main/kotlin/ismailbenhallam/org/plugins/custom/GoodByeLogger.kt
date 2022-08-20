package ismailbenhallam.org.plugins.custom

import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent

data class GoodByeLoggerConfiguration(var message: String = "Good Bye :)")

val GoodByeLogger =
    createApplicationPlugin(
        name = "GoodByeLogger",
        createConfiguration = ::GoodByeLoggerConfiguration
    ) {
        on(MonitoringEvent(ApplicationStopped)) {
            application.environment.log.info(this.pluginConfig.message)
        }
    }
