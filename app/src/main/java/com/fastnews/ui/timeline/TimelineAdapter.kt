package com.fastnews.ui.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fastnews.R
import com.fastnews.service.model.PostData
import com.fastnews.service.model.realm.PostResponseEntity
import io.realm.Realm
import io.realm.exceptions.RealmException
import kotlinx.android.synthetic.main.include_item_timeline_thumbnail.view.*

/**
 * TimelineAdapter
 */
class TimelineAdapter(val onClickItem: (PostData, ImageView) -> Unit) :
    RecyclerView.Adapter<TimelineItemViewHolder>() {

    //region fields
    private var items: MutableList<PostData> = mutableListOf()
    //endregion

    //region superclass
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineItemViewHolder =
        TimelineItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_timeline,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TimelineItemViewHolder, position: Int) {
        holder.data = items[position]
        holder.view.setOnClickListener {
            onClickItem(
                items[position],
                holder.view.item_timeline_thumbnail
            )
        }
    }
    //endregion

    //region privates

    /**
     * Add new content do Relm to consume when user is offline
     */
    private fun setRealm(items: MutableList<PostData>) {
        val realm = Realm.getDefaultInstance()

        items.forEach {
            realm.beginTransaction()
            try {
                val postResponseEntity = PostResponseEntity()
                postResponseEntity.id = it.name
                postResponseEntity.name = it.name
                postResponseEntity.author = it.author
                postResponseEntity.title = it.title
                postResponseEntity.created_utc = it.created_utc
                postResponseEntity.num_comments = it.num_comments
                postResponseEntity.score = it.score
                postResponseEntity.thumbnail = it.thumbnail
                postResponseEntity.url = it.url
                realm.insertOrUpdate(postResponseEntity)
                realm.commitTransaction()
            } catch (e: RealmException) {
                Log.e("TimelineAdapter", e.toString())
            }
        }
    }
    //endregion

    /**
     * Add new content in items
     */
    fun addData(items: MutableList<PostData>) {
        setRealm(items)

        val size = this.items.size
        this.items.addAll(items)
        val sizeNew = this.items.size
        notifyItemRangeChanged(size, sizeNew)
        notifyDataSetChanged()
    }

    /**
     * Set all data in items
     */
    fun setData(items: MutableList<PostData>) {
        setRealm(items)

        this.items = items
        notifyDataSetChanged()
    }


}