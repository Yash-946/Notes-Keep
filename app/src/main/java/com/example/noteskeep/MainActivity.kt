package com.example.noteskeep

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteskeep.Adapter.NoteAdapter
import com.example.noteskeep.Database.NoteDatabase
import com.example.noteskeep.Models.Note
import com.example.noteskeep.Models.NotesViewModel
import com.example.noteskeep.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NoteAdapter.NoteClickListener,PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var adapter : NoteAdapter
    private lateinit var viewModel : NotesViewModel
    private lateinit var selectNote : Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note
            if(note != null){

                viewModel.updateNote(note)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)
        viewModel.allnote.observe(this){ list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = NoteDatabase.getDatabase(this)

    }

    private fun initUi() {
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = NoteAdapter(this,this)
        binding.recycleView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note") as? Note
                if(note != null){
                    viewModel.insertNote(note)
                }
            }
        }

        binding.fbAddNote.setOnClickListener{
            val intent = Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null){
                    adapter.filterList(newText)
                }
                return true
            }
        })

    }

    override fun OnItemClicked(note: Note) {
       val intent = Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun OnLongItemClicked(note: Note, cardview: CardView) {
        selectNote = note
        popUpDisplay(cardview)
    }

    private fun popUpDisplay(cardview: CardView) {
        val popup = PopupMenu(this,cardview)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectNote)
            return true
        }
        return false
    }
}