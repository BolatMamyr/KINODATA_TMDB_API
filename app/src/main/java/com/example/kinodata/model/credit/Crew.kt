package com.example.kinodata.model.credit

data class Crew(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Crew
        if (id != other.id || name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + credit_id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}