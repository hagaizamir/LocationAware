package hagai.edu.locationaware;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * CFirebase Messeaging service for getting the cloud notification
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    //
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Ness", remoteMessage.toString());
    }
}
