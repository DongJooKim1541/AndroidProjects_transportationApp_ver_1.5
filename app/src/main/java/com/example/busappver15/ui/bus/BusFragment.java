package com.example.busappver15.ui.bus;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class BusFragment extends Fragment {

    private ListView myListView;
    public static EditText cityName,busNum;
    private Button searchBtn;
    private CityCodeListParser cParser;
    private RouteNumberListSearchParser rParser;
    private ArrayList<BusItems> list,cityList;
    private RouteNumberListSearchViewModelAdapter adapter;
    public static String cityCode=null;

    int pageNo=1;   //검색을 시작할 page 번호
    ProgressDialog dialog_progress; //로딩을 위한 다이얼로그

    //스크롤링을 통한 추가 로드를 위해 필요한 변수
    LayoutInflater mInflater;
    boolean mLockListView=true;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_bus, container, false);

        cityName = (EditText) root.findViewById(R.id.cityName);    //도시명
        busNum = (EditText) root.findViewById(R.id.busNum);    //노선 번호

        //다이얼로그 객체 준비
        dialog_progress = new ProgressDialog(getActivity());
        dialog_progress.setMessage("Loading.....");

        searchBtn = (Button) root.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //한글자 이상 입력받은 경우에만 검색
                if (cityName.getText().toString().trim().length() > 0 && busNum.getText().toString().trim().length() > 0) {
                    pageNo=1;

                    //객체 검색
                    myListView = (ListView) root.findViewById(R.id.myListView);

                    //리스트뷰의 오버스크롤 제거
                    myListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

                    cityList=new ArrayList<BusItems>();
                    list = new ArrayList<BusItems>();

                    adapter = null;

                    android.util.Log.d("KDJ","--------cityAsync 실행--------");
                    new cityAsync().execute();

                }
            }
        });

        cParser=new CityCodeListParser();
        rParser = new RouteNumberListSearchParser();

        return root;
    }


    class cityAsync extends AsyncTask<String, String, ArrayList<BusItems>> {
        @Override
        protected ArrayList<BusItems> doInBackground(String... strings) {
            return cParser.cityCodeListApi(cityList);
        }

        @Override
        protected void onPostExecute(ArrayList<BusItems> busItems) {
            //doInBackground에서 통신을 마친 결과가 onPostExcute의 Items 넘어온다.
            for(int i=0;i<busItems.size();i++){
                if(busItems.get(i).getCityName().trim().equalsIgnoreCase(cityName.getText().toString().trim())){
                    cityCode=busItems.get(i).getCityCode().trim();
                    dialog_progress.show(); //로딩 시작
                    //BusAsync 실행
                    //android.util.Log.d("KDJ","--------BusAsync 실행--------");
                    new BusAsync().execute();
                }
            }
            if(cityCode==null){
                Toast.makeText(getActivity(), "검색한 도시가 없습니다.", Toast.LENGTH_SHORT).show();
                dialog_progress.dismiss();
                return;
            }
        }
    }

    class BusAsync extends AsyncTask<String, String, ArrayList<BusItems>> {

        @Override
        protected ArrayList<BusItems> doInBackground(String... strings) {
            return rParser.connectRouteNumberListSearchApi(list,pageNo);
        }

        @Override
        protected void onPostExecute(ArrayList<BusItems> BusItems) {
            //doInBackground에서 통신을 마친 결과가 onPostExcute의 Items 넘어온다. 리스트뷰의 화면을 갱신

            if (adapter == null) {
                adapter = new RouteNumberListSearchViewModelAdapter(getActivity(),R.layout.bus_all_item, BusItems, myListView);

                //리스트 뷰에 스크롤 리스너 등록
                myListView.setOnScrollListener(scrollListener);

                //리스트 뷰에 adapter 세팅
                myListView.setAdapter(adapter);

            }

            if (BusItems.size() == 0) {
                Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                dialog_progress.dismiss();
                return;
            }

            //리스트뷰의 변경사항 갱신
            adapter.notifyDataSetChanged();
            mLockListView = false;
            dialog_progress.dismiss();

            watchListOfBusByRoute();
        }
    }

    //리스트 뷰의 스크롤 감시자
    AbsListView.OnScrollListener scrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //현재 리스트뷰의 상태를 알려주는 메소드
            //scrollState - 상태값은 총 3가지

            //SCROLL_STATE_FLING (2)
            // - 터치 후 손을 뗀 상태에서 아직 스크롤 되고 있는 상태

            //SCROLL_STATE_IDLE (0)
            // - 스크롤이 종료 되어 어떠한 애니메이션도 발생하지 않는 상태

            //SCROLL_STATE_TOUCH_SCROLL (1)
            // - 스크린에 터치를 한 상태에서 스크롤하는 상태
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //스크롤이 발생하는 동안 지속적으로 호출되는 메소드
            //firstVisibleItem - 현재 보여지는 리스트뷰의 최상단 아이템 인덱스
            //visibleItemCount - 현재 보여지는 리스트뷰의 아이템 개수
            //totalItemCount - 현재 리스트뷰의 총 아이템 개수

            //현재 보여지는 처음보이는 항목의 인덱스 값고 아이템 카운트를 더했을 때 totalItemCount와 같으면 가장 아래로 스크롤 되었다.
            int count=totalItemCount-visibleItemCount;
            android.util.Log.d("KDJ","visibleItemCount: "+visibleItemCount);
            if(firstVisibleItem>=count && totalItemCount!=0 && mLockListView==false){
                mLockListView=true;
                android.util.Log.d("KDJ","Integer.parseInt(rParser.getBi().getTotalCount()): "+Integer.parseInt(rParser.getBi().getTotalCount()));
                if(pageNo*10 < Integer.parseInt(rParser.getBi().getTotalCount()) && list.size()>=10){

                    pageNo+=1;
                    new BusAsync().execute();
                }

            }
        }
    };

    private void watchListOfBusByRoute(){

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BusItems busItems=(BusItems)adapter.getItem(position);

                Intent i=new Intent(getActivity(), Bus_subActivity.class);

                i.putExtra("cityCode",busItems.getCityCode());
                i.putExtra("routeId",busItems.getRouteId());

                startActivity(i);

            }
        });
    }

}