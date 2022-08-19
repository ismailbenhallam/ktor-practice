package ismailbenhallam.org.routing

import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticBasePackage
import io.ktor.server.routing.Routing

fun Routing.serverAssets() {
    static("/assets") {
        staticBasePackage = "assets"
        defaultResource("hello.png")
        resources(".")
        // This is optional, we can use it to give a virtual (easier) name to our resources
        for (i in 1..3) {
            resource("$i", "img$i.jpeg")
        }
    }
}