package mobi.intutit.android.weatherwidget;


import mobi.intutit.android.weatherwidget.AddressMap.AddressInfo;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class WeatherDataManager {
	private static String TAG = "WWW";
	
	private static WeatherDataManager mWeatherDataContainer = null;

	private static Context mContext;	
	
	private CurrentWeatherData mCurrentWeatherData;
	
	private WeatherDataManager(){
	}
	
	public static WeatherDataManager getInstance(Context context) {		
		if(mWeatherDataContainer == null) {
			Log.d(TAG, "new WeatherDataManager");
			mWeatherDataContainer = new WeatherDataManager();
			mContext = context;
		}
		return mWeatherDataContainer;
	}
	
	public boolean isLoaded() {
		if(mCurrentWeatherData == null)
			return false;
		else
			return true;
	}
	
	public void setLocation(Location location) {		
		
		Log.d(TAG, "setLocation() Latitude:" + location.getLatitude() + " Longitue:" + location.getLongitude());
		
		final AddressInfo ai = AddressMap.getInstance().queryCloseCity(location);		
		int curStnId = AddressMap.getInstance().getCurStationId(ai.mCityname);		
		String curWeatherURL = "http://www.kma.go.kr/XML/weather/sfc_web_map.xml";
		
		synchronized (this) {
			mCurrentWeatherData = new CurrentWeatherData();
			mCurrentWeatherData.setOption(curWeatherURL, curStnId);
			
			mCurrentWeatherData.update();			
		}
	}
	
	public void updateCurrentWeather(Runnable callback){
		synchronized (this) {
			mCurrentWeatherData.update();
		}
	}
	
	public int getCurrentTemp() {
		synchronized (this) {
			if(mCurrentWeatherData == null) {
				return -999;
			} else {
				return mCurrentWeatherData.getCurrentTemp();
			}
		}
	}
	
	public String getCurrentWeather() {
		synchronized (this) {
			if(mCurrentWeatherData == null) {
				return "";
			} else {
				return mCurrentWeatherData.getWeatherString();
			}
		}
	}	
}
