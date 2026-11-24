package it.progmob.cinelog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.progmob.cinelog.model.Content

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _contents = MutableLiveData<List<Content>>()
    val contents: LiveData<List<Content>> = _contents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // filtri correnti
    private var currentSearch: String = ""
    private var currentKind: String = "all"   // "all", "f", "s"
    private var currentGenre: String = "all"

    fun loadContents() {
        _isLoading.value = true

        var query: Query = db.collection("contents")
            .whereEqualTo("status", "approved")

        // filtro kind
        if (currentKind != "all") {
            query = query.whereEqualTo("kind", currentKind)
        }

        // filtro genere
        if (currentGenre != "all") {
            query = query.whereEqualTo("genre", currentGenre)
        }

        query.get()
            .addOnSuccessListener { result ->
                var list = result.toObjects(Content::class.java)

                // filtro ricerca testo (lato client, Firestore non supporta full-text)
                if (currentSearch.isNotBlank()) {
                    list = list.filter {
                        it.title.contains(currentSearch, ignoreCase = true)
                    }
                }

                _contents.value = list
                _isLoading.value = false
            }
            .addOnFailureListener {
                _contents.value = emptyList()
                _isLoading.value = false
            }
    }

    fun setSearch(query: String) {
        currentSearch = query
        loadContents()
    }

    fun setKindFilter(kind: String) {
        currentKind = kind
        loadContents()
    }

    fun setGenreFilter(genre: String) {
        currentGenre = genre
        loadContents()
    }
}