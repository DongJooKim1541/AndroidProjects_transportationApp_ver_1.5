package com.example.busappver15.ui.subway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.busappver15.R;

import java.util.ArrayList;

public class Subway_findFacilityViewModelAdapter extends ArrayAdapter<SubwayItems> implements AdapterView.OnItemClickListener{

    private Context context;
    private ArrayList<SubwayItems> list;
    private SubwayItems si=new SubwayItems();
    private int resource;
    private ListView myListView;


    public Subway_findFacilityViewModelAdapter(@NonNull Context context, int resource, ArrayList<SubwayItems> objects, ListView myListView) {
        super(context, resource, objects);
        list=objects;
        this.context=context;
        this.resource=resource;
        this.myListView=myListView;
        this.myListView.setOnItemClickListener(this);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView=linf.inflate(resource,null);

        si=list.get(position);

        if(si!=null){
            TextView dirDesc=(TextView) convertView.findViewById(R.id.dirDesc);
            TextView exitNo=(TextView) convertView.findViewById(R.id.exitNo);

            if(dirDesc!=null){
                dirDesc.setText(" 시설명: "+si.getDirDesc());
            }
            if(exitNo!=null){
                exitNo.setText("출구: "+si.getExitNo()+"번 출구");
            }
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
