package com.example.movieworld

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [topMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopMainFragment : Fragment() {

    private var isMenuVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnFilter = view.findViewById<ImageButton>(R.id.btn_filter)

        btnFilter.setOnClickListener {
            filterListener?.onFilter()

        }
    }

    //FILTER BUTTON
    //sends data of when button is clicked to the main activity
    //allows menu tool bar to open

    private var filterListener: OnFilterListener? = null

    //filter button interface
    interface OnFilterListener {
        fun onFilter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilterListener) {
            filterListener = context
        } else {
            throw RuntimeException("$context must implement OnFilterListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        filterListener = null
    }
}