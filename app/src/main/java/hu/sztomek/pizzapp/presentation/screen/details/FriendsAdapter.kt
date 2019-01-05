package hu.sztomek.pizzapp.presentation.screen.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.presentation.image.GlideApp
import javax.inject.Inject

class FriendsAdapter @Inject constructor() : RecyclerView.Adapter<FriendsAdapter.FriendItemViewHolder>() {

    class FriendItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)) {

        fun bind(item: Friend) {
            GlideApp.with(itemView)
                .load(item.avatar)
                .placeholder(R.drawable.ic_broken_image)
                .apply(RequestOptions().circleCrop())
                .into(itemView as ImageView)
        }

    }

    private val items: MutableList<Friend> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
        return FriendItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(friends: List<Friend>) {
        items.clear()
        items.addAll(friends)

        notifyDataSetChanged()
    }

}