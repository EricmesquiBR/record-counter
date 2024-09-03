package com.example.recordcounter.ui.savedtimes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recordcounter.R

class SavedTimesFragment : Fragment() {

    companion object {
        fun newInstance() = SavedTimesFragment()
    }

    private lateinit var viewModel: SavedTimesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_times, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavedTimesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}