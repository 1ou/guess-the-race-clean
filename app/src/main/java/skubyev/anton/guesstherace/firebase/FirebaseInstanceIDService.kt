package skubyev.anton.guesstherace.firebase

import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

class FirebaseInstanceIDService: com.google.firebase.iid.FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Timber.d( "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String) {

    }
}