package hu.sztomek.pizzapp.presentation.screen.map

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.image.GlideApp
import timber.log.Timber
import javax.inject.Inject

class InfoWindowAdapter @Inject constructor(private val resources: Resources, mapActivity: MapActivity) : GoogleMap.InfoWindowAdapter {

    private val layoutInflater = LayoutInflater.from(mapActivity)
    private var marker: Marker? = null

    override fun getInfoContents(marker: Marker?): View? {
        var view: View? = null
        (marker?.tag as? Place)?.let {
            this.marker = marker
            val inflatedView = layoutInflater.inflate(R.layout.layout_info_window, null)
            val title = inflatedView.findViewById<TextView>(R.id.info_window_title)
            title.text = marker.title
            val content = inflatedView.findViewById<TextView>(R.id.info_window_message)
            content.text = marker.snippet
            content.setTextColor(resources.getColor(if (it.open) R.color.open else R.color.closed))
            val image = inflatedView.findViewById<ImageView>(R.id.info_window_image)
            GlideApp.with(image)
                .load(it.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_broken_image)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Timber.d("Failed to load image: $e")

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val thisMarker = this@InfoWindowAdapter.marker
                        if (thisMarker != null && dataSource != DataSource.MEMORY_CACHE) {
                            thisMarker.showInfoWindow()
                        }

                        return false
                    }
                })
                .into(image)

            view = inflatedView
        }

        return view
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}