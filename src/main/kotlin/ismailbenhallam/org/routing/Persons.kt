package ismailbenhallam.org.routing

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/persons")
class Persons {

    @Serializable
    @Resource("/{id}")
    data class ById(val parent: Persons, val id: String)
}