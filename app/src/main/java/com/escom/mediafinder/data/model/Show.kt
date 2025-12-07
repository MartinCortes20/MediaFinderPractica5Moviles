package com.escom.mediafinder.data.model

import com.google.gson.annotations.SerializedName

data class ShowSearchResponse(
    val score: Double,
    val show: Show
)

data class Show(
    val id: Int,
    val name: String,
    val language: String?,
    val genres: List<String>?,
    val status: String?,
    val premiered: String?,
    val rating: Rating?,
    val image: ImageUrl?,
    val summary: String?
)

data class Rating(
    val average: Double?
)

data class ImageUrl(
    val medium: String?,
    val original: String?
)