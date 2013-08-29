package mobi.intuitit.android.weatherwidget;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class WeatherActivity extends Activity {
	private static final String TAG = "WWW";

	WeatherDataManager mWeatherDataManager;

	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	private Handler mHandler;

	static {
		sWorkerThread = new HandlerThread("WeatherActivity-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Activity created");
		super.onCreate(savedInstanceState);

		mWeatherDataManager = WeatherDataManager.getInstance(this);
			// When widget gets alive from OOM, onCreate() is called but all
			// instance inside of it are pre-initialized state.
			// so weatherData from weather data manager is null. So we need to
			// handle this case here starting from last known location.
			final Context context = this;
			sWorkerQueue.post(new Runnable() {
				public void run() {
					Location location = LocationFinder
							.getLastKnownLocation(context);
					mWeatherDataManager.setLocation(location);
					Message.obtain(mHandler, 0, 0).sendToTarget();
				}
			});
		}
}
