package ismailbenhallam.org.requests

import ismailbenhallam.org.models.Person
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PersonRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val id: String? = UUID.randomUUID().toString()
) {
    fun toPerson() = Person(firstName = firstName!!, lastName = lastName!!, id = UUID.fromString(id))
}