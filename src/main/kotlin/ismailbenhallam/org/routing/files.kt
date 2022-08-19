package ismailbenhallam.org.routing

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.response.header
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import java.io.File

// Static Routing is more clean and easy..
fun Routing.serveFiles() {
    get("/") {
        val file = File(javaClass.classLoader.getResource("assets/hello.png")!!.file)
        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Inline
                .withParameter(ContentDisposition.Parameters.FileName, "assets/hello.png")
                .withParameter(ContentDisposition.Parameters.Size, file.length().toString())
                .toString()
        )
        call.respondFile(file)
    }
}