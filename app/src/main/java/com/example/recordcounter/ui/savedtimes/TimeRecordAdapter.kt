package com.example.recordcounter.ui.savedtimes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recordcounter.databinding.ItemTimeRecordBinding

class TimeRecordAdapter : ListAdapter<TimeRecord, TimeRecordAdapter.TimeRecordViewHolder>(TimeRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeRecordViewHolder {
        val binding = ItemTimeRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimeRecordViewHolder(private val binding: ItemTimeRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(timeRecord: TimeRecord) {
            binding.tvName.text = timeRecord.name
            binding.tvTime.text = timeRecord.time
        }
    }
}

class TimeRecordDiffCallback : DiffUtil.ItemCallback<TimeRecord>() {
    override fun areItemsTheSame(oldItem: TimeRecord, newItem: TimeRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TimeRecord, newItem: TimeRecord): Boolean {
        return oldItem == newItem
    }
}
