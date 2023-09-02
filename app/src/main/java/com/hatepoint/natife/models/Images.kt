package com.hatepoint.natife.models

import kotlinx.serialization.Serializable

@Serializable
data class Images(
    val original: Original,
    val fixed_height: Preview
)