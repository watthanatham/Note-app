package com.watthanatham.easynotes

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.databinding.FragmentCreateNoteBinding
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.notesDao()
        )
    }
    var currentDate:String? = null
    lateinit var note: Note
    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!
    var priority: Int = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding.showDateTime.text = currentDate
        binding.pRed.setOnClickListener {
            priority = 1
            binding.pRed.setImageResource(R.drawable.ic_done)
            binding.pYellow.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }
        binding.pYellow.setOnClickListener {
            priority = 2
            binding.pRed.setImageResource(0)
            binding.pYellow.setImageResource(R.drawable.ic_done)
            binding.pGreen.setImageResource(0)
        }
        binding.pGreen.setOnClickListener {
            priority = 3
            binding.pRed.setImageResource(0)
            binding.pYellow.setImageResource(0)
            binding.pGreen.setImageResource(R.drawable.ic_done)
        }
            binding.btnSave.setOnClickListener {
                addNewNote()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.etNoteTitle.text.toString(),
            binding.etNoteDesc.text.toString()
        )
    }
    private fun addNewNote() {
        if(isEntryValid()) {
             viewModel.addNewNote(
                binding.etNoteTitle.text.toString(),
                priority,
                binding.etNoteDesc.text.toString(),
                binding.showDateTime.text.toString(),
            )
            val action = CreateNoteFragmentDirections.actionCreateNoteFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}