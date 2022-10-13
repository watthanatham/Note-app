package com.watthanatham.easynotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watthanatham.easynotes.R
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.databinding.ItemListNoteBinding


class NoteListAdapter(private val onItemClicked: (Note) -> Unit) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
    class NoteViewHolder(private var binding: ItemListNoteBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(note: Note) {
            binding.showDate.text = note.dateTime
            binding.showTitle.text = note.titleName
            binding.showDesc.text = note.description
            when(note.priority) {
                1 -> binding.priorityStatus.setBackgroundResource(R.drawable.red_shape)
                2 -> binding.priorityStatus.setBackgroundResource(R.drawable.yellow_shape)
                3 -> binding.priorityStatus.setBackgroundResource(R.drawable.green_shape)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(
            ItemListNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

}