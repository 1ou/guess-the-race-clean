package skubyev.anton.guesstherace.presentation.feedback

import android.annotation.SuppressLint
import android.os.Build
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class FeedbackPresenter @Inject constructor(
        private val router: Router
) : MvpPresenter<FeedbackView>() {

    fun onBackPressed() = router.exit()

    @SuppressLint("HardwareIds")
    fun getDeviceInfo() = "SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND: " + Build.BRAND + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: "+Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE
}