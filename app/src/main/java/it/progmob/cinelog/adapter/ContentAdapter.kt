package it.progmob.cinelog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.progmob.cinelog.R
import it.progmob.cinelog.model.Content

class ContentAdapter(
    private val onClick: (Content) -> Unit
) : ListAdapter<Content, ContentAdapter.ContentViewHolder>(DiffCallback()) {

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.item_title)
        val kind: TextView = itemView.findViewById(R.id.item_kind)
        val genre: TextView = itemView.findViewById(R.id.item_genre)
        val year: TextView = itemView.findViewById(R.id.item_year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content, parent, false)
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = getItem(position)
        holder.title.text = content.title
        holder.kind.text = if (content.kind == "f") "Film" else "Serie TV"
        holder.genre.text = content.genre
        holder.year.text = content.release.take(4)  // solo l'anno
        holder.itemView.setOnClickListener { onClick(content) }
    }

    class DiffCallback : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Content, newItem: Content) =
            oldItem == newItem
    }
}