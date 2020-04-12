package com.ashwin.android.notespace.note.list

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ashwin.android.notespace.R
import com.ashwin.android.notespace.common.makeToast
import com.ashwin.android.notespace.common.startWithFade
import kotlinx.android.synthetic.main.fragment_note_list.*

class NoteListView : Fragment() {
    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this, NoteListInjector(requireActivity().application).provideNoteListViewModelFactory()).get(NoteListViewModel::class.java)

        (space_bg_imageview.drawable as AnimationDrawable).startWithFade()

        showLoadingState()
        setUpAdapter()
        setUpObservers()
        setUpClickListeners()

        load()
    }

    private fun load() {
        viewModel.handleEvent(NoteListEvent.OnStart)
    }

    private fun showLoadingState() = (satellite_imageview.drawable as AnimationDrawable).start()

    private fun setUpAdapter() {
        adapter = NoteListAdapter()
        noteslist_recyclerview.adapter = adapter
    }

    private fun setUpObservers() {
        adapter.event.observe(viewLifecycleOwner, Observer {
                viewModel.handleEvent(it)
            }
        )

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
                event.getOnce()?.let { errorMessage ->
                    showErrorState(errorMessage)
                }
            }
        )

        viewModel.noteList.observe(viewLifecycleOwner, Observer { noteList ->
            adapter.submitList(noteList)
            if (noteList.isNotEmpty()) {
                (satellite_imageview.drawable as AnimationDrawable).stop()
                satellite_imageview.visibility = View.INVISIBLE
                noteslist_recyclerview.visibility = View.VISIBLE
            }
        })

        viewModel.editNote.observe(viewLifecycleOwner, Observer { event ->
                event.getOnce()?.let {
                    startNoteDetailWithArgs(it)
                }
            }
        )
    }

    private fun setUpClickListeners() {
        fab_create_new_item.setOnClickListener {
            val direction = NoteListViewDirections.actionNoteListViewToNoteDetailView("")
            findNavController().navigate(direction)
        }

        toolbar_refresh_imagebutton.setOnClickListener {
            load()
        }

        toolbar_auth_imagebutton.setOnClickListener {
            findNavController().navigate(R.id.loginActivity)
        }
    }

    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)

    private fun startNoteDetailWithArgs(noteId: String) {
        val action = NoteListViewDirections.actionNoteListViewToNoteDetailView(noteId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // THIS IS IMPORTANT!!! Causes memory leak
        noteslist_recyclerview.adapter = null
    }
}
