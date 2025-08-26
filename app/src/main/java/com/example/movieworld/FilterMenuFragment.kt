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
 * Use the [FilterMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnExitFilter = view.findViewById<ImageButton>(R.id.btn_exit_filter)

        btnExitFilter.setOnClickListener {
            exitListener?.onExit()

        }

    }

    //FILTER BUTTON
    //sends data of when button is clicked to the main activity
    //allows menu tool bar to open

    private var exitListener: OnExitListener? = null

    //filter button interface
    interface OnExitListener {
        fun onExit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnExitListener) {
            exitListener = context
        } else {
            throw RuntimeException("$context must implement OnFilterListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        exitListener = null
    }

}