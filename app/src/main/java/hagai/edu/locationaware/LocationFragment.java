package hagai.edu.locationaware;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * [
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    private static final int RC_LOCATION = 11;
    FusedLocationProviderClient locationClient;
    LocationCallback callback;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        locationClient = new FusedLocationProviderClient(getContext());
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                Location lastLocation = locationResult.getLastLocation();

                updateUI(locations.get(0));
            }
        };
        requestLocationUpdates();


//        lastKnownLocation();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void updateUI(Location location) {
        tvLocation.setText(location.toString());

//internet permission
        Geocoder geocoder = new Geocoder(getContext());
        try {
            Address address = geocoder.
                    getFromLocation(location.getLatitude(), location.getLongitude(), 1).
                    get(0);
            StringBuilder result = new StringBuilder();

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                result.append(address.getAddressLine(i)).append("\n");

            }

            tvLocation.setText(location + "\n" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //todo: 1) request last known location:
    //2) request fine location updates-> start the GPS For me
    //3) given a latLng-> address and vice versa
    //4) know if the user stepped into a certain location()


    //last known location may be null
    private void lastKnownLocation() {
        if (!checkLocationPermission()) return;
        locationClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        Toast.makeText(getContext(), location.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void requestLocationUpdates() {
        //Request ->
        LocationRequest request = new LocationRequest();
        //for my app
        request.setInterval(2000);//two seconds
        //if other app already got the location, let me know about it
        request.setFastestInterval(500);
        //PRIORITY _ HIGH_ACCURACY==GPS
        //REQUEST_LOW_POWER ==CELLULAR
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        request.setSmallestDisplacement(1000);//1000 meter
//        request.setNumUpdates(60*1000);// one minute
//        locationClient.requestLocationUpdates();

        if ( !checkLocationPermission())return;

        locationClient.requestLocationUpdates(request,callback, null );
    }

    private boolean checkLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        //If No Permission-> Request the permission and return false.
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), permissions, RC_LOCATION);
            return false;
        }
        return true;//return true if we have a permission
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //noinspection MissingPermission
            requestLocationUpdates();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
