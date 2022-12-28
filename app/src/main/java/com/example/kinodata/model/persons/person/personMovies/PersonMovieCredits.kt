package com.example.kinodata.model.persons.person.personMovies

data class PersonMovieCredits(
    val cast: List<PersonActingMovies>,
    val crew: List<PersonMoviesAsCrew>,
    val id: Int
)