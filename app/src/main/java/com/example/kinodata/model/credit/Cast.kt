package com.example.kinodata.model.credit

data class Cast(
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Cast
        if (id != other.id || name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = cast_id
        result = 31 * result + credit_id.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        return result
    }


}