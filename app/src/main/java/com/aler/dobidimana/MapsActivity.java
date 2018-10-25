package com.aler.dobidimana;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.String;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
@SuppressWarnings("unused")
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,LocationListener{


    private String bestProvider;

   private LocationManager mNoll;
    // --Commented out by Inspection (15/10/2018 11:48 AM):public int searchRadius;



    private TextView tempR;
    private TextView statusBottom;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;

// --Commented out by Inspection START (15/10/2018 11:40 AM):
// --Commented out by Inspection START (15/10/2018 11:44 AM):
////    // The entry points to the Places API.
////    private GeoDataClient mGeoDataClient;
//// --Commented out by Inspection STOP (15/10/2018 11:40 AM)
//    private PlaceDetectionClient mPlaceDetectionClient;
// --Commented out by Inspection STOP (15/10/2018 11:44 AM)

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

// --Commented out by Inspection START (15/10/2018 11:51 AM):
//    // Used for selecting the current place.
//    private static final int M_MAX_ENTRIES = 20;
// --Commented out by Inspection STOP (15/10/2018 11:51 AM)
//    private String[] mLikelyPlaceNames;
//    private String[] mLikelyPlaceAddresses;
//    private String[] mLikelyPlaceAttributions;
//    private LatLng[] mLikelyPlaceLatLngs;
    // --Commented out by Inspection (15/10/2018 11:44 AM):private String[] mLikelyPlaceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            //noinspection unused
            CameraPosition mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);




        // Construct a GeoDataClient.
   //     mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
     //   mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
//notification
        tempR= findViewById(R.id.tempResult);
        tempR.setText(R.string.get_device_location);
        statusBottom=findViewById(R.id.status_bottom);
        statusBottom.setText(R.string.wait_label);
        getDeviceLocation();






    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_dobi, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_dobi) {
            searchDobi();
        }
        if (item.getItemId()==R.id.about){
            Intent aboutScreen=new Intent (this,about.class);
            startActivity(aboutScreen);
        }

        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

            //tempR.setText("Device Location Set");

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getDeviceLocation();

                Double setLat = mLastKnownLocation.getLatitude();
                tempR = findViewById(R.id.tempResult);

                tempR.setText(R.string.device_location_updated);

                statusBottom=findViewById(R.id.status_bottom);
                statusBottom.setText(R.string.search_now_text);

                return false;
            }
        });





    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                mNoll=(LocationManager) getSystemService(LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                bestProvider = String.valueOf(mNoll.getBestProvider(criteria, true));
                Location location=mNoll.getLastKnownLocation(bestProvider);
                if (location !=null) {


                    locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = task.getResult();
                                tempR.setText(R.string.device_location_updated);
                                statusBottom=findViewById(R.id.status_bottom);
                                statusBottom.setText(R.string.search_now_text);
                                //NULL location handler?


//                                mLastKnownLocation.setLatitude(mDefaultLocation.latitude);
//                                locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
//                                PendingIntent mIntent=PendingIntent.getService()
//                                Criteria criteria = new Criteria();

//                                String bestProvider=String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//                                locationManager.requestLocationUpdates(bestProvider, 1000, 0,);
                                    //locationManager=(LocationManager) MapsActivity.getSystemService(MapsActivity.LOCATION_SERVICE);
                                    // mLastKnownLocation.

                                //
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
                else{
                   // tempR.setText("else true");
                    mNoll.requestLocationUpdates(bestProvider, 1000, 0, this);
               //     while (mLastKnownLocation==null){
                    mMap.setMyLocationEnabled(false);
                    tempR.setText(R.string.wait_for_GPS);
                    textBlinkStart(tempR);
                    statusBottom=findViewById(R.id.status_bottom);
                    statusBottom.setText(R.string.wait_label);
                 //   }
                //    mLastKnownLocation=mNoll.getLastKnownLocation(bestProvider);
//                    getDeviceLocation();
                }


            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

// --Commented out by Inspection START (15/10/2018 11:51 AM):
//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//          //try to add filter- not wrking
//         //           List<String> filters = new ArrayList<>();
//           // filters.add(String.valueOf(Place.TYPE_LAUNDRY));
//             //       PlaceFilter filterDobi;
//            //filterDobi = new PlaceFilter(false,filters);
//
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace (null);
//            placeResult.addOnCompleteListener
//                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                               // String strResult;
//                               // strResult = (String) likelyPlaces.getCount();
//                                TextView tempResult=(TextView)findViewById(R.id.tempResult);
//                                tempResult.setText(String.valueOf(likelyPlaces.getCount()));
//
//
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                   count = likelyPlaces.getCount();
//                                } else {
//                                   count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//                                mLikelyPlaceCategory = new String[count];
//
//                               for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                           .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                   i++;
//                                   if (i > (count - 1)) {
//                                        break;
//                                   }
//                               }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                               likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
// --Commented out by Inspection STOP (15/10/2018 11:51 AM)

// --Commented out by Inspection START (15/10/2018 11:52 AM):
//    /**
//     * Displays a form allowing the user to select a place from a list of likely places.
//     */
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));
//            }
//        };
//
//        // Display the dialog.
//        //noinspection unused
////        AlertDialog dialog = new AlertDialog.Builder(this)
////                .setTitle(R.string.pick_place)
////                .setItems(mLikelyPlaceNames, listener)
////                .show();
//    }
// --Commented out by Inspection STOP (15/10/2018 11:52 AM)

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        textBlinkStop(tempR);
        tempR.setText(R.string.device_location_updated);
        statusBottom=findViewById(R.id.status_bottom);
        statusBottom.setText(R.string.search_now_text);
        mLastKnownLocation=mNoll.getLastKnownLocation(bestProvider);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLastKnownLocation.getLatitude(),
                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        mMap.setMyLocationEnabled(true);
        mNoll.removeUpdates(this);

    }

    private void textBlinkStart(TextView toBlink) {


        Animation anim = new AlphaAnimation(0.0f, 1.0f);

        anim.setDuration(100); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        toBlink.startAnimation(anim);
    }


    private void textBlinkStop(TextView toBlink){

            toBlink.clearAnimation(); // cancel blink animation
            toBlink.setAlpha(1.0f); // restore original alpha
        }


    public void moveAction(View View){
        searchDobi();
    }

    private void searchDobi()
    {
        getDeviceLocation();
        Intent move = new Intent(this, result.class);
        Bundle b = new Bundle();
        getDeviceLocation();
        Double tempCurLat=mLastKnownLocation.getLatitude();
        Double tempCurLong=mLastKnownLocation.getLongitude();
        // tempR=(TextView)findViewById(R.id.tempResult);
        // tempR.setText(tempCurLat.toString());
        b.putDouble("curLat",tempCurLat);
        b.putDouble("curLong",tempCurLong);
        move.putExtras(b);
        startActivity(move);
    }





}
