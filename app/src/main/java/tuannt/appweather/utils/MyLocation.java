package tuannt.appweather.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import tuannt.appweather.R;
import tuannt.appweather.activities.FirstActivity;
import tuannt.appweather.activities.SlideActivity;
import tuannt.appweather.models.User;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.tasks.GetIDCity;

/**
 * Created by tuan on 21/02/2016.
 */
public class MyLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Activity context;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    protected LocationManager locationManager;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public MyLocation(Activity context){
        this.context = context;
        init();
    }

    private void init(){
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    public void connectApi(){
        mGoogleApiClient.connect();
    }

    public void disconnectApi(com.google.android.gms.location.LocationListener locationListener){
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location){
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

//        Toast.makeText(context, "" + currentLatitude + "\n" + currentLongitude, Toast.LENGTH_LONG).show();

        try{
            new GetCityWeather(context).execute(API.getApiCurrentCityByCoordinate
                (String.valueOf(currentLatitude), String.valueOf(currentLongitude)));
        }catch (Exception e){
            MyNotification.showDialogNotification(context);
        }
//        Log.i("Tuan", "Tuan: " + currentLatitude);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Variables.isFirstStartApp = true;
                Intent intent;
                DBAdapter dbAdapter = MyMethods.openDB(context, "IDCity");
                if(dbAdapter.getAllRow().getCount() < 1) {
                    new GetIDCity(context).execute("http://content.amobi.vn/api/thoitiet/city-id");
                }else{
                    dbAdapter.close();
                    dbAdapter = MyMethods.openDB(context, "Favorite");
                    Variables.isFirstStartApp = false;
                    if(dbAdapter.getAllRow().getCount() < 1){
                        intent = new Intent(context, FirstActivity.class);
                        context.startActivity(intent);
                        context.finish();
                    }else{
                        Cursor cursor = dbAdapter.getAllRow();
                        cursor.moveToPosition(0);
                        new GetCityWeather(context).execute(API.getApiCurrentCityById(cursor.getString(0)));
                    }

                }
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public boolean checkGPSAndNetwork(){
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled && !isNetworkEnabled){
            return false;
        }else{
            return true;
        }
    }

}
