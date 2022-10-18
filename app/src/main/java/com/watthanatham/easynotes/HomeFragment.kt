package com.watthanatham.easynotes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.watthanatham.easynotes.adapter.NoteListAdapter
import com.watthanatham.easynotes.data.Note

import com.watthanatham.easynotes.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.observeOn
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.notesDao()
        )
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var priority: Int = 3


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
        // set layout
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // navigate to edit fragment
        val adapter = NoteListAdapter { note ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note.id)
            this.findNavController().navigate(action)
        }
        // get all notes
        binding.recyclerView.adapter = adapter
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }
//        binding.recyclerView.setHasFixedSize(true)
        binding.btnAddNote.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            this.findNavController().navigate(action)
        }

        // filter note by red color
       binding.txtHigh.setOnClickListener {
            viewModel.highNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
        // filter note by yellow color
       binding.txtMedium.setOnClickListener {
            viewModel.mediumNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
        // filter note by green color
        binding.txtNormal.setOnClickListener {
            viewModel.normalNote.observe(this.viewLifecycleOwner) { notes ->
                notes.let {
                    adapter.submitList(it)
                }
            }
        }
        // reset to show all notes
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



// unused
//    var arrNotes = ArrayList<Note>()
//    arrNotes = notes as ArrayList<Note>
//        binding.searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return  true
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                var tempArr = ArrayList<Note>()
//                for (arr in arrNotes) {
//                    if (arr.titleName.toLowerCase(Locale.getDefault()).contains(p0.toString())){
//                        tempArr.add(arr)
//                    }
//                }
//                viewModel.setData(tempArr)
//                Log.d("data", tempArr.toString())
//                adapter.notifyDataSetChanged()
//                return true
//            }
//
//        })
