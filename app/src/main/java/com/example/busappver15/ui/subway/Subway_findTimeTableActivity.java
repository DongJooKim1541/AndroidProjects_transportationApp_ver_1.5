package com.example.busappver15.ui.subway;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busappver15.R;

import java.util.ArrayList;

public class Subway_findTimeTableActivity extends AppCompatActivity {

    private TextView subwayStationNm;
    private ListView mySubwayListView;
    private EditText dailyTypeCode,upDownTypeCode;
    private Button searchTimeTableBtn;
    private Subway_findTimeTableParser parser;
    private ArrayList<SubwayItems> list;
    private Subway_findTimeTableViewModelAdapter adapter;
    private String subwayStationId;
    private String subwayStationName;
    private String subwayRouteName;
    private String dailyTypeCodeString;
    private String upDownTypeCodeString;
    private ImageView imageView;
    private boolean isFilled=false;

    int pageNo;   //검색을 시작할 page 번호
    ProgressDialog dialog_progress; //로딩을 위한 다이얼로그

    //스크롤링을 통한 추가 로드를 위해 필요한 변수
    LayoutInflater mInflater;
    boolean mLockListView=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_findtimetable);

        subwayStationNm=(TextView)findViewById(R.id.subwayStationNm);
        dailyTypeCode=(EditText)findViewById(R.id.dailyTypeCode);
        upDownTypeCode=(EditText)findViewById(R.id.upDownTypeCode);
        searchTimeTableBtn=(Button)findViewById(R.id.searchTimeTableBtn);
        imageView=(ImageView)findViewById(R.id.timetableImageView);

        dialog_progress = new ProgressDialog(this);
        dialog_progress.setMessage("Loading.....");

        Intent i=getIntent();
        subwayStationId=i.getExtras().getString("subwayStationId");
        subwayStationName=i.getExtras().getString("subwayStationName");
        subwayRouteName=i.getExtras().getString("subwayRouteName");

        parser=new Subway_findTimeTableParser();

        searchTimeTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                mInputMethodManager.hideSoftInputFromWindow(dailyTypeCode.getWindowToken(), 0);

                if(subwayStationId!=null && dailyTypeCode.getText().toString().trim().length()>0 && upDownTypeCode.getText().toString().trim().length()>0){

                    subwayStationNm.setText(subwayStationName);

                    if(subwayRouteName!=null) {
                        if (subwayRouteName.contains("1호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_1);
                        }
                        else if (subwayRouteName.contains("2호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_2);
                        }
                        else if (subwayRouteName.contains("3호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_3);
                        }
                        else if (subwayRouteName.contains("4호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_4);
                        }
                        else if (subwayRouteName.contains("5호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_5);
                        }
                        else if (subwayRouteName.contains("6호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_6);
                        }
                        else if (subwayRouteName.contains("7호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_7);
                        }
                        else if (subwayRouteName.contains("8호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_8);
                        }
                        else if (subwayRouteName.contains("9호선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_9);
                        }
                        else if (subwayRouteName.contains("신분당선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_sinbundang);
                        }
                        else if (subwayRouteName.contains("분당선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_bundang);
                        }
                        else if (subwayRouteName.contains("경의중앙선") || subwayRouteName.contains("경춘선")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_gyungeui);
                        }
                        else if (subwayRouteName.contains("공항철도")) {
                            imageView.setBackgroundResource(R.drawable.timetableimage_airport);
                        }
                        else {
                            imageView.setBackgroundResource(R.drawable.timetableimage_else);
                        }
                    }

                    pageNo=1;

                    mySubwayListView=(ListView)findViewById(R.id.mySubwayListView);

                    mySubwayListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

                    list=new ArrayList<SubwayItems>();

                    adapter=null;

                    dailyTypeCodeString=convertEditText(dailyTypeCode);
                    upDownTypeCodeString=convertEditText(upDownTypeCode);

                    new SubwayAsync().execute();
                }
            }
        });
    }

    public String convertEditText(EditText editText){
        String input=editText.getText().toString().trim();
        String result;
        if(input.equalsIgnoreCase("평일")){
            result="01";
        }
        else if(input.equalsIgnoreCase("토요일")){
            result="02";
        }
        else if(input.equalsIgnoreCase("일요일")){
            result="03";
        }
        else if(input.equalsIgnoreCase("상행")){
            result="U";
        }
        else if(input.equalsIgnoreCase("하행")){
            result="D";
        }
        else{
            result=null;
        }
        return result;
    }

    class SubwayAsync extends AsyncTask<String, String, ArrayList<SubwayItems>>{
        @Override
        protected ArrayList<SubwayItems> doInBackground(String... strings) {
            return parser.Subway_findTimeTableApi(subwayStationId,dailyTypeCodeString,upDownTypeCodeString,list,pageNo);
        }

        @Override
        protected void onPostExecute(ArrayList<SubwayItems> SubwayItems) {
            if(adapter==null && isFilled==false){
                android.util.Log.d("KDJ","------------------------------1------------------------------");
                adapter=new Subway_findTimeTableViewModelAdapter(Subway_findTimeTableActivity.this,R.layout.subway_find_timetable_item,SubwayItems,mySubwayListView);
                isFilled=true;

                if(pageNo*10 < Integer.parseInt(parser.getSi().getTotalCount()) && parser.getSi().getTotalCount()!=null){

                    pageNo++;
                    android.util.Log.d("KDJ","pageNo: "+pageNo);

                    new SubwayAsync().execute();
                }
                mySubwayListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (SubwayItems.size() == 0 && isFilled==false) {
                    Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    dialog_progress.dismiss();
                    return;
                }
            }
            else{
                if(parser.getSi().getTotalCount()!=null){
                    if(pageNo*10 < Integer.parseInt(parser.getSi().getTotalCount())){
                        pageNo++;
                        android.util.Log.d("KDJ","pageNo: "+pageNo);

                        new SubwayAsync().execute();

                    }

                }
                else{
                    if(adapter!=null){
                        mySubwayListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        new SubwayAsync().execute();
                    }
                }
            }
        }
    }

}
