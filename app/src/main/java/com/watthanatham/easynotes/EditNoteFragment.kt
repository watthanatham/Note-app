package com.watthanatham.easynotes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.databinding.FragmentEditNoteBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*


class EditNoteFragment : Fragment() {
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.notesDao()
        )
    }
    private val navigationArgs: EditNoteFragmentArgs by navArgs()
    lateinit var note: Note
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    var priority: Int = 1
    var currentDate:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding.showDateTime.text = currentDate
        val id = navigationArgs.noteId
        viewModel.retrieveNote(id).observe(this.viewLifecycleOwner) { selectedNote ->
            note = selectedNote
            bind(note)
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

    private fun bind(note: Note) {
        binding.apply {
            etNoteTitle.setText(note.titleName, TextView.BufferType.SPANNABLE)
            etNoteDesc.setText(note.description, TextView.BufferType.SPANNABLE)
            when (note.priority) {
                1 -> binding.pRed.setImageResource(R.drawable.ic_done)
                2 -> binding.pYellow.setImageResource(R.drawable.ic_done)
                3 -> binding.pGreen.setImageResource(R.drawable.ic_done)
            }
            btnUpdate.setOnClickListener { updateNote() }
            btnDelete.setOnClickListener { showConfirmationDialog()  }
            binding.pRed.setOnClickListener { v ->
                priority = 1
                binding.pRed.setImageResource(R.drawable.ic_done)
                binding.pYellow.setImageResource(0)
                binding.pGreen.setImageResource(0)
            }
            binding.pYellow.setOnClickListener { v ->
                priority = 2
                binding.pRed.setImageResource(0)
                binding.pYellow.setImageResource(R.drawable.ic_done)
                binding.pGreen.setImageResource(0)
            }
            binding.pGreen.setOnClickListener { v ->
                priority = 3
                binding.pRed.setImageResource(0)
                binding.pYellow.setImageResource(0)
                binding.pGreen.setImageResource(R.drawable.ic_done)
            }
        }
    }
    private fun updateNote() {
        return viewModel.updateNote(
            this.navigationArgs.noteId,
            this.binding.etNoteTitle.text.toString(),
            priority,
            this.binding.etNoteDesc.text.toString(),
            this.binding.showDateTime.text.toString()
        )
        val action = EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment()
        findNavController().navigate(action)
    }
    private fun deleteNote() {
        viewModel.deleteNote(note)
        findNavController().navigateUp()
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .show()
    }
}