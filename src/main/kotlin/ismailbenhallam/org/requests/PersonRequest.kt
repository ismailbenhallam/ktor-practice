package ismailbenhallam.org.requests

import ismailbenhallam.org.models.Person
import kotlinx.serialization.Serializable

@Serializable
data class PersonRequest(val firstName: String? = null, val lastName: String? = null) {
    fun toPerson() = Person(firstName = firstName!!, lastName = lastName!!)
}