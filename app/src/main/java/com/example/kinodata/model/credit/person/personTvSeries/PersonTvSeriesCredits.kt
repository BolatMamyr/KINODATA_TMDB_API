package com.example.kinodata.model.credit.person.personTvSeries

data class PersonTvSeriesCredits(
    val cast: List<PersonTvSeries>,
    val crew: List<PersonTvCrew>,
    val id: Int
)