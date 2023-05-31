package com.example.noteskeep.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeep.Models.Note
import com.example.noteskeep.R
import kotlin.random.Random

class NoteAdapter(private val context:Context, private val listener:NoteClickListener)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NoteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val notes_layout = itemview.findViewById<CardView>(R.id.card_layout)
        val title = itemview.findViewById<TextView>(R.id.tv_title)
        val note_tv = itemview.findViewById<TextView>(R.id.tv_note)
        val date = itemview.findViewById<TextView>(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_view,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentnote = NotesList[position]

        holder.title.text = currentnote.title
        holder.title.isSelected = true

        holder.note_tv.text = currentnote.note

        holder.date.text = currentnote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(),null))

        holder.notes_layout.setOnClickListener{
            listener.OnItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
            listener.OnLongItemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }

    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun randomColor():Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    interface NoteClickListener{
        fun OnItemClicked(note:Note)
        fun OnLongItemClicked(note:Note,cardview:CardView)
    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(newList)

        notifyDataSetChanged()
    }

    fun filterList(search:String){
        NotesList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){

                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }
}

