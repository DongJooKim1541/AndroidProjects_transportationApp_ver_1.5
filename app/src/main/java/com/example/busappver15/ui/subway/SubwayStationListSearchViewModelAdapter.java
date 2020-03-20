package com.example.busappver15.ui.subway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.busappver15.R;

import java.util.ArrayList;

public class SubwayStationListSearchViewModelAdapter extends ArrayAdapter<SubwayItems> implements AdapterView.OnItemClickListener {

    private Context context;
    private ArrayList<SubwayItems> list;
    private SubwayItems si;
    private int resource;
    private ListView myListView;

    public SubwayStationListSearchViewModelAdapter(Context context, int resource, ArrayList<SubwayItems> objects, ListView myListView) {
        super(context,resource,objects);
        list=objects;
        this.context=context;
        this.resource=resource;
        this.myListView=myListView;
        this.myListView.setOnItemClickListener(this);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView=linf.inflate(resource,null);

        si=list.get(position);

        if(si!=null){
            TextView subwayRouteName=(TextView) convertView.findViewById(R.id.subwayRouteName);
            TextView subwayStationName=(TextView) convertView.findViewById(R.id.subwayStationName);
            ImageView imageView=(ImageView)convertView.findViewById(R.id.stationImageView);

            if(subwayRouteName!=null){
                String routeName=si.getSubwayRouteName();
                if(routeName.contains("1호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_1);
                }
                else if(routeName.contains("2호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_2);
                }
                else if(routeName.contains("3호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_3);
                }
                else if(routeName.contains("4호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_4);
                }
                else if(routeName.contains("5호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_5);
                }
                else if(routeName.contains("6호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_6);
                }
                else if(routeName.contains("7호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_7);
                }
                else if(routeName.contains("8호선")){
                   imageView.setBackgroundResource(R.drawable.subwayimage_8);
                }
                else if(routeName.contains("9호선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_9);
                }
                else if(routeName.contains("신분당선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_sinbundang);
                }
                else if(routeName.contains("분당선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_bundang);
                }
                else if(routeName.contains("경의중앙선") || routeName.contains("경춘선")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_gyungeui);
                }
                else if(routeName.contains("공항철도")){
                    imageView.setBackgroundResource(R.drawable.subwayimage_airport);
                }
                else {
                    imageView.setBackgroundResource(R.drawable.subwayimage_else);
                }

                subwayRouteName.setText(si.getSubwayRouteName());
            }
            if(subwayStationName!=null){
                subwayStationName.setText(si.getSubwayStationName());
            }
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
