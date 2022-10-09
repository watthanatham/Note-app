package com.watthanatham.easynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.watthanatham.easynotes.adapter.NoteListAdapter
import com.watthanatham.easynotes.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.notesDao()
        )
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
//        val adapter = NoteListAdapter { note ->
//            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
//            this.findNavController().navigate(action)
//        }
//        binding.recyclerView.adapter = adapter
//        viewModel.allNotes.observe(this.viewLifecycleOwner) { note ->
//            note.let {
//                adapter.submitList(it)
//            }
//        }
        binding.btnAddNote.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            this.findNavController().navigate(action)
        }
    }

    companion object {

    }
}