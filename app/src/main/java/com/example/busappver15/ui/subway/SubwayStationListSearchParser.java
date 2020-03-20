package com.example.busappver15.ui.subway;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SubwayStationListSearchParser {

    private SubwayItems si;
    private String myQuery1;

    public ArrayList<SubwayItems> SubwayStationListSearchApi(ArrayList<SubwayItems> list,int pageNo){
        try{
            myQuery1= URLEncoder.encode(SubwayFragment.subwayStationName.getText().toString().trim(),"UTF-8"); //도시명
            String key="pL4Erh3b64mgmWQeg9qh3bQ4rP9SAu1zAOanR4S1I2U3dJ%2BPproQUh9jY93JXyYnj2NIuC7ShhgecOwvq25pmw%3D%3D";
            String urlStr="http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getKwrdFndSubwaySttnList?subwayStationName="+myQuery1+"&pageNo="+pageNo+"&serviceKey="+key;
            android.util.Log.d("KDJ","URL: "+urlStr);

            //URL클래스 객체 생성하여 접근할 경로 설정
            URL url=new URL(urlStr);

            //URL클래스의 연결 정보를 Connection 객체에 전달
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            //메소드방식 설정
            connection.setRequestMethod("GET");

            //타입설정
            connection.setRequestProperty("Content-Type","application/xml");

            //URL을 수행하여 받을 자원을 변환하기위한 객체 준비
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();

            //파서 객체를 생성한다.
            XmlPullParser parser=factory.newPullParser();

            //connection을 통해서 파싱을 진행한다.
            parser.setInput(connection.getInputStream(),null);

            //파서를 통하여 각 요소(Element)들을 반복 수행처리 한다.
            int parserEvent=parser.getEventType();

            while(parserEvent!=XmlPullParser.END_DOCUMENT){
                //xml문서의 끝이 아닐때만 반복한다
                //시작 태그일때만 태그의 이름을 알아낸다.

                //</item>일때 Item객체 생성
                if(parserEvent==XmlPullParser.START_TAG){
                    String tagName=parser.getName();
                    if(tagName.equalsIgnoreCase("item")){
                        si=new SubwayItems();
                    }
                }

                if(parserEvent==XmlPullParser.START_TAG){

                    String tagName=parser.getName();

                    if(tagName.equalsIgnoreCase("totalCount")){
                        String totalCount=parser.nextText();
                        si.setTotalCount(totalCount);
                        android.util.Log.d("KDJ","si.getTotalCount(): "+si.getTotalCount());
                    }

                    if(tagName.equalsIgnoreCase("subwayRouteName")){
                        String subwayRouteName=parser.nextText();
                        si.setSubwayRouteName(subwayRouteName);
                        android.util.Log.d("KDJ","si.getSubwayRouteName(): "+si.getSubwayRouteName());
                    }

                    if(tagName.equalsIgnoreCase("subwayStationId")){
                        String subwayStationId=parser.nextText();
                        si.setSubwayStationId(subwayStationId);
                        android.util.Log.d("KDJ","si.getSubwayStationId(): "+si.getSubwayStationId());
                    }

                    if(tagName.equalsIgnoreCase("subwayStationName")){
                        String subwayStationName=parser.nextText();
                        si.setSubwayStationName(subwayStationName);
                        android.util.Log.d("KDJ","si.setSubwayStationName(): "+si.getSubwayStationName());
                        list.add(si);
                    }
                }
                parserEvent=parser.next();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public SubwayItems getSi() {
        return si;
    }
}
