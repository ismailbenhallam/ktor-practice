package ismailbenhallam.org.events

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
  import io.ktor.server.application.log

fun Application.onApplicationStoppedEvent() {
    // This
    /*install(GoodByeLogger) {
        this.message = "See u later ;)"
    }*/
    // Could be replaced with
    environment.monitor.subscribe(ApplicationStopped) {
        it.log.info("See u later ;)")
    }
}