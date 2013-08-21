package mobi.intutit.android.weatherwidget;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LocationFinder {
	// UI handler codes.
    public static final int UPDATE_ADDRESS = 1;
    public static final int UPDATE_LOCATION = 2;

	private static final String TAG = "WWW";
	
	private static final boolean USE_LAST_KNOWN_LOCATION = true;
	private static final boolean RANDOMIZE_LOCATION = false;

    private LocationManager mLocationManager;
    private Handler mHandler;
    
    private Context mContext;
    private boolean mGpsEnabled;
    private boolean mNetworkEnabled;
    
    private Location mLocation;
    private static HandlerThread mWorkerThread;
    
    public static Location getLastKnownLocation(Context context) {
    	if(!isGpsNetworkEnabled(context)) {
    		Location location = new Location(LocationManager.NETWORK_PROVIDER);
        	location.setLatitude(37.566535);
        	location.setLongitude(126.977969);
        	return location;
    	}
    	else {
    		LocationManager locationManager =
                    (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        	String providerName = LocationManager.NETWORK_PROVIDER;
    		return locationManager.getLastKnownLocation(providerName);
    	}
    }
    
    public static boolean isGpsNetworkEnabled(Context context) {
    	LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || 
        		locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
    }
    
    public LocationFinder(Context context, HandlerThread workerThread) {
    	
	    
    	// Check if the GPS setting is currently enabled on the device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
    	mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    	
    	mGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	mNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	Log.d(TAG, "mGpsEnabled: " + mGpsEnabled); 
    	Log.d(TAG, "mNetworkEnabled: " + mNetworkEnabled);
    	
        /*mGeocoderAvailable =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();*/
                
        mContext = context;
        mWorkerThread = workerThread;
	}
    
    public void find() {
        if(!mGpsEnabled && !mNetworkEnabled) { 
        	Toast.makeText(mContext, "찾을수 없음", Toast.LENGTH_LONG).show();
        	return;
        }
        
        String providerName;
        if(mNetworkEnabled){
        	providerName = LocationManager.NETWORK_PROVIDER;
        }else if (mGpsEnabled){
        	providerName = LocationManager.GPS_PROVIDER;
        }else{
        	return;
    	}
        
        Log.d(TAG, "Location finding with provider=" + providerName);
        Location location = requestUpdatesFromProvider(providerName, "실행안됨");
        return;
    }
    
    /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     *
     * @param provider Name of the requested provider.
     * @param errorResId Resource id for the string message to be displayed if the provider does
     *                   not exist on the device.
     * @return A previously returned {@link android.location.Location} from the requested provider,
     *         if exists.
     */
    private Location requestUpdatesFromProvider(final String provider, String errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestSingleUpdate(provider, listener, mWorkerThread.getLooper());
            location = mLocationManager.getLastKnownLocation(provider);
            if(USE_LAST_KNOWN_LOCATION) {
            	Log.d(TAG, "getLastKnownLocation location: " + location);
	            
	            if(location == null) {
	            	//Seoul as default 
	            	location = new Location(LocationManager.NETWORK_PROVIDER);
	            	location.setLatitude(37.566535);
	            	location.setLongitude(126.977969);
	            }
	            Message.obtain(mHandler, UPDATE_LOCATION, location).sendToTarget();
	            doReverseGeocoding(location);
            }
        } else { 
            Toast.makeText(mContext, errorResId, Toast.LENGTH_LONG).show();
        }
        //Log.d(TAG, "requestUpdatesFromProvider location: " + location);
        return location;
    }
    
    private void doReverseGeocoding(Location location) {
    	mLocation = location;

        (new ReverseGeocodeLookupTask()).execute();
    }
    
	private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
        	Log.d(TAG, "Location updated " + location);
        	
        	if(RANDOMIZE_LOCATION){
	        	double latitude = location.getLatitude() - Math.random()*2.5;
	        	latitude = Double.parseDouble(("" + latitude).substring(0, 10));
	        	location.setLatitude(latitude); //위도 
	        	
	        	double longitude = location.getLongitude() + Math.random()*2.0;
	        	longitude = Double.parseDouble(("" + longitude).substring(0, 11));
	        	location.setLongitude(longitude); //경도
        	}
        	
        	if(!USE_LAST_KNOWN_LOCATION) {
	        	Message.obtain(mHandler, UPDATE_LOCATION, location).sendToTarget();
	        	doReverseGeocoding(location);
        	}
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            String localityName = "";
            Location location = mLocation;
            
            //Log.d(TAG, "location:" + location);

            if (location != null)
            {
                localityName = Geocoder.reverseGeocode(location);
                Message.obtain(mHandler, UPDATE_ADDRESS, localityName).sendToTarget();
            }

            return null;
        }
    }
   
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
}

