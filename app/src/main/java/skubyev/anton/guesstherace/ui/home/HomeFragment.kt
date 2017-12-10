package skubyev.anton.guesstherace.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.mindorks.placeholderview.SwipeDecor
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.SwipeViewBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import skubyev.anton.guesstherace.R
import skubyev.anton.guesstherace.extension.random
import skubyev.anton.guesstherace.model.data.storage.Image
import skubyev.anton.guesstherace.presentation.home.HomePresenter
import skubyev.anton.guesstherace.presentation.home.HomeView
import skubyev.anton.guesstherace.toothpick.DI
import skubyev.anton.guesstherace.ui.global.BaseFragment
import timber.log.Timber
import toothpick.Toothpick


class HomeFragment : BaseFragment(), HomeView {

    override val layoutRes = R.layout.fragment_home

    private lateinit var interstitialAd: InterstitialAd

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @InjectPresenter lateinit var presenter: HomePresenter

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
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_white_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_black_msg_view))

        discussBTN.setOnClickListener { presenter.clickedDiscuss() }

        presenter.loadNotifications()

        notificationsIW.isClickable = true
        notificationsIW.setOnClickListener { presenter.clickedNotifications() }

        initFirebaseAnalytics()
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
        setHasOptionsMenu(true)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(context!!)
                .unregisterReceiver(messageReceiver)

        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    private fun initFirebaseAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "screen")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "home")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun initAdv() {
        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = getString(R.string.admob_unit_id)
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    override fun showNotifications(count: Int) {
        cart_badge.text = count.toString()
    }

    override fun showImage(image: Image) {
        showAdvertising()
        raceImageView.removeAllViews()
        raceImageView.addView(GuessedCard(
                context!!,
                image.url,
                { presenter.clickedButton("white") },
                { presenter.clickedButton("black") }
        ))
        answerImageView.visibility = View.INVISIBLE
        Picasso.with(context).load(image.urlAnswer).into(answerImageView)
    }

    override fun showAnswer(url: String, state: Boolean) {
        answerImageView.visibility = View.VISIBLE

        if (state) {
            showMessage(resources.getString(R.string.you_guessed))
//            showKonfetti()
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
        if ((0..20).random() == 10) {
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