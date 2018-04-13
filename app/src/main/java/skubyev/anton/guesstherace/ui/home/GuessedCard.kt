package skubyev.anton.guesstherace.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.swipe.SwipeIn
import com.mindorks.placeholderview.annotations.swipe.SwipeOut
import com.squareup.picasso.Picasso
import skubyev.anton.guesstherace.R
import timber.log.Timber
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation

@Layout(R.layout.card_view)
class GuessedCard(
        private val context: Context,
        private val url: String,
        private val leftSwipeListener: () -> Unit,
        private val rightSwipeListener: () -> Unit
) {
    @View(R.id.imageView)
    private lateinit var imageView: ImageView

    @View(R.id.progressBar)
    private lateinit var progressBar: ProgressBar

    private var target = object: com.squareup.picasso.Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            val fadeOut = AlphaAnimation(0f, 1f)
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.duration = 1000
            imageView.startAnimation(fadeOut)
            imageView.setImageBitmap(bitmap)
            progressBar.visibility = android.view.View.INVISIBLE
            Timber.e("IMAGE SUCCESS.")
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {
            Timber.e("IMAGE FAILED.")
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            progressBar.visibility = android.view.View.VISIBLE
            Timber.e("IMAGE LOADING.")
        }
    }

    @Resolve
    private fun onResolved() {
        Picasso.with(context).load(url).into(target)
    }

    @SwipeOut
    private fun onSwipedOut() {
        rightSwipeListener.invoke()
    }

    @SwipeIn
    private fun onSwipeIn() {
        leftSwipeListener.invoke()
    }
}