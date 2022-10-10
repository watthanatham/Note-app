package com.watthanatham.easynotes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.watthanatham.easynotes.data.Note
import com.watthanatham.easynotes.databinding.FragmentCreateNoteBinding
import com.watthanatham.easynotes.databinding.FragmentEditNoteBinding


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
        val id = navigationArgs.noteId
        viewModel.retrieveNote(id).observe(this.viewLifecycleOwner) { selectedNote ->
            note = selectedNote
            bind(note)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bind(note: Note) {
        binding.apply {
            btnUpdate.setOnClickListener { editNote() }
            btnDelete.setOnClickListener { showConfirmationDialog() }
        }
    }
    // not ready to use update function
    private fun editNote() {
        val action = EditNoteFragmentDirections.actionEditNoteFragmentToHomeFragment()
        this.findNavController().navigate(action)
    }
    //
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