// SavedTimesFragment.kt
package com.example.recordcounter.ui.savedtimes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recordcounter.databinding.FragmentSavedTimesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedTimesFragment : Fragment() {

    private lateinit var viewModel: SavedTimesViewModel
    private lateinit var binding: FragmentSavedTimesBinding
    private lateinit var adapter: TimeRecordAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedTimesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TimeRecordAdapter { timeRecord ->
            deleteTimeRecord(timeRecord)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SavedTimesFragment.adapter
        }

        viewModel = ViewModelProvider(this)[SavedTimesViewModel::class.java]
        viewModel.getAllRecords().observe(viewLifecycleOwner) { records ->
            records?.let { adapter.submitList(it) }
        }
    }

    private fun deleteTimeRecord(timeRecord: TimeRecord) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                viewModel.delete(timeRecord)
            } catch (e: Exception) {
                e.printStackTrace()
                android.widget.Toast.makeText(context, "Error deleting record", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
