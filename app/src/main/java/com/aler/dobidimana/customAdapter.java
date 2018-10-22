package com.aler.dobidimana;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class customAdapter extends ArrayAdapter<result.GooglePlace> {

    private Context context;
    private ArrayList<result.GooglePlace> venuesList;
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
        View view = inflater.inflate(R.layout.row_layout, null);


        TextView title = (TextView) view.findViewById(R.id.listText);
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

