package com.example.mycontactlist;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GpsInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_info);
		LocationListener gpsListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			@Override
			public void onProviderEnabled(String provider) {
				setAll("Enabled...");
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				setAll("Disabled");
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				TextView lat = (TextView) findViewById(R.id.gLatitude);
				TextView lon = (TextView) findViewById(R.id.gLongitude);
				TextView acc = (TextView) findViewById(R.id.gAccuracy);
				lat.setText(String.format("%f",location.getLatitude()));
				lon.setText(String.format("%f",location.getLongitude()));
				acc.setText(String.format("%f",location.getAccuracy()));
				updateBestLocation(location);
			}
			
			private void setAll(String str) {
				TextView lat = (TextView) findViewById(R.id.gLatitude);
				TextView lon = (TextView) findViewById(R.id.gLongitude);
				TextView acc = (TextView) findViewById(R.id.gAccuracy);
				lat.setText(str);
				lon.setText(str);
				acc.setText(str);
			}
		};
		LocationListener networkListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			@Override
			public void onProviderEnabled(String provider) {
				setAll("Enabled...");
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				setAll("Disabled");
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				TextView lat = (TextView) findViewById(R.id.nLatitude);
				TextView lon = (TextView) findViewById(R.id.nLongitude);
				TextView acc = (TextView) findViewById(R.id.nAccuracy);
				lat.setText(String.format("%f",location.getLatitude()));
				lon.setText(String.format("%f",location.getLongitude()));
				acc.setText(String.format("%f",location.getAccuracy()));
				updateBestLocation(location);
			}
			
			private void setAll(String str) {
				TextView lat = (TextView) findViewById(R.id.nLatitude);
				TextView lon = (TextView) findViewById(R.id.nLongitude);
				TextView acc = (TextView) findViewById(R.id.nAccuracy);
				lat.setText(str);
				lon.setText(str);
				acc.setText(str);
			}
		};
		LocationManager lm = (LocationManager)
				getBaseContext().getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
	}
	
	private Location currBestLocation;
	
	private void updateBestLocation(Location l) {
		if(currBestLocation == null) currBestLocation = l;
		else {
			//Time difference in seconds
			float timeDiff = ((float)(l.getTime() - currBestLocation.getTime())) / 1000;
			float accDiff = l.getAccuracy() - currBestLocation.getAccuracy();
			//We're going to say that one extra point of accuracy is worth two seconds extra time
			//so multiply accDiff by 2 to put them on the same level
			//Also, lower levels of accuracy are better, so multiply by -1
			accDiff *= 2 * -1;
			//If the net better time + better accuracy belongs to the new location value,
			//then set that as the location value.
			if(timeDiff + accDiff > 0) {
				currBestLocation = l;
			}
			else return; //no need to update UI
		}
		TextView lat = (TextView) findViewById(R.id.bLatitude);
		TextView lon = (TextView) findViewById(R.id.bLongitude);
		TextView acc = (TextView) findViewById(R.id.bAccuracy);
		lat.setText(String.format("%f",currBestLocation.getLatitude()));
		lon.setText(String.format("%f",currBestLocation.getLongitude()));
		acc.setText(String.format("%f",currBestLocation.getAccuracy()));
	}
}
