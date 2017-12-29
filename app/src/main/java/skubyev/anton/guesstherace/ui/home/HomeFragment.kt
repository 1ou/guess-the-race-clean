package skubyev.anton.guesstherace.ui.home

import android.content.*
import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import skubyev.anton.guesstherace.BuildConfig.ORIGIN_IMAGES_ENDPOINT
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.presentation.home.HomePresenter
import skubyev.anton.guesstherace.presentation.home.HomeView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import skubyev.anton.guesstherace.ui.global.ConfirmDialog
import timber.log.Timber
import toothpick.Toothpick
import skubyev.anton.guesstherace.extension.visible
import skubyev.anton.guesstherace.ui.global.RateDialog


class HomeFragment : BaseFragment(), HomeView, ConfirmDialog.OnClickListener, RateDialog.OnClickListener {

    override val layoutRes = R.layout.fragment_home

    private lateinit var interstitialAd: InterstitialAd

    @InjectPresenter lateinit var presenter: HomePresenter

    companion object {
        private const val IMAGE_OVER_TAG = "image_over_tag"
        private const val RATE_TAG = "rate_tag"
    }

    @ProvidePresenter
    fun providePresenter(): HomePresenter {
        return Toothpick
                .openScopes(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(HomePresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }

        raceImageView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
                .setDisplayViewCount(1)
                .setSwipeDecor(SwipeDecor()
                        .setSwipeAnimFactor(100f)
                        .setSwipeAnimTime(100)
                        .setSwipeInMsgLayoutId(R.layout.swipe_white_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_black_msg_view))

        discussBTN.setOnClickListener { presenter.clickedDiscuss() }

        presenter.loadNotifications()

        notificationsIW.isClickable = true
        notificationsIW.setOnClickListener { presenter.clickedNotifications() }
        cart_badge.setOnClickListener { presenter.clickedNotifications() }

        initAdv()
    }

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val state = intent.getBooleanExtra("message", false)

            if (state) {
                val count = cart_badge.text.toString().toInt() + 1
                cart_badge.text = count.toString()
            }
        }
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(context!!)
                .registerReceiver(
                        messageReceiver,
                        IntentFilter("notification")
                )

        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(context!!)
                .unregisterReceiver(messageReceiver)

        super.onPause()
    }

    private fun initAdv() {
        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = getString(R.string.admob_unit_id)
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    override fun showImagesOverInfo() {
        ConfirmDialog.newInstants(
                getString(R.string.images_over_title),
                getString(R.string.images_over_text),
                getString(R.string.ok),
                getString(R.string.cancel),
                IMAGE_OVER_TAG
        ).show(childFragmentManager, IMAGE_OVER_TAG)
    }

    override val dialogConfirm: (tag: String) -> Unit = { tag ->
        when (tag) {
            IMAGE_OVER_TAG -> {
                presenter.clickedSettings()
            }
            else -> showMessage(getString(R.string.error_choose))
        }
    }

    override val dialogRate: (tag: String) -> Unit = { tag ->
        when (tag) {
            RATE_TAG -> {
                presenter.changeStateShowRate(false)
                launchMarket()
            }
        }
    }

    override fun showRateDialog() {
        RateDialog.newInstants(
                getString(R.string.rate_title),
                getString(R.string.rate_text),
                RATE_TAG
        ).show(childFragmentManager, RATE_TAG)
    }

    private fun launchMarket() {
        val uri = Uri.parse("market://details?id=" + context?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) { }
    }

    override fun showTraining() {
        carouselView.visible(true)
        raceImageView.visible(false)

        val sampleImages = intArrayOf(R.drawable.tutorial1, R.drawable.tutorial2, R.drawable.background_splash)

        carouselView.pageCount = sampleImages.size

        carouselView.setImageListener { position, imageView ->
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageResource(sampleImages[position])
        }

        carouselView.reSetSlideInterval(10000)
        carouselView.playCarousel()

        carouselView.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    raceImageView.visible(true)
                    carouselView.visible(false)
                    carouselView.pageCount = 0
                    showMessage(getString(R.string.swipe_help))
                }
            }
        })
    }

    override fun showNotifications(count: Int) {
        cart_badge.text = count.toString()
    }

    override fun showImage(image: Image) {
        showAdvertising()
        raceImageView.removeAllViews()

        val url: String
        val currentOrientation = resources.configuration.orientation

        url = if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(context).load(ORIGIN_IMAGES_ENDPOINT + image.urlAnswer).into(answerImageView)
            ORIGIN_IMAGES_ENDPOINT + image.url
        } else {
            Picasso.with(context).load(ORIGIN_IMAGES_ENDPOINT + image.urlAnswerLandscape).into(answerImageView)
            ORIGIN_IMAGES_ENDPOINT + image.urlLandscape
        }

        raceImageView.addView(GuessedCard(
                context!!,
                url,
                { presenter.clickedButton("white") },
                { presenter.clickedButton("black") }
        ))
        answerImageView.visibility = View.INVISIBLE

    }

    override fun showAnswer(url: String, state: Boolean) {
        answerImageView.visibility = View.VISIBLE

        if (state) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.victory)
            mediaPlayer.start()
            showMessage(resources.getString(R.string.you_guessed))
            showKonfetti()
            presenter.appendRating(true)
        } else {
            showMessage(resources.getString(R.string.you_not_guessed))
            presenter.appendRating(false)
        }
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private fun showAdvertising() {
        if (presenter.isShowAdv()) {
            if (interstitialAd.isLoaded) {
                interstitialAd.show()
            } else {
                Timber.d("TAG", "The interstitial wasn't loaded yet.")
            }
        }
    }

    private fun showKonfetti() {
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(3f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(8))
                .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .stream(50, 3000L)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}