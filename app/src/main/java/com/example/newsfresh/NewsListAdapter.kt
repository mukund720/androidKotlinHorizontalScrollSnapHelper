package com.example.newsfresh

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsListAdapter(
    private val listener: NewsItemClicked,
    container: RecyclerView,
    spacingDp: Float,
    sideItemsVisibleDp: Float
) : RecyclerView.Adapter<NewsViewHolder>() {
    val rcView = container
    private val items: ArrayList<News> = ArrayList()
    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    lateinit var front_animation: AnimatorSet
    lateinit var back_animation: AnimatorSet
    var isFront = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        (parent.context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        view.setOnClickListener {
            listener.onItemClicked(items[viewHolder.adapterPosition])
            // flipCard(view,parent.context)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.authorView.text = currentItem.author
//        holder.imageView.sour=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.imageView)
        // set height and width
        val itemPadding = 8
        screenWidth = displayMetrics.widthPixels
        //here you may change the divide amount from 2.5 to whatever you need
        val itemWidth = (screenWidth - itemPadding).div(1.5)
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = layoutParams.height
        layoutParams.width = itemWidth.toInt()
        holder.itemView.layoutParams = layoutParams
    }

    fun updateNews(updatedItems: ArrayList<News>) {
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun flipCard(view: View, context: Context) {

        var scale = DisplayMetrics().density
        val front = view.findViewById<CardView>(R.id.cvImage)
        val back = view.findViewById<CardView>(R.id.cvImage)
        front.cameraDistance = 8000 * scale
        back.cameraDistance = 8000 * scale


        // Now we will set the front animation
        front_animation =
            AnimatorInflater.loadAnimator(context, R.animator.front_animator) as AnimatorSet
        back_animation =
            AnimatorInflater.loadAnimator(context, R.animator.back_animator) as AnimatorSet

        // Now we will set the event listener

        if (isFront) {
            front_animation.setTarget(front)
            back_animation.setTarget(back)
            front_animation.start()
            back_animation.start()
            isFront = false

        } else {
            front_animation.setTarget(back)
            back_animation.setTarget(front)
            back_animation.start()
            front_animation.start()
            isFront = true

        }
    }

}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.title)
    val authorView: TextView = itemView.findViewById(R.id.author)
    val imageView: ImageView = itemView.findViewById(R.id.image)

}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}