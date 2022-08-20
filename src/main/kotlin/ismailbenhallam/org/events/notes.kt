package ismailbenhallam.org.events

import io.ktor.events.EventDefinition
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.server.application.log
import ismailbenhallam.org.models.Person

// There are 2 ways of subscribing to events (Check DefaultApplicationEvents.kt for the built-in events)
// 1- Using the "monitor"

fun Application.handleEvent() {
    environment.monitor.subscribe(ApplicationStarted) {
        log.info("Application has started successfully")
    }
    environment.monitor.subscribe(CustomEvent) {
        log.info("person received: $it")
    }
}

// 2- Inside a Plugin
val ApplicationStartedPlugin = createApplicationPlugin("ApplicationStartedPlugin") {
    on(MonitoringEvent(ApplicationStarted)) {
        it.log.info("Application has started successfully")
    }

    on(MonitoringEvent(CustomEvent)) {
        this@createApplicationPlugin.application.log.info("person received: $it")
    }

}


private val CustomEvent = EventDefinition<Person>()