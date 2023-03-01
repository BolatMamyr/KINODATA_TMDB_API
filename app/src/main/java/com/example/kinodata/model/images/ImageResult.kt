package com.example.kinodata.model.images

data class ImageResult(
    val backdrops: List<Photo>,
    val id: Int,
    val logos: List<Photo>,
    val posters: List<Photo>,
    // for person Photos
    val profiles: List<Photo>
)