package com.example.busappver15.ui.bus;

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

public class ListOfBusLocationsByRouteViewModelAdapter extends ArrayAdapter<BusItems> implements AdapterView.OnItemClickListener {

    private Context context;
    private ArrayList<BusItems> list;
    private BusItems bi=new BusItems();
    private int resource;
    private ListView myListView;

    public ListOfBusLocationsByRouteViewModelAdapter(Context context,int resource,ArrayList<BusItems> objects,ListView myListView){
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
        bi=list.get(position);

        if(bi!=null) {
            TextView nodeNm = (TextView) convertView.findViewById(R.id.nodeNm);
            TextView vehicleNo = (TextView) convertView.findViewById(R.id.vehicleNo);

            if (nodeNm != null) {
                nodeNm.setText("정류소: " + bi.getNodeNm());
            }
            if (vehicleNo != null) {
                vehicleNo.setText("차량 번호: " + bi.getVehicleNo());
            }
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
