package br.com.hospitalfind;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.hospitalfind.Model.PlaceDetail;
import br.com.hospitalfind.resource.GooglePlacesResources;

import com.example.hospital.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapactivity extends Activity implements LocationListener {

	// Initialize the location fields
	private PlacesTask PTask = null;
	private LocationManager locationManager;
	private String provider;
	static final LatLng Def = new LatLng(-23.55090699, -46.63326144);
	private LatLng Loc;
	private GoogleMap map;
	private Marker meuLocal;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		// Initialize the location fields
		if (location != null) {
			float lat = (float) (location.getLatitude());
			float lng = (float) (location.getLongitude());
			Loc = new LatLng(lat, lng);
			Toast.makeText(this, Loc + "", Toast.LENGTH_SHORT).show();

		} else {
			Loc = Def;
		}
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		meuLocal = map.addMarker(new MarkerOptions()
		.position(Loc)
		.icon(BitmapDescriptorFactory.defaultMarker(
			     BitmapDescriptorFactory.HUE_AZURE))
		.title("Minha localização")
		.snippet("Nem tudo que é ouro fulgura")
				);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc, 15));

	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			float lat = (float) (location.getLatitude());
			float lng = (float) (location.getLongitude());
			Loc = new LatLng(lat, lng);
			Toast.makeText(this, Loc + "", Toast.LENGTH_SHORT).show();

		} else {
			Loc = Def;
		}
		meuLocal.remove();
		meuLocal = map.addMarker(new MarkerOptions()
		.position(Loc)
		.icon(BitmapDescriptorFactory.defaultMarker(
			     BitmapDescriptorFactory.HUE_AZURE))
		.title("Minha localização")
		.snippet("Nem tudo que é ouro fulgura")
				);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Loc, 15));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
        case R.id.action_search:
        	PTask = new PlacesTask(String.valueOf(Loc.latitude),String.valueOf( Loc.longitude), "5000", this);
			PTask.execute((Void) null);
			return true;
			
        case R.id.action_settings:
        	Toast.makeText(this,"Configurações", Toast.LENGTH_SHORT).show();
            return true;
		}
		return super.onOptionsItemSelected(item);
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mapa, menu);
		return true;
	}

	public class PlacesTask extends AsyncTask<Void, Void, PlaceDetail[]> {

		private final String lat;
		private final String lng;
		private final String rad;
		private Context c = null;
		private PlaceDetail[] plc = null;

		PlacesTask(String la, String lo, String ra, Context ctx) {
			lat = la;
			lng = lo;
			rad = ra;
			c = ctx;
		}

		@Override
		protected PlaceDetail[] doInBackground(Void... params) {

		
			plc = GooglePlacesResources.buscar(lat, lng, rad,"");
			return plc;

		}

		@Override
		protected void onPostExecute(final PlaceDetail[] success) {
			PTask = null;
			// showProgress(false);
			for (int i = 0; i < success.length; i++) {
				;
				double lLat = Double.parseDouble(success[i].getLat());
				double lLng = Double.parseDouble(success[i].getLon());
				Marker Local = map.addMarker(new MarkerOptions()
				.position(new LatLng(lLat, lLng))
				.title(success[i].getName() + " " + success[i].getRatings()));

			}
		}

		@Override
		protected void onCancelled() {
			PTask = null;
			// showProgress(false);
		}
	}
}
