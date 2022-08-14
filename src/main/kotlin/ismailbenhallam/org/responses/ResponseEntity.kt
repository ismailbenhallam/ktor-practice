package ismailbenhallam.org.responses

import kotlinx.serialization.Serializable

@Serializable
data class ResponseEntity(var message: String, var statusCode: Int)