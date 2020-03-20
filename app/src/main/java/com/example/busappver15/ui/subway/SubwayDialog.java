package com.example.busappver15.ui.subway;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.busappver15.R;

public class SubwayDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private SubwayStationListSearchViewModelAdapter adapter;
    private int position;
    private String choice;
    private  SubwayItems subwayItems;
    private Intent i;
    private RadioButton findBusRouteButton,findFacilityButton,findSubwayTimeButton;
    private Button yesButton,noButton;

    public SubwayDialog(Context context, SubwayStationListSearchViewModelAdapter adapter,int position){
        super(context);
        mContext=context;
        this.adapter=adapter;
        this.position=position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subway_dialog_layout);

        getData();

        findBusRouteButton=(RadioButton)findViewById(R.id.findBusRouteButton);
        findFacilityButton=(RadioButton)findViewById(R.id.findFacilityButton);
        findSubwayTimeButton=(RadioButton)findViewById(R.id.findSubwayTimeButton);
        android.util.Log.d("KDJ","--------radioGroup 생성--------");

        yesButton=(Button)findViewById(R.id.yesButton);
        noButton=(Button)findViewById(R.id.noButton);

        findBusRouteButton.setOnClickListener(this);
        findFacilityButton.setOnClickListener(this);
        findSubwayTimeButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
    }

    private void getData(){
        subwayItems=new SubwayItems();
        subwayItems=(SubwayItems)adapter.getItem(position);
    }

    @Override
    public void onClick(View v) {
        choice=new String();
        switch(v.getId()){
            case R.id.findBusRouteButton:
                i=new Intent(getContext(), Subway_findBusRouteActivity.class);
                i.putExtra("subwayStationId",subwayItems.getSubwayStationId());
                break;
            case R.id.findFacilityButton:
                i=new Intent(getContext(), Subway_findfacilityActivity.class);
                i.putExtra("subwayStationId",subwayItems.getSubwayStationId());
                break;
            case R.id.findSubwayTimeButton:
                i=new Intent(getContext(), Subway_findTimeTableActivity.class);
                i.putExtra("subwayStationId",subwayItems.getSubwayStationId());
                i.putExtra("subwayStationName",subwayItems.getSubwayStationName());
                i.putExtra("subwayRouteName",subwayItems.getSubwayRouteName());
                break;
            case R.id.yesButton:
                if(findBusRouteButton.isChecked()==true || findFacilityButton.isChecked()==true || findSubwayTimeButton.isChecked()==true){
                    mContext.startActivity(i);
                    dismiss();
                }
                else{
                    Toast.makeText(mContext,"항목을 체크하여 주십시오!!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.noButton:
                dismiss();
                break;

        }
    }
}
