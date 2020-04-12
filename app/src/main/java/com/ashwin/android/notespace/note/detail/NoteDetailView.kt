package com.ashwin.android.notespace.note.detail

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
import com.ashwin.android.notespace.common.hideKeyboard
import com.ashwin.android.notespace.common.makeToast
import com.ashwin.android.notespace.common.startWithFade
import com.ashwin.android.notespace.common.toEditable
import kotlinx.android.synthetic.main.fragment_note_detail.*

class NoteDetailView : Fragment() {
    private lateinit var viewModel: NoteDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        (frag_note_detail.background as AnimationDrawable).startWithFade()

        viewModel = ViewModelProvider(this, NoteDetailInjector(requireActivity().application).provideNoteViewModelFactory()).get(NoteDetailViewModel::class.java)

        showLoadingState()
        setUpClickListeners()
        setUpObservers()

        viewModel.handleEvent( NoteDetailEvent.OnStart(NoteDetailViewArgs.fromBundle(arguments!!).noteId) )
    }

    private fun showLoadingState() {
        (imv_note_detail_satellite.drawable as AnimationDrawable).start()
    }

    private fun setUpClickListeners() {
        imb_toolbar_back.setOnClickListener {
            goBack()
        }

        imb_toolbar_done.setOnClickListener {
            viewModel.handleEvent(NoteDetailEvent.OnDoneClick(note_detail_edittext.text.toString()))
        }

        imb_toolbar_delete.setOnClickListener {
            viewModel.handleEvent(NoteDetailEvent.OnDeleteClick)
        }

        /*requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.noteListView)
        }*/
    }

    private fun setUpObservers() {
        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
                event.getOnce()?.let {errorMessage ->
                    showErrorState(errorMessage)
                }
            }
        )

        viewModel.note.observe(viewLifecycleOwner, Observer { note ->
                note_detail_edittext.text = note.contents.toEditable()
            }
        )

        viewModel.updated.observe(viewLifecycleOwner, Observer {
                goBack()
            }
        )

        viewModel.deleted.observe(viewLifecycleOwner, Observer {
                goBack()
            }
        )
    }

    private fun showErrorState(errorMessage: String?) {
        makeToast(errorMessage!!)
        goBack()
    }

    private fun goBack() {
        hideKeyboard()
        //findNavController().navigate(R.id.noteListView)
        findNavController().navigateUp()
    }
}
