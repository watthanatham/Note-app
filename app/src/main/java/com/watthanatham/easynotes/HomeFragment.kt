package com.watthanatham.easynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.watthanatham.easynotes.adapter.NoteListAdapter
import com.watthanatham.easynotes.data.Note

import com.watthanatham.easynotes.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.observeOn
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.notesDao()
        )
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    var priority: Int = 1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = NoteListAdapter { note ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }
        binding.btnAddNote.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            this.findNavController().navigate(action)
        }

       binding.txtHigh.setOnClickListener {
            viewModel.highNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
       binding.txtMedium.setOnClickListener {
            viewModel.mediumNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
        binding.txtNormal.setOnClickListener {
            viewModel.normalNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
        binding.resetFilter.setOnClickListener {
            viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
