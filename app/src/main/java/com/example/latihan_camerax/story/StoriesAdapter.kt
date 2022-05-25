package com.example.latihan_camerax.story

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.latihan_camerax.api.ListStory
import com.example.latihan_camerax.databinding.ListStoryBinding

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.ListStoryHolder>() {

    private val allStories = ArrayList<ListStory>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListStory(stories: ArrayList<ListStory>) {
        allStories.clear()
        allStories.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ListStoryHolder, position: Int) {
        val (name, description, photoUrl, _) = allStories[position]
        holder.binding.tvName.text = name
        holder.binding.tvDescription.text = description
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgUploaded)
        holder.binding.imgUploaded.setOnClickListener {
            onItemClickCallback.onItemClicked(allStories[holder.adapterPosition])
        }
    }

    override fun getItemCount() = allStories.size

    class ListStoryHolder(var binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStory)
    }
}
