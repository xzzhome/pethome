package com.xzz.basic.util;

import com.xzz.org.domain.Shop;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 位置相关工具类
 */
public class DistanceUtils {

    //给一个地址 - 算出这个地址的经纬度
    public static Point getPoint(String address){
        String Application_ID="PQ9FAt6qg7taDWj6LLABYO7u6bSETXhD";//配置上自己的百度地图应用的AK
        try{
            String sCurrentLine; String sTotalString;sCurrentLine ="";
            sTotalString = "";
            InputStream l_urlStream;
            URL l_url = new URL("http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak="+Application_ID+"&callback=showLocation");
            HttpURLConnection l_connection = (HttpURLConnection) l_url.openConnection();
            l_connection.connect();
            l_urlStream = l_connection.getInputStream();
            java.io.BufferedReader l_reader = new java.io.BufferedReader(new InputStreamReader(l_urlStream));
            String str=l_reader.readLine();
            System.out.println(str);

            //用经度分割返回的网页代码
            String s=","+"\""+"lat"+"\""+":";
            String strs[]=str.split(s,2);
            String s1="\""+"lng"+"\""+":";
            String a[]=strs[0].split(s1, 2);
            s1="}"+","+"\"";
            String a1[]=strs[1].split(s1,2);


            Point point=new Point();
            point.setLng(Double.valueOf(a[1]));
            point.setLat(Double.valueOf(a1[0]));
            return point;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //地球半径,进行经纬度运算需要用到的数据之一
    private static final double EARTH_RADIUS = 6378137;
    //根据坐标点获取弧度
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param point1 A点坐标
     * @param point2 B点坐标
     * @return
     */
    public static double getDistance(Point point1,Point point2)
    {
        double radLat1 = rad(point1.getLat());
        double radLat2 = rad(point2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(point1.getLng()) - rad(point2.getLng());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param point 用户指定的地址坐标
     * @param shops 商店
     * @return
     */
    public static Shop getNearestShop (Point point, List<Shop> shops) {

        //如果传过来的集合只有一家店铺,那么直接将这家店铺的信息返回就是最近的店铺了
        Shop shop=shops.get(0);
        //获取集合中第一家店铺到指定地点的距离
        double distance=getDistance(point,getPoint(shops.get(0).getAddress()));
        double minDistance = distance;
        //如果有多家店铺,那么就和第一家店铺到指定地点的距离做比较
        if (shops.size()>1){
            for (int i=1;i<shops.size();i++){
                double distanceTmp = getDistance(point, getPoint(shops.get(i).getAddress()));
                if (distanceTmp<distance){
                    shop=shops.get(i);
                    minDistance = distanceTmp;
                }
            }
        }

        /*if (minDistance>50000){ //大于50公里，没有合适的店铺
            return null;
        }*/
        return shop;
    }

}
