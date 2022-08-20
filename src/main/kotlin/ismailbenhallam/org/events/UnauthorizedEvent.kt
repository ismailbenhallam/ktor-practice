package ismailbenhallam.org.events

import io.ktor.events.EventDefinition
import io.ktor.server.application.Application
import io.ktor.server.application.log

val UnauthorizedEvent = EventDefinition<String>()

val failingAccessTracked = mutableMapOf<String, Int>()

fun Application.onUnauthorizedEvent() {
    this.environment.monitor.subscribe(UnauthorizedEvent) {
        if (failingAccessTracked.containsKey(it)) {
            failingAccessTracked[it] = failingAccessTracked[it]!! + 1
            if (failingAccessTracked[it]!! > 2)
                this@onUnauthorizedEvent.log.error("A user ($it) tried to access the API ${failingAccessTracked[it]}")
        } else {
            failingAccessTracked[it] = 1
        }
    }
}