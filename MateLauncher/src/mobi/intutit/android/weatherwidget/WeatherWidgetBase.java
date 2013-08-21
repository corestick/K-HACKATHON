package mobi.intutit.android.weatherwidget;



import mobi.intuitit.android.mate.launcher.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * The weather widget's AppWidgetProvider.
 */
public abstract class WeatherWidgetBase{
	private static String TAG = "WWW";
	
	public static String CLICK_ACTION = "com.rambar.weeklyweatherwidget.CLICK";
	public static String REFRESH_ACTION = "com.rambar.weeklyweatherwidget.REFRESH";
	public static String EXTRA_WEATHER_INFO = "com.rambar.weeklyweatherwidget.weather_info";
	
	
	private static Location mLocation;
	private static String mCityname;
	
	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	
	
	static {
		Log.e(TAG, "static init");
		sWorkerThread = new HandlerThread("WeatherDataManager-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());		
	}
	
	private WeatherDataManager getWeatherDataManager(Context context) {
		return WeatherDataManager.getInstance(context);
	}

    public void onReceive(Context context, Intent intent) {
    	final Context ctx = context;
        String action = intent.getAction();
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        
        if (action.equals(REFRESH_ACTION)) {        	
//        	updateAll(context, appWidgetManager, appWidgetIds);
        }  else if (action.equals(CLICK_ACTION)) {        	
        	final int position = intent.getIntExtra(EXTRA_WEATHER_INFO, 0);
        	if(mCityname != null) {
	        	//Bind the click intent for starting an activity
//        		startActivity(context, position);
        	} else {
        		//        		onUpdate(context, appWidgetManager, appWidgetIds);
        		
        	}
        }
    }
	
//	private void showCurrentWeather(Context context, RemoteViews rv, int appWidgetId) {
//		final String formatStr = context.getResources().getString(R.string.current_temp_format_string);
//		rv.setTextViewText(R.id.current_temp, String.format(formatStr, getWeatherDataManager(context).getCurrentTemp()));
//		rv.setImageViewResource(R.id.city_wicon, getWeatherDataManager(context).getCurrentResourceID());
//		rv.setTextViewText(R.id.city_weather, getWeatherDataManager(context).getCurrentWeather());
//		
//		final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//		mgr.updateAppWidget(appWidgetId, rv);
//	}	
//	
	
	private void setLocation(final Context context, final RemoteViews rv, final int appWidgetId, final Location location) {
		getWeatherDataManager(context).setLocation(location);
		final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		mgr.updateAppWidget(appWidgetId, rv);
	}
    
    private void updateAll(Context context) {
    	final Context ctx = context;
    	
    	if(LocationFinder.isGpsNetworkEnabled(context)){
    		LocationFinder locationFinder = new LocationFinder(context, sWorkerThread);
    		locationFinder.setHandler(new Handler() {
				public void handleMessage(Message msg) {
                	switch(msg.what) {
                	case LocationFinder.UPDATE_LOCATION:
                		Log.d(TAG, "handleMessage() : UPDATE_LOCATION");
	                	mLocation = (Location) msg.obj;
	                	break;
                	case LocationFinder.UPDATE_ADDRESS:
                		Log.d(TAG, "handleMessage() : UPDATE_ADDRESS");
                		mCityname = (String)msg.obj;
                		
                	
                		if(mCityname.equals("알수없음")) 
            				Toast.makeText(ctx, "에러", Toast.LENGTH_LONG).show();
                		
                		Toast.makeText(ctx, "에러", Toast.LENGTH_SHORT).show();
                		break;
                	}
                }
    	    });
    		locationFinder.find();
    	}
    	else { //GPS not enabled 
    		mLocation = new Location(LocationManager.NETWORK_PROVIDER);
    		mLocation.setLatitude(37.566535);
    		mLocation.setLongitude(126.977969);
			mCityname = "서울";			
			
    	}   	
    }
}