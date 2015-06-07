package br.com.manzini.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import br.com.hospitalfind.Model.PlaceDetail;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



public class PlacesService {

	
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyC4b3qHTgny4PbEGjhrzHccbjjgh_vKYiw";

     public static PlaceDetail[] search( String lat, String lng, String radius, String pg) {
        //ArrayList<Place> resultList = null;
        PlaceDetail plc[] = null;
        String page;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?location=" + lat + "," + lng);
            sb.append("&radius=" + radius);
            sb.append("&types=hospital");
            sb.append("&pagetoken="+pg);
            sb.append("&key=" + API_KEY);
            
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error processing Places API URL"+e);
            return plc;
        } catch (IOException e) {
        	 System.out.println("Error connecting to Places API"+ e);
            return plc;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");
            if(jsonObj.has("next_page_token")){
            page = jsonObj.getString("next_page_token");
            }else{
            page="";
            }

            // Extract the Place descriptions from the results
            //resultList = new ArrayList<Place>(predsJsonArray.length());
            plc = new PlaceDetail[predsJsonArray.length()];
            for (int i = 0; i < predsJsonArray.length(); i++) {
            	PlaceDetail place = new PlaceDetail();
            	place.page=page;
                place.placeId = predsJsonArray.getJSONObject(i).getString("place_id");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                place.lon = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                place.lat = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                if (predsJsonArray.getJSONObject(i).has("rating")) {
                	place.ratings = predsJsonArray.getJSONObject(i).getInt("rating");
                }
                plc[i]=place;
                //resultList.add(place);
            }
        } catch (JSONException e) {
        	 System.out.println("Error processing JSON results"+ e);
        }

        return plc;
    }

    public static PlaceDetail details(String reference) {
        HttpURLConnection conn = null;
       
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_DETAILS);
            sb.append(OUT_JSON);
            sb.append("?placeid="+ URLEncoder.encode(reference, "utf8"));
            sb.append("&key=" + API_KEY);
            

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(),"utf8");

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
        	 System.out.println("Error processing Places API URL"+ e);
            return null;
        } catch (IOException e) {
        	 System.out.println("Error connecting to Places API"+ e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        PlaceDetail place = null;
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");

            place = new PlaceDetail();
            place.placeId = jsonObj.getString("place_id");
            place.lon = jsonObj.getJSONObject("geometry").getJSONObject("location").getString("lng");
            place.name =jsonObj.getString("name");
            place.end=jsonObj.getString("formatted_address");
            place.lat = jsonObj.getJSONObject("geometry").getJSONObject("location").getString("lat");
            if (jsonObj.has("rating")) {
            	place.ratings = jsonObj.getInt("rating");
            }
        } catch (JSONException e) {
        	 System.out.println("Error processing JSON results"+e);
        }

        return place;
    }
   
}