package hagai.edu.locationaware;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

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
       // showNotification();
        try {
            //use internet permissions
            URL url = new URL("https://www.google.com");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder builder = new StringBuilder();


            while ((line = reader.readLine()) !=null){
                builder.append(line).append("\n");
            }
            //done
            String result = builder.toString();

            //report to result
            Intent message = new Intent("ITunesChannel");
            message.putExtra("json", result);

            //messanger in c#
            //event....bus
            LocalBroadcastManager.getInstance(this).sendBroadcast(message);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void showNotification() {
        // Do some work:
        //How do we report the result?
        //push notification: the only UI that the service is meant to do

        Context context = this;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //Bare minimum
        builder.setContentTitle("This is the Title" + new Date());
        builder.setContentText("The Text");
        builder.setSmallIcon(R.drawable.ic_note); //Icon that match the standards.
        //builder.setContentIntent()

        builder.setAutoCancel(true);

        Intent contentIntent = new Intent(this /*context*/ , MapsActivity.class);
        contentIntent.putExtra("Map" , "A");
        contentIntent.putExtra("Map" , "B");

        //we always get the same pending intent
        //update current means that we want to update the extras:
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
