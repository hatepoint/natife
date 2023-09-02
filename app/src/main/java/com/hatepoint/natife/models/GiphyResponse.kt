package com.hatepoint.natife.models

import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(
    val `data`: List<Data>,
)