package com.example.kinodata.model.persons.person.personTvSeries

data class PersonTvSeriesCredits(
    val cast: List<PersonActingTv>,
    val crew: List<PersonTvAsCrew>,
    val id: Int
)