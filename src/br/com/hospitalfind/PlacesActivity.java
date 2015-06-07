package br.com.hospitalfind;

import br.com.hospitalfind.Model.PlaceDetail;
import br.com.hospitalfind.resource.GooglePlacesResources;

import com.example.hospital.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
 

public class PlacesActivity extends Activity {
 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
    }
}