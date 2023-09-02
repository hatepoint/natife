import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gif(
    @SerialName("userId") val userId: Int,
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String
)
