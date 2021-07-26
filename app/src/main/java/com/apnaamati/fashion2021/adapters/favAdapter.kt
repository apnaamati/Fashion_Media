package com.apnaamati.fashion2021.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apnaamati.fashion2021.OfflineStorage
import com.apnaamati.fashion2021.R
import com.apnaamati.fashion2021.fullScreenVideo
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

class favAdapter(var context: Context, var favlist: ArrayList<String>) :
    RecyclerView.Adapter<favAdapter.ViewHolder>() {
    var activity: Activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(favlist[position]).into(holder.image)
        Picasso.get().load(favlist[position]).networkPolicy(NetworkPolicy.OFFLINE)
            .into(holder.image, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception) {
                    Picasso.get().load(favlist[position]).into(holder.image)
                }
            })
        val offlineStorage = OfflineStorage(context)
        if (offlineStorage.getString("fav")?.contains(favlist[position]) == true) {
            holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        val doubleClick = booleanArrayOf(false)
        val handler = Handler()
        holder.image.setOnClickListener {
            val r = Runnable { doubleClick[0] = false }
            if (doubleClick[0]) {
                favlist.removeAt(position)
                notifyItemRangeChanged(position, favlist.size)
                notifyItemRemoved(position)
                offlineStorage.putListString("fav", favlist)
                holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                doubleClick[0] = false
            } else {
                doubleClick[0] = true
                handler.postDelayed(r, 500)
            }
        }
        holder.btnShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT, """
     Download The Amazing Fashion App. Which Have more then 1000+ images and videos to download.The New Fashion Sale is here! 
     Hey please check this application https://play.google.com/store/apps/details?id=${context.packageName}
     """.trimIndent()
            )
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
        holder.btnFav.setOnClickListener {
            favlist.removeAt(position)
            notifyItemRangeChanged(position, favlist.size)
            notifyItemRemoved(position)
            offlineStorage.putListString("fav", favlist)
            holder.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        holder.btnFS.setOnClickListener {
            val intent = Intent(context, fullScreenVideo::class.java)
            intent.putExtra("fullImage", favlist[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favlist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var btnFav: ImageView
        var btnShare: ImageView
        var like: ImageView
        var btnFS: ImageView

        init {
            image = itemView.findViewById(R.id.img)
            btnFav = itemView.findViewById(R.id.btnFav)
            btnShare = itemView.findViewById(R.id.btnShare)
            like = itemView.findViewById(R.id.like)
            btnFS = itemView.findViewById(R.id.btnFS)
        }
    }

    init {
        activity = activity
    }
}