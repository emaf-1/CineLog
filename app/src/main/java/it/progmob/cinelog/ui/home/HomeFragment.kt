package it.progmob.cinelog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.progmob.cinelog.R
import it.progmob.cinelog.adapter.ContentAdapter

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ContentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        adapter = ContentAdapter { content ->
            // navigazione al dettaglio — la aggiungiamo dopo
        }
        val rv = view.findViewById<RecyclerView>(R.id.rv_contents)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // ProgressBar
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        // Osserva i dati dal ViewModel
        viewModel.contents.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // SearchView
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearch(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearch(newText ?: "")
                return true
            }
        })

        // Spinner tipo (Film / Serie / Tutti)
        val kindSpinner = view.findViewById<Spinner>(R.id.spinner_kind)
        val kindOptions = listOf("Tutti", "Film", "Serie TV")
        val kindAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, kindOptions)
        kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kindSpinner.adapter = kindAdapter
        kindSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                val kind = when (pos) {
                    1 -> "f"
                    2 -> "s"
                    else -> "all"
                }
                viewModel.setKindFilter(kind)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Spinner genere
        val genreSpinner = view.findViewById<Spinner>(R.id.spinner_genre)
        val genreOptions = listOf("Tutti", "Fantascienza", "Crime", "Dramma",
            "Commedia", "Horror", "Animazione", "Thriller", "Azione")
        val genreAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, genreOptions)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter
        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                val genre = if (pos == 0) "all" else genreOptions[pos]
                viewModel.setGenreFilter(genre)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // carica i dati
        viewModel.loadContents()
    }
}