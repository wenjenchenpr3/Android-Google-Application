package edu.utexas.googlemap;

/*import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/


import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
 


import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
 

public class MainActivity extends FragmentActivity {
 
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { // Google Play Services are available
        	
        	if(googleMap == null){
 
		        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		     
		        // Getting a reference to the map
		        googleMap = supportMapFragment.getMap();
        	}
	 
	        // Getting reference to btn_find of the layout activity_main
	        Button btn_find = (Button) findViewById(R.id.btn_find);
	        
	 
	        // Defining button click event listener for the find button
	        OnClickListener findClickListener = new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // Getting reference to EditText to get the user input location
	                EditText etLocation = (EditText) findViewById(R.id.et_location);
	 
	                // Getting user input location
	                String location = etLocation.getText().toString();
	                
	 
	                if(location!=null && !location.equals("")){
	                    new GeocoderTask().execute(location);
	                    
	                }
	            }
	        };
	 
	        // Setting button click event listener for the find button
	        btn_find.setOnClickListener(findClickListener);
	        
	        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        
	        RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
	        rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        	 
	            @Override
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	                if(checkedId == R.id.rb_normal){
	                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	                }else if(checkedId == R.id.rb_satellite){
	                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	                }else if(checkedId == R.id.rb_terrain){
	                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	                }
	            }
	        });
	        
	        /*ToggleButton toggle = (ToggleButton) findViewById(R.id.traffic);
	        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	        	@Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	                if (isChecked) {
	                    // The toggle is enabled
	                	googleMap.setTrafficEnabled(true);
	                } else {
	                    // The toggle is disabled
	                	googleMap.setTrafficEnabled(false);
	                }
	            }
	        });*/
	        
	        CheckBox checkbox = (CheckBox) findViewById(R.id.traffic);
	        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	                if (isChecked) {
	                	googleMap.setTrafficEnabled(true);
	                } else {
	                	googleMap.setTrafficEnabled(false);
	                }
	            }
	        }); 
	        
	        
        	if(googleMap != null){
    	        // Enabling MyLocation Layer of Google Map
		        googleMap.setMyLocationEnabled(true);
		        
		        // Getting LocationManager object from System Service LOCATION_SERVICE
		        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		        
		        // Creating a criteria object to retrieve provider
		        Criteria criteria = new Criteria();
	
		        // Getting the name of the best provider
		        String provider = locationManager.getBestProvider(criteria, true);
		        
		        // Getting Current Location
		        Location location = locationManager.getLastKnownLocation(provider);
		        
		        latLng = new LatLng(location.getLatitude(), location.getLongitude());
	
		        if(location!=null){
			        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
		        }
        		
        	}
        }

        
        
              
 
    }

    
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.layout.menu, menu);
        menu.add("menu1");
	    menu.add("menu2");
	    menu.add("menu3");
	    menu.add("menu4");
	      
	    return super.onCreateOptionsMenu(menu);
    }
    
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      // TODO Auto-generated method stub
      Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
      return super.onOptionsItemSelected(item);
    }
 
    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
                String a = String.valueOf("Long: " + String.valueOf(address.getLongitude() + " lat: " + address.getLatitude()));
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                markerOptions.snippet(a);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

            }
        }
    }
    
    
}