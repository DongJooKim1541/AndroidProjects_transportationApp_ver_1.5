package com.example.busappver15.ui.subway;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.busappver15.R;

import java.util.ArrayList;

public class SubwayFragment extends Fragment {

    private ListView myListView;
    public static EditText subwayStationName;
    private Button searchBtn;
    private SubwayStationListSearchParser sParser;
    private ArrayList<SubwayItems> list;
    private SubwayStationListSearchViewModelAdapter adapter;

    int pageNo=1;
    ProgressDialog dialog_progress;

    boolean mLockListView=true;

    private SubwayDialog subwayDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_subway, container, false);

        subwayStationName=(EditText)root.findViewById(R.id.subwayStationName);
        searchBtn=(Button)root.findViewById(R.id.searchSubwayBtn);

        dialog_progress = new ProgressDialog(getActivity());
        dialog_progress.setMessage("Loading.....");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subwayStationName.getText().toString().trim().length()>0){
                    pageNo=1;

                    myListView=(ListView)root.findViewById(R.id.mySubwayListView);

                    myListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

                    list=new ArrayList<SubwayItems>();

                    adapter=null;

                    new subwayAsync().execute();

                    android.util.Log.d("KDJ","--------subwayAsync 실행--------");
                }
            }
        });

        sParser=new SubwayStationListSearchParser();

        return root;
    }

    class subwayAsync extends AsyncTask<String,String,ArrayList<SubwayItems>>{
        @Override
        protected ArrayList<SubwayItems> doInBackground(String... strings) {
            return sParser.SubwayStationListSearchApi(list,pageNo);
        }

        @Override
        protected void onPostExecute(ArrayList<SubwayItems> SubwayItems) {

            if(adapter==null){
                adapter=new SubwayStationListSearchViewModelAdapter(getActivity(),R.layout.subway_station_item,SubwayItems,myListView);

                myListView.setOnScrollListener(scrollListener);

                myListView.setAdapter(adapter);
            }

            if(SubwayItems.size()==0){
               Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                dialog_progress.dismiss();
                return;
            }

            adapter.notifyDataSetChanged();
            mLockListView = false;
            dialog_progress.dismiss();

            listClickEvents();

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

                if(pageNo*10 < Integer.parseInt(sParser.getSi().getTotalCount()) && list.size()>=10){

                    pageNo+=1;
                    new subwayAsync().execute();
                }

            }
        }
    };

    private void listClickEvents(){

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                subwayDialog=new SubwayDialog(getContext(),adapter,position);

                subwayDialog.setCanceledOnTouchOutside(false);
                subwayDialog.show();
            }
        });
    }

}