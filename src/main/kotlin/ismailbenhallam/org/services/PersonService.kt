package ismailbenhallam.org.services

import ismailbenhallam.org.models.Person
import java.util.*

class PersonService {
    private val persons = mutableMapOf<UUID, Person>()

    infix fun add(person: Person) = persons.put(person.id, person)

    infix fun get(id: UUID) = persons[id]

    infix fun get(id: String) = get(UUID.fromString(id))

    infix fun remove(id: UUID) = persons.remove(id)

    infix fun remove(id: String) = remove(UUID.fromString(id))

    fun getAll() = persons.values

    fun isEmpty() = persons.isEmpty()
}