package com.aler.dobidimana;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

@SuppressWarnings("ALL")
public class detailPage extends AppCompatActivity {
double latFinal;
double longFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String unit="m";
        //insert your API key here --
        String GOOGLE_KEY = getString(R.string.place_api_key);
        setContentView(R.layout.activity_detail_page);
        Bundle b=getIntent().getExtras();
        String nameDetail=b.getString("nameDetail");
        String addressDetail=b.getString("addressDetail");
        String photoIDDetail=b.getString("photoIDDetail");
        String latitudeDetail=b.getString("latitudeDetail");
        String longitudeDetail=b.getString("longitudeDetail");
        Float distanceDetail=b.getFloat("distanceDetail");
        String openDetail=b.getString("openDetail");

        latFinal=Double.valueOf(latitudeDetail);
        longFinal=Double.valueOf(longitudeDetail);
        String distanceFinal=distanceValue(distanceDetail);
        MobileAds.initialize(this,getString(R.string.admob_key));


//        if (distanceDetail.compareTo(999.99f)>0) {
//            distanceFinal = distanceFinal/1000;
//            unit="km";
//        }
//        else {
        //    distanceFinal=Math.round(distanceDetail);
      //change unit if >1000m
//        int compareResult=Float.compare(distanceDetail,999.99f);
//
//          if (compareResult>0){
//                    distanceDetail=distanceDetail/1000;
//            distanceFinal=String.format("%.2f",distanceDetail);
//                    unit="km";
//        }
//        else if(compareResult<0)
//        {
//          distanceFinal=String.valueOf(Math.round(distanceDetail));
//        }
//
//        else
//            {
//                distanceFinal=String.valueOf(Math.round(distanceDetail));
//            }


        TextView nameText= findViewById(R.id.name_Text);
        TextView addressText= findViewById(R.id.addree_Text);
        TextView distanceText= findViewById(R.id.distance);
        TextView openText= findViewById(R.id.open_now_text);

        nameText.setText(nameDetail);
        addressText.setText(addressDetail);
        distanceText.setText(distanceFinal);
        openText.setText(openDetail);



//        https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY



        String imgURL;
                  imgURL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoIDDetail+"&key="+ GOOGLE_KEY;
        ImageView imageDisplay= findViewById(R.id.imageView);
        TextView nopictureText=findViewById(R.id.no_picture_detail_text);
        AdView mAdViewAltDetail = findViewById(R.id.adViewAltDetail);
    if (photoIDDetail.contentEquals("")){

       nopictureText.setVisibility(View.VISIBLE);
       imageDisplay.setVisibility(View.GONE);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewAltDetail.loadAd(adRequest);
       mAdViewAltDetail.setVisibility(View.VISIBLE);

        imageDisplay.setImageResource(android.R.drawable.presence_busy);
    }
//        add code to change pictue if no photo reference present
    else{
        nopictureText.setVisibility(View.GONE);
        mAdViewAltDetail.setVisibility(View.GONE);


        Picasso.get().load(imgURL).fit().into(imageDisplay);

    }

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




    public void goAction(View view)
 {
     Uri gmmIntentUri = Uri.parse("google.navigation:q="+latFinal+","+longFinal);
     Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
     mapIntent.setPackage("com.google.android.apps.maps");
     startActivity(mapIntent);

 }

@SuppressLint("DefaultLocale")
public String distanceValue(Float distance){
@SuppressWarnings("UnusedAssignment") String distanceAfter=" ";
    int compareResult=Float.compare(distance,999.99f);

    if (compareResult>0){
        distance=distance/1000;
        distanceAfter=String.format("%.2f",distance)+"km";
       // unit="km";
    }
    else if(compareResult<0)
    {
        distanceAfter=String.valueOf(Math.round(distance))+"m";
    }

    else
    {
        distanceAfter=String.valueOf(Math.round(distance))+"m";
    }

return distanceAfter;
    }


}
