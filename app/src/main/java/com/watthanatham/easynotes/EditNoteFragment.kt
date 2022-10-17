package com.watthanatham.easynotes

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    var priority: Int = 3
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
        // show time
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
        hideKeyboard()
    }
    // get data
    private fun bind(note: Note) {
        binding.apply {
            etNoteTitle.setText(note.titleName, TextView.BufferType.SPANNABLE)
            etNoteDesc.setText(note.description, TextView.BufferType.SPANNABLE)
            checkNote()
            Log.d("priority", checkNote().toString())
            btnUpdate.setOnClickListener { updateNote() }
            btnDelete.setOnClickListener { showConfirmationDialog()  }

            // set note status
            binding.pRed.setOnClickListener { v ->
                priority = 1
                binding.pRed.setImageResource(R.drawable.ic_done)
                binding.pYellow.setImageResource(0)
                binding.pGreen.setImageResource(0)
            }
            // set note status
            binding.pYellow.setOnClickListener { v ->
                priority = 2
                binding.pRed.setImageResource(0)
                binding.pYellow.setImageResource(R.drawable.ic_done)
                binding.pGreen.setImageResource(0)
            }
            // set note status
            binding.pGreen.setOnClickListener { v ->
                priority = 3
                binding.pRed.setImageResource(0)
                binding.pYellow.setImageResource(0)
                binding.pGreen.setImageResource(R.drawable.ic_done)
            }
        }
    }
    private fun hideKeyboard() {
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    // check status to show on edit fragment and set note status as default
    private fun checkNote() {
        when (note.priority) {
            1 -> {
                binding.pRed.setImageResource(R.drawable.ic_done)
                priority = 1
            }
            2 -> {
                binding.pYellow.setImageResource(R.drawable.ic_done)
                priority = 2
            }
            3 -> {
                binding.pGreen.setImageResource(R.drawable.ic_done)
                priority = 3
            }
        }
    }

    private fun updateNote() {
        showSuccessDialog()
        return viewModel.updateNote(
            this.navigationArgs.noteId,
            this.binding.etNoteTitle.text.toString(),
            priority,
            this.binding.etNoteDesc.text.toString(),
            this.binding.showDateTime.text.toString()
        )
    }

    private fun deleteNote() {
        viewModel.deleteNote(note)
        findNavController().navigateUp()
    }

    // dialog will be show before delete
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete))
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
    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.update_title))
            .setMessage(getString(R.string.update))
            .show()
    }
}