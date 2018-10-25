package com.aler.dobidimana;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings({"ALL", "CanBeFinal"})
public class customAdapter extends ArrayAdapter<result.GooglePlace> {

    private Context context;
    @SuppressWarnings("CanBeFinal")
    private ArrayList<result.GooglePlace> venuesList;
    @SuppressWarnings("CanBeFinal")
    public Float[] distanceResult;

    public customAdapter(Context context, int resource, ArrayList objects,Float[] distance) {

        super(context, resource, objects);
       this.distanceResult=distance;
        this.context = context;
        this.venuesList = objects;
    }
    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        result.GooglePlace venueList = venuesList.get(position);

        detailPage diPage=new detailPage();
        String distanceFinalResult=diPage.distanceValue(distanceResult[position]);
        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.row_layout, null);


        TextView title = view.findViewById(R.id.listText);
        TextView address = (TextView) view.findViewById(R.id.addressText);
        TextView openNow = (TextView) view.findViewById(R.id.result_open_text);
        TextView distance = (TextView) view.findViewById(R.id.result_distance_text);

        title.setText(venueList.getName());
        address.setText(venueList.getAddress());
        openNow.setText(venueList.getOpenNow());
        distance.setText(distanceFinalResult);

        return view;
    }
}

