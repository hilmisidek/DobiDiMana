package com.aler.dobidimana;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class detailPage extends AppCompatActivity {
double latFinal;
double longFinal;
//insert your API key here
    private String GOOGLE_KEY="your key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GOOGLE_KEY=getString(R.string.google_maps_key);
        setContentView(R.layout.activity_detail_page);
        Bundle b=getIntent().getExtras();
        String nameDetail=b.getString("nameDetail");
        String addressDetail=b.getString("addressDetail");
        String photoIDDetail=b.getString("photoIDDetail");
        String latitudeDetail=b.getString("latitudeDetail");
        String longitudeDetail=b.getString("longitudeDetail");
        Float distanceDetail=b.getFloat("distanceDetail");
        latFinal=Double.valueOf(latitudeDetail);
        longFinal=Double.valueOf(longitudeDetail);
        String distanceFinal="";
        String unit="m";

//        if (distanceDetail.compareTo(999.99f)>0) {
//            distanceFinal = distanceFinal/1000;
//            unit="km";
//        }
//        else {
        //    distanceFinal=Math.round(distanceDetail);
      //change unit if >1000m
        int compareResult=Float.compare(distanceDetail,999.99f);

          if (compareResult>0){
                    distanceDetail=distanceDetail/1000;
            distanceFinal=String.format("%.2f",distanceDetail);
                    unit="km";
        }
        else if(compareResult<0)
        {
          distanceFinal=String.valueOf(Math.round(distanceDetail));
        }

        else
            {
                distanceFinal=String.valueOf(Math.round(distanceDetail));
            }


        TextView nameText=(TextView)findViewById(R.id.name_Text);
        TextView addressText=(TextView)findViewById(R.id.addree_Text);
        nameText.setText(nameDetail);
        addressText.setText(addressDetail+","+latitudeDetail+","+longitudeDetail+"\n Distance:"+distanceFinal+" "+unit);


//        https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY



        String imgURL;
                  imgURL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoIDDetail+"&key="+GOOGLE_KEY;
        ImageView imageDisplay=(ImageView)findViewById(R.id.imageView);

    if (photoIDDetail.contentEquals("")){imageDisplay.setImageResource(android.R.drawable.presence_busy);}
//        add code to change pictue if no photo reference present
    else{Picasso.get().load(imgURL).fit().into(imageDisplay);}


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
        //     if (item.getItemId() == R.id.search_dobi) {
        //     searchDobi();
        //   }
        return true;
    }




    public void goAction(View view)
 {
     Uri gmmIntentUri = Uri.parse("google.navigation:q="+latFinal+","+longFinal);
     Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
     mapIntent.setPackage("com.google.android.apps.maps");
     startActivity(mapIntent);

 }




}
