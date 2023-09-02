package com.hatepoint.natife.models

import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val height: String,
    val size: String,
    val url: String,
    val width: String
)
