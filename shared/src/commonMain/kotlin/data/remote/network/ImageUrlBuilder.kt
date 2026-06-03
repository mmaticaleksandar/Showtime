package data.remote.network

object ImageUrlBuilder {

    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

    //Poster slika
    fun posterUrl(path: String?, size: String = "w500"): String? {
        return path?.let {
            BASE_IMAGE_URL + size + it
        }
    }

    //Pozadina
    fun backdropUrl(
        path: String?,
        size: String = "w780"
    ): String? {
        return path?.let {
            BASE_IMAGE_URL + size + it
        }
    }

    //Profilna glumca
    fun profileUrl(
        path: String?,
        size: String = "w185"
    ): String? {
        return path?.let {
            BASE_IMAGE_URL + size + it
        }
    }
}