package hagai.edu.locationaware;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Hagai Zamir on 07-Jul-17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //get the new Token here
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //99% no user yet. the app was just installed.
        //the user didn't sign in yet.

        SharedPreferences prefs = this.getSharedPreferences("userID", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userID", token);
        editor.apply();


        //FirebaseDatabase.getInstance().getReference("Users");
    }
}
