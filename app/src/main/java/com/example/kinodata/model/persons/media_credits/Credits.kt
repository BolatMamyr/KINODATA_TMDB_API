package com.example.kinodata.model.persons.media_credits

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) {

    fun getFirstFourActors(): String {
        val cast = cast.take(4)
        val names = cast.map { it.name }
        var res = ""
        for ((index, name) in names.withIndex()) {
            if(index == 0) {
                res += name
            } else {
                res += ", $name"
            }
        }
        return res
    }
}