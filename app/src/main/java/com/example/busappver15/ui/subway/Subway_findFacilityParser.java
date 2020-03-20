package com.example.busappver15.ui.subway;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Subway_findFacilityParser {

    private SubwayItems si;

    public ArrayList<SubwayItems> Subway_findBusRouteApi(String subwayStationId, ArrayList<SubwayItems> list, int pageNo){

        try{
            String key="pL4Erh3b64mgmWQeg9qh3bQ4rP9SAu1zAOanR4S1I2U3dJ%2BPproQUh9jY93JXyYnj2NIuC7ShhgecOwvq25pmw%3D%3D";
            String urlStr="http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnExitAcctoCfrFcltyList?subwayStationId="+subwayStationId+"&pageNo="+pageNo+"&serviceKey="+key;

            URL url=new URL(urlStr);

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type","application/xml");

            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();

            XmlPullParser parser=factory.newPullParser();

            parser.setInput(connection.getInputStream(),null);

            int parserEvent=parser.getEventType();

            while(parserEvent!= XmlPullParser.END_DOCUMENT){
                //xml문서의 끝이 아닐때만 반복한다
                //시작 태그일때만 태그의 이름을 알아낸다.

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
                    }

                    if(tagName.equalsIgnoreCase("dirDesc")){
                        String dirDesc=parser.nextText();
                        si.setDirDesc(dirDesc);
                    }

                    if(tagName.equalsIgnoreCase("exitNo")){
                        String exitNo=parser.nextText();
                        si.setExitNo(exitNo);
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
