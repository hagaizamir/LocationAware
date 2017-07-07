package hagai.edu.locationaware;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FencingFragment extends Fragment {


    @BindView(R.id.fabNotify)
    FloatingActionButton fabNotify;
    Unbinder unbinder;

    public FencingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fencing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String json = intent.getStringExtra("json");
                Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter filter = new IntentFilter("ITunesChannel");
        LocalBroadcastManager.getInstance(getContext()).
                registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabNotify)
    public void onNotifyClicked() {
        /*
        Intent intent = new Intent(getContext(),NotificationService.class);
        getActivity().startService(intent);
        */

        //alarm manager may help us schedule repeating tasks

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), NotificationService.class);
        PendingIntent pi = PendingIntent.getService(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //alarmManager.set();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10*1000 , 60000 , pi);
        //alarmManager.setInexactRepeating();
        //alarmManager.setExact();
        //alarmManager.setAndAllowWhileIdle();
    }
}
