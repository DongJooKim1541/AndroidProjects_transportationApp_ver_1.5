package com.example.busappver15.ui.subway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busappver15.R;

import java.util.ArrayList;

public class Subway_findfacilityActivity extends AppCompatActivity {

    private ListView myfacilityListView;
    private Subway_findFacilityParser parser;
    private ArrayList<SubwayItems> list;
    private Subway_findFacilityViewModelAdapter adapter;
    private String subwayStationId;

    int pageNo=1;
    ProgressDialog dialog_progress;

    boolean mLockListView=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_findfacility);

        dialog_progress = new ProgressDialog(this);
        dialog_progress.setMessage("Loading.....");

        Intent i=getIntent();
        subwayStationId=i.getExtras().getString("subwayStationId");

        parser=new Subway_findFacilityParser();

        if(subwayStationId!=null){
            pageNo=1;

            myfacilityListView = (ListView) findViewById(R.id.myfacilityListView);

            myfacilityListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

            list = new ArrayList<SubwayItems>();

            adapter=null;

            new SubwayAsync().execute();
        }
    }

    class SubwayAsync extends AsyncTask<String, String, ArrayList<SubwayItems>>{
        @Override
        protected ArrayList<SubwayItems> doInBackground(String... strings) {
            return parser.Subway_findBusRouteApi(subwayStationId,list,pageNo);
        }

        @Override
        protected void onPostExecute(ArrayList<SubwayItems> SubwayItems) {
            if(adapter==null){

                adapter=new Subway_findFacilityViewModelAdapter(Subway_findfacilityActivity.this,R.layout.subway_find_facility_item, SubwayItems, myfacilityListView);

                myfacilityListView.setOnScrollListener(scrollListener);

                myfacilityListView.setAdapter(adapter);

            }
            if (SubwayItems.size() == 0) {
                Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                dialog_progress.dismiss();
                return;
            }

            //리스트뷰의 변경사항 갱신
            adapter.notifyDataSetChanged();
            mLockListView = false;
            dialog_progress.dismiss();
        }
    }

    AbsListView.OnScrollListener scrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            int count=totalItemCount-visibleItemCount;

            if(firstVisibleItem>=count && totalItemCount!=0 && mLockListView==false){
                mLockListView=true;
                if(pageNo*10 < Integer.parseInt(parser.getSi().getTotalCount()) && list.size()>=10){

                    pageNo+=1;
                    new SubwayAsync().execute();
                }
            }
        }
    };
}
