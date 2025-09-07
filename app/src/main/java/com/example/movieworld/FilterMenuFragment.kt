package com.example.movieworld

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton

class FilterMenuFragment : Fragment() {

    //listeners to send data to main activity
    private var filterListener: OnFilterChangedListener? = null
    private var exitListener: OnExitListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnExitFilter = view.findViewById<ImageButton>(R.id.btn_exit_filter)
        btnExitFilter.setOnClickListener {
            exitListener?.onExit()
        }


        //All the movie genres (categories)
        val checkboxes = listOf(
            view.findViewById<CheckBox>(R.id.btnAction),
            view.findViewById<CheckBox>(R.id.btnAdventure),
            view.findViewById<CheckBox>(R.id.btnAnimation),
            view.findViewById<CheckBox>(R.id.btnComedy),
            view.findViewById<CheckBox>(R.id.btnCrime),
            view.findViewById<CheckBox>(R.id.btnFamily),
            view.findViewById<CheckBox>(R.id.btnHorror),
            view.findViewById<CheckBox>(R.id.btnMystery),
            view.findViewById<CheckBox>(R.id.btnSciFi),
            view.findViewById<CheckBox>(R.id.btnThriller)
        )

        val updateFilter = {
            val selectedGenres = checkboxes.filter { it.isChecked }
                .map { it.text.toString() }
                .toSet()
            filterListener?.onFilterChanged(selectedGenres)
        }

        // Attach listener to each checkbox
        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, _ -> updateFilter() }
        }

        //clears all filters
        val btnClear = view.findViewById<Button>(R.id.btnClear)
        btnClear.setOnClickListener{
            checkboxes.forEach { it.isChecked = false }
            filterListener?.onFilterChanged(emptySet())
        }
    }

    // FILTER LISTENER
    interface OnFilterChangedListener {
        fun onFilterChanged(selected: Set<String>)
    }

    // EXIT LISTENER
    interface OnExitListener {
        fun onExit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilterChangedListener) {
            filterListener = context
        }
        if (context is OnExitListener) {
            exitListener = context
        }
        if (filterListener == null && exitListener == null) {
            throw RuntimeException("$context must implement OnFilterChangedListener or OnExitListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        filterListener = null
        exitListener = null
    }
}
