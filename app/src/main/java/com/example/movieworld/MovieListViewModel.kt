package com.example.movieworld

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class MovieListViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        _movies.value = listOf(
            Movie(
                title = "Ponyo",
                genre = "Animation, Family",
                year = 2008,
                rating = 7.8,
                duration = 101,
                description = "Sosuke rescues a goldfish trapped in a bottle. The goldfish, who is the daughter of a wizard, transforms herself into a young girl with her father's magic and falls in love with Sosuke.",
                imageRes = R.drawable.poster_ponyo,
                isFavorite = false
            ),
            Movie(
                title = "Mission: Impossible - The Final Reckoning",
                genre = "Action, Adventure, Thriller",
                year = 2025,
                rating = 7.3,
                duration = 169,
                description = "Hunt and the IMF pursue a dangerous AI called the Entity that's infiltrated global intelligence. With governments and a figure from his past in pursuit, Hunt races to stop it from forever changing the world.",
                imageRes = R.drawable.poster_missionimpossiblethefinalreckoning,
                isFavorite = false
            ),
            Movie(
                title = "The Naked Gun",
                genre = "Action, Comedy, Crime",
                year = 2025,
                rating = 7.0,
                duration = 85,
                description = "Lieutenant Frank Drebin Jr becomes a police officer like his legendary father and must save the police department from shutting down by solving a case.",
                imageRes = R.drawable.poster_thenakedgun,
                isFavorite = false
            ),
            Movie(
                title = "Weapons",
                genre = "Thriller, Horror, Mystery",
                year = 2025,
                rating = 7.8,
                duration = 128,
                description = "When all but one child from the same classroom mysteriously vanish on the same night at exactly the same time, a community is left questioning who or what is behind their disappearance.",
                imageRes = R.drawable.poster_weapons,
                isFavorite = false
            ),
            Movie(
                title = "Jurassic Park",
                genre = "Sci-Fi, Adventure",
                year = 1993,
                rating = 8.2,
                duration = 127,
                description = "An industrialist invites some experts to visit his theme park of cloned dinosaurs. After a power failure, the creatures run loose, putting everyone's lives, including his grandchildren's, in danger.",
                imageRes = R.drawable.poster_jurassicpark,
                isFavorite = false
            ),
            Movie(
                title = "The Lion King",
                genre = "Family, Adventure",
                year = 1994,
                rating = 8.5,
                duration = 88,
                description = "As a cub, Simba is forced to leave the Pride Lands after his father Mufasa is murdered by his wicked uncle, Scar. Years later, he returns as a young lion to reclaim his throne.",
                imageRes = R.drawable.poster_thelionking,
                isFavorite = false
            ),
            Movie(
                title = "Dune",
                genre = "Sci-Fi, Adventure",
                year = 2021,
                rating = 8.0,
                duration = 155,
                description = "Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.",
                imageRes = R.drawable.poster_dune,
                isFavorite = false
            ),
            Movie(
                title = "Oppenheimer",
                genre = "Thriller, Crime",
                year = 2023,
                rating = 8.3,
                duration = 180,
                description = "A dramatization of the life story of J. Robert Oppenheimer, the physicist who had a large hand in the development of the atomic bombs that brought an end to World War II.",
                imageRes = R.drawable.poster_oppenheimer,
                isFavorite = false
            ),
            Movie(
                title = "Pulp Fiction",
                genre = "Crime, Thriller",
                year = 1994,
                rating = 8.8,
                duration = 149,
                description = "In the realm of underworld, a series of incidents intertwines the lives of two Los Angeles mobsters, a gangster's wife, a boxer and two small-time criminals.",
                imageRes = R.drawable.poster_pulpfiction,
                isFavorite = false
            ),
            Movie(
                title = "Sky High",
                genre = "Family, Comedy",
                year = 2005,
                rating = 6.3,
                duration = 100,
                description = "Will, the son of The Commander and Jetstream, is enrolled in Sky High, a school for the children of superheroes. There, he discovers his true powers and learns about loyalty and friendship.\n",
                imageRes = R.drawable.poster_skyhigh,
                isFavorite = false
            )
        )
    }

    fun updateFavoriteByTitle(title: String, fav: Boolean) {
        val current = _movies.value ?: return
        val updated = current.toMutableList()
        for (i in updated.indices) {
            if (updated[i].title == title) {
                updated[i] = updated[i].copy(isFavorite = fav)
            }
        }
        _movies.value = updated.toList()
    }


}