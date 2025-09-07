package com.example.movieworld

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

//MovieListViewModel holds ALL movie data and remembers favourites.
//Survives screen rotation.

class MovieListViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()    // _movies is the private lists of movies (only this class can change it)
    val movies: LiveData<List<Movie>> = _movies             // movies is the public version (other classes can observe it but not change)

    // stores the state when switching between fragments
    //persistent state
    var currentQuery: String = ""
    var selectedGenres: MutableSet<String> = mutableSetOf()
    var showFavouritesOnly: Boolean = false

    //init runs when the app start, and fills the list with the below 10 movies
    init {
        _movies.value = listOf(
            Movie(
                id = 1,
                title = "Ponyo",
                categories = listOf("Animation"),
                year = 2008,
                rating = 7.8,
                duration = 101,
                description = "Sosuke rescues a goldfish trapped in a bottle. The goldfish, who is the daughter of a wizard, transforms herself into a young girl with her father's magic and falls in love with Sosuke.",
                imageRes = R.drawable.poster_ponyo,
                url = "https://www.imdb.com/title/tt0876563/",
                isFavorite = false
            ),
            Movie(
                id = 2,
                title = "Baby Driver",
                categories = listOf("Action", "Drama"),
                year = 2017,
                rating = 7.5,
                duration = 113,
                description = "Hunt and the IMF pursue a dangerous AI called the Entity that's infiltrated global intelligence. With governments and a figure from his past in pursuit, Hunt races to stop it from forever changing the world.",
                imageRes = R.drawable.poster_babydriver,
                url = "https://www.imdb.com/title/tt3890160/",
                isFavorite = false
            ),
            Movie(
                id = 3,
                title = "The Naked Gun",
                categories = listOf("Comedy", "Crime"),
                year = 2025,
                rating = 7.0,
                duration = 85,
                description = "Lieutenant Frank Drebin Jr becomes a police officer like his legendary father and must save the police department from shutting down by solving a case.",
                imageRes = R.drawable.poster_thenakedgun,
                url = "https://www.imdb.com/title/tt3402138/",
                isFavorite = false
            ),
            Movie(
                id = 4,
                title = "Weapons",
                categories = listOf("Horror", "Mystery"),
                year = 2025,
                rating = 7.8,
                duration = 128,
                description = "When all but one child from the same classroom mysteriously vanish on the same night at exactly the same time, a community is left questioning who or what is behind their disappearance.",
                imageRes = R.drawable.poster_weapons,
                url = "https://www.imdb.com/title/tt26581740/",
                isFavorite = false
            ),
            Movie(
                id = 5,
                title = "Jurassic Park",
                categories = listOf("Sci-Fi", "Adventure"),
                year = 1993,
                rating = 8.2,
                duration = 127,
                description = "An industrialist invites some experts to visit his theme park of cloned dinosaurs. After a power failure, the creatures run loose, putting everyone's lives, including his grandchildren's, in danger.",
                url = "https://www.imdb.com/title/tt0107290/",
                imageRes = R.drawable.poster_jurassicpark,
                isFavorite = false
            ),
            Movie(
                id = 6,
                title = "The Lion King",
                categories = listOf("Family", "Adventure"),
                year = 1994,
                rating = 8.5,
                duration = 88,
                description = "As a cub, Simba is forced to leave the Pride Lands after his father Mufasa is murdered by his wicked uncle, Scar. Years later, he returns as a young lion to reclaim his throne.",
                imageRes = R.drawable.poster_thelionking,
                url = "https://www.imdb.com/title/tt0110357/",
                isFavorite = false
            ),
            Movie(
                id = 7,
                title = "Dune",
                categories = listOf("Sci-Fi", "Adventure"),
                year = 2021,
                rating = 8.0,
                duration = 155,
                description = "Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.",
                imageRes = R.drawable.poster_dune,
                url = "https://www.imdb.com/title/tt1160419/",
                isFavorite = false
            ),
            Movie(
                id = 8,
                title = "Oppenheimer",
                categories = listOf("Thriller", "Crime"),
                year = 2023,
                rating = 8.3,
                duration = 180,
                description = "A dramatization of the life story of J. Robert Oppenheimer, the physicist who had a large hand in the development of the atomic bombs that brought an end to World War II.",
                imageRes = R.drawable.poster_oppenheimer,
                url = "https://www.imdb.com/title/tt15398776/",
                isFavorite = false
            ),
            Movie(
                id = 9,
                title = "Pulp Fiction",
                categories = listOf("Thriller", "Crime"),
                year = 1994,
                rating = 8.8,
                duration = 149,
                description = "In the realm of underworld, a series of incidents intertwines the lives of two Los Angeles mobsters, a gangster's wife, a boxer and two small-time criminals.",
                imageRes = R.drawable.poster_pulpfiction,
                url = "https://www.imdb.com/title/tt0110912/",
                isFavorite = false
            ),
            Movie(
                id = 10,
                title = "Sky High",
                categories = listOf("Family", "Comedy"),
                year = 2005,
                rating = 6.3,
                duration = 100,
                description = "Will, the son of The Commander and Jetstream, is enrolled in Sky High, a school for the children of superheroes. There, he discovers his true powers and learns about loyalty and friendship.\n",
                imageRes = R.drawable.poster_skyhigh,
                url = "https://www.imdb.com/title/tt0405325/",
                isFavorite = false
            )
        )
    }


    //Function that updates favourite status of a movie by its ID, called whenever user taps heart button.
    fun updateFavoriteById(id: Int, fav: Boolean) {
        val current = _movies.value ?: return       //Get current list or exit if null.
        val updated = current.toMutableList()       //Make a mutable copy so we can edit

        //For each movie, check if the ID matches what we want to update.
        for (i in updated.indices) {
            if (updated[i].id == id) {
                //If ID matches, replace the old movie with a new copy where isFavourite is set to fav.
                updated[i] = updated[i].copy(isFavorite = fav)
            }
        }
        _movies.value = updated.toList()        //Update the list (triggers UI update).
    }
}