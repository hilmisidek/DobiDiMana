package com.aler.dobidimana;




import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;







//import org.apache.http.HttpResponse;

//import org.apache.http.client.HttpClient;

//import org.apache.http.client.methods.HttpGet;

//import org.apache.http.impl.client.DefaultHttpClient;

//import org.apache.http.util.ByteArrayBuffer;



import org.json.JSONArray;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


@SuppressWarnings({"ALL", "unused"})
public class result extends AppCompatActivity {
    String value="callback";

    private AdView mAdViewAlt;
    ArrayList<GooglePlace> venuesList;
    String latitude = "5.282113";
    String longtitude = "103.162515";
    private int PROXIMITY_RADIUS = 5000;
   // private static final String GOOGLE_KEY=Your API key;
    public String GOOGLE_KEY_Lorry="lori";
   private String GOOGLE_KEY="your key";
    ArrayAdapter<String> myAdapter;
    ArrayAdapter<String> AddrAdapter;

    @SuppressWarnings("WeakerAccess")
    ListView theList;
   // ArrayAdapter<String> myAdapter;
//String listResult[]={"satu","dua","tiga"};
//start JSON query example
    @Override
    protected void onCreate(Bundle savedInstanceState) {



       // Double curLati=b.getDouble("currLat");
       // Double curLongi=b.getDouble("currLong");
      //  latitude=curLati.toString();
      //  longtitude=curLongi.toString();
        super.onCreate(savedInstanceState);


        GOOGLE_KEY=getString(R.string.place_api_key);

       //firebaseRead();


   // GOOGLE_KEY=value;

      // GOOGLE_KEY=value;
         Bundle b=getIntent().getExtras();


         Double curLati=b.getDouble("curLat");
          Double curLongi=b.getDouble("curLong");
         latitude=curLati.toString();
         longtitude=curLongi.toString();

        MobileAds.initialize(this,getString(R.string.admob_key));


      //  theList.setVisibility(View.VISIBLE);

        setContentView(R.layout.activity_result);

      //  final TextView keyView=(TextView)findViewById(R.id.tempKEY);

//        readData(new MyCallback() {
//            @Override
//            public void onCallback(final String value) {
//                Log.d("TAG", value);
//                GOOGLE_KEY=value;
//                keyView.setText(GOOGLE_KEY);
//                //   GOOGLE_KEY = value;
//            }
//        });


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


//        TextView latiV=(TextView)findViewById(R.id.latiText);
//        latiV.setText(latitude);
//        TextView longiV=(TextView)findViewById(R.id.longiText);
//        longiV.setText(longtitude);
        new googleplaces().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.about_single){
            Intent aboutScreen=new Intent (this,about.class);
            startActivity(aboutScreen);
        }
        return true;
    }








    @SuppressLint("StaticFieldLeak")
    private class googleplaces extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longtitude + "&rankby=distance&type=laundry&sensor=true&key=" + GOOGLE_KEY);
//            temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longtitude + "&radius="+radiusSet+"&type=laundry&sensor=true&key=" + GOOGLE_KEY);


            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longtitude + "&rankby=distance&type=laundry&sensor=true&key=" + GOOGLE_KEY);
//            System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longtitude + "&radius="+radiusSet+"&type=laundry&sensor=true&key=" + GOOGLE_KEY);

            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(final String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right

                // parse Google places search result
//  add distance calculation
//                LatLng currlatlong=new LatLng(Double.valueOf(latitude),Double.valueOf(longtitude));
                Location currLoc = new Location("current");
                currLoc.setLatitude(Double.valueOf(latitude));
                currLoc.setLongitude(Double.valueOf(longtitude));

                Location destiny=new Location("destination");



                venuesList = parseGoogleParse(temp);

                //add distance array

                final Float[] distance=new Float[venuesList.size()];
                //
//                List<String> listTitle = new ArrayList<String>();
//                List<String> listAddress = new ArrayList<>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
// calculate distance
                    destiny.setLatitude(Double.valueOf(venuesList.get(i).getLatResult()));
                    destiny.setLongitude(Double.valueOf(venuesList.get(i).getLongResult()));
                    distance[i]=currLoc.distanceTo(destiny);
//                    listTitle.add(i, venuesList.get(i).getName() + "\nOpen Now: " +
//                            venuesList.get(i).getOpenNow() +"\nPhoto ID=" + venuesList.get(i).getPhotoID() +
//                            "\n(" + venuesList.get(i).getCategory() + ")"+"\nlat="+
//                            venuesList.get(i).getLatResult()+"\nlong="+venuesList.get(i).getLongResult()+"\ndistance="+
//                            distance[i]);
//                    //listAddress.add(i,venuesList.get(i).getAddress());
                }

                // set the results to the list
                // and show them in the xml
//                myAdapter = new ArrayAdapter<String>(result.this, R.layout.row_layout, R.id.listText, listTitle);
//            myAdapter= new customAdapter(getApplicationContext(),listTitle,listAddress);
                //    AddrAdapter = new ArrayAdapter<String>(result.this,R.layout.row_layout,R.id.addressText,listAddress);
ArrayAdapter<GooglePlace> adapter = new customAdapter(getApplicationContext(),0,venuesList,distance);


               theList = findViewById(android.R.id.list);
               theList.setAdapter(adapter);
                mAdViewAlt=findViewById(R.id.adViewAlt);

                mAdViewAlt.setVisibility(View.GONE);
               theList.setVisibility(View.VISIBLE);

//               separate view trial





//



             //   theList.setAdapter(myAdapter,AddrAdapter);
               //  start
               theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       String stringText;
                       @SuppressWarnings("UnnecessaryLocalVariable") int posisi=position;
                       String namaResult=venuesList.get(posisi).getName();
                       String addressResult = venuesList.get(posisi).getAddress();
                       String photoIDResult=venuesList.get(posisi).getPhotoID();
                       String latResult=venuesList.get(posisi).getLatResult();
                       String longResult=venuesList.get(posisi).getLongResult();
                       Float distanceResult=distance[posisi];
                       String openResult=venuesList.get(posisi).getOpenNow();

                       //in normal case
                      // stringText= ((TextView)view).getText().toString();

                       //in case if listview has separate item layout
                       TextView textview= view.findViewById(R.id.listText);
                       //noinspection UnusedAssignment
                       stringText=textview.getText().toString();

//                       TextView DATAtemp=(TextView)findViewById(R.id.tempData);
//                       DATAtemp.setText(namaResult);

                       Intent clickAction=new Intent(result.this,detailPage.class);
                       Bundle detail=new Bundle();
                       detail.putString("nameDetail",namaResult);
                       detail.putString("addressDetail",addressResult);
                       detail.putString("photoIDDetail",photoIDResult);
                       detail.putString("latitudeDetail",latResult);
                       detail.putString("longitudeDetail",longResult);
                       detail.putFloat("distanceDetail",distanceResult);
                       detail.putString("openDetail",openResult);

                       clickAction.putExtras(detail);
                       startActivity(clickAction);
                       //show selected

                       //Toast.makeText(result.this, stringText, Toast.LENGTH_LONG).show();
                        }
               });
//end
            }
        if (venuesList.size()==0){
                TextView searchText = findViewById(R.id.search_result_text);

                searchText.setText(R.string.no_result_text);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdViewAlt.loadAd(adRequest);
            theList.setVisibility(View.GONE);
            mAdViewAlt.setVisibility(View.VISIBLE);




        }

        }
    }

    @SuppressWarnings("StringBufferMayBeStringBuilder")
    public static String makeCall(String url) {
       //String placesBuilder="null";
        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";
        StringBuilder placesBuilder = new StringBuilder();
        // instanciate an HttpClient
        try {

            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                @SuppressWarnings("UnusedAssignment") BufferedReader reader = null;

                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) {
                    return "";
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    //noinspection StringConcatenationInsideStringBufferAppend
                    placesBuilder.append(line + "\n");
                }

                if (placesBuilder.length() == 0) {
                    return "";
                }

                Log.d("test", placesBuilder.toString());
            }
            else {
                Log.i("test", "Unsuccessful HTTP Response Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            Log.e("test", "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e("test", "Error connecting to Places API", e);
        }
        return placesBuilder.toString();
    }



    @SuppressWarnings("Convert2Diamond")
    private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

        //noinspection Convert2Diamond
        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
//set address
                        if (jsonArray.getJSONObject(i).has("vicinity")) {
                           poi.setAdddress(jsonArray.getJSONObject(i).optString("vicinity"));

                        }
//set photoID
                        if (jsonArray.getJSONObject(i).has("photos")) {
                            JSONArray photoArray=jsonArray.getJSONObject(i).getJSONArray("photos");
                            String photoIDtemp="";
                            for (int h=0;h<photoArray.length();h++){
                             if (photoArray.getJSONObject(h).has("photo_reference")){photoIDtemp=photoArray.getJSONObject(h).optString("photo_reference");}
                            }
                            poi.setPhotoID(photoIDtemp);
                        }

// set lat and long
                        if (jsonArray.getJSONObject(i).has("geometry")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location"))
                            {
                                poi.setLatResult(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lat"));
                                poi.setLongResult(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lng"));
                            }
                        }



                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<GooglePlace>();
        }
        return temp;

    }


    //
    @SuppressWarnings("WeakerAccess")
    public static class GooglePlace {
        private String name;
        private String category;
        private String rating;
        private String open;
//     add address;
        private String address;
//        add photoID
        private String photoID;
        private String latResult;  //add latitude and longtitude to pass
        private String longResult;

        public GooglePlace() {
            this.name = "";
            this.rating = "";
            this.open = "";
            this.setCategory("");
            this.address=("");
            this.photoID=("");
            this.latResult=("");
            this.longResult=("");
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        @SuppressWarnings("unused")
        public String getRating() {
            return rating;
        }

        public void setOpenNow(String open) {
            this.open = open;
        }

        public String getOpenNow() {
            return open;
        }
//    add get and set address
        public void setAdddress(String address){this.address=address;}

        public String getAddress() {return address;}
//

//        add get and set photo id
        public void setPhotoID(String phID){this.photoID=phID;}
        @SuppressWarnings("WeakerAccess")
        public String getPhotoID(){return photoID;}

//        add get and set lat and long
        public void setLatResult(String latRslt){this.latResult=latRslt;}
        public String getLatResult(){return latResult;}

        public void setLongResult(String longRslt){this.longResult=longRslt;}
        public String getLongResult(){return longResult;}



    }





//    public void readData(final MyCallback myCallback) {
//
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        // [START single_value_read]
//
//        mDatabase.child("place_key").addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user value
//
//                       String value = dataSnapshot.getValue().toString();
//
//                       myCallback.onCallback(value);
//
//                        GOOGLE_KEY_Lorry=value;
//
//                        // [END_EXCLUDE]
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e("error", "getUser:onCancelled", databaseError.toException());
//                        // [START_EXCLUDE]
//                    }
//                });
//
//        // [END single_value_read]
//    }
//
//    public interface MyCallback {
//        void onCallback(String value);
//            }

//    public void setKey(String keyRead){
//        this.GOOGLE_KEY_Lorry=keyRead;
//
//    }
//
//    public String getKey(){return GOOGLE_KEY_Lorry;}

//END
 //       resultList=(ListView)findViewById(R.id.listresult);
   //     ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_result, R.id.listresult, listResult);
     //   resultList.setAdapter(arrayAdgoogle.maps.places.PlacesServiceapter);


}
