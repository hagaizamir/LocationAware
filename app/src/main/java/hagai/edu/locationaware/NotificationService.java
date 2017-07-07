package hagai.edu.locationaware;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

//service = main tread
//intent service = background / secondary tread

public class NotificationService extends IntentService {

    public  NotificationService (){

        //create an intent service
        //service name used to name worker thread , important only for debugging

        //required empty constructor:
        super("NotificationService");
    }

    //here the service will get it's mission parameter
    //the entry point to the service

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Do some work:
        //How do we report the result?
        //push notification: the only UI that the service is meant to do

        Context context = this;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //Bare minimum
        builder.setContentTitle("This is the Title");
        builder.setContentText("The Text");
        builder.setSmallIcon(R.drawable.ic_note); //Icon that match the standards.
        //builder.setContentIntent()

        Intent contentIntent = new Intent(this /*context*/ , MapsActivity.class);

        PendingIntent pi =
                PendingIntent.getActivity(this, 1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        Notification notification = builder.build();
        //title
        //message
        //icon

        //Action

        //Context.getSystemService
        //NotificationManager nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //LayoutInflater inflater =(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        //show the notification
        NotificationManagerCompat nm = NotificationManagerCompat.from(this /*context*/);
        nm.notify(1, notification);




        //Another option: using the service as a worker service (thread)-> how do we report? broadcasts


    }

}
