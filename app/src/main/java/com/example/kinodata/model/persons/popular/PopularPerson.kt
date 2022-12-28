package com.example.kinodata.model.persons.popular

data class PopularPerson(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for: List<KnownFor>,
    val known_for_department: String,
    val name: String,
    val popularity: Double,
    val profile_path: String
) {
    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) return false

        other as PopularPerson
        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }

}