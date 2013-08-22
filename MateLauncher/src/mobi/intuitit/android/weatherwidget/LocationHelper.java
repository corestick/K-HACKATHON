package mobi.intuitit.android.weatherwidget;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationHelper {
	/**
	 * Context 객체 입니다.
	 */
	private Context mContext;
	/**
	 * 위도값을 저장할 변수 입니다.
	 */
	private double mLat;
	/**
	 * 경도값을 저장할 변수 입니다.
	 */
	private double mLng;
	/**
	 * GPS의 정확도 입니다.
	 */
	private int mAccuracy;
	/**
	 * 셋팅유무를 저장할 변수 입니다.
	 */
	private boolean isLocationSetting = false;
	/**
	 * ACCURACY의 변수 (자세히)
	 */
	public static final int ACCURACY_FINE = 1;
	/**
	 * ACCURACY의 변수 (보통)
	 */
	public static final int ACCURACY_COARSE = 2;

	public LocationHelper(Context context) {
		this.mContext = context;
		this.mAccuracy = ACCURACY_COARSE;
	}

	/**
	 * 셋팅을 시작한다.
	 */
	public void run() {
		final LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		final Criteria criteria = new Criteria();
		criteria.setAccuracy(mAccuracy);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// Provider 생성
		final String bestProvider = locationManager.getBestProvider(criteria,
				true);
		locationManager.requestLocationUpdates(bestProvider, 2000, 10,
				mLocationListener);

		Location location = locationManager.getLastKnownLocation(bestProvider);
		updateWithNewLocation(location);
	}

	private final LocationListener mLocationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		if (location == null) {
			isLocationSetting = false;
			return;
		}

		mLat = location.getLatitude();
		mLng = location.getLongitude();
		isLocationSetting = true;

//		Toast.makeText(mContext, "Location 가 셋팅 되었습니다.", Toast.LENGTH_SHORT)
//				.show();
	}

	/**
	 * 위도값을 리턴합니다.
	 * 
	 * @return
	 */
	public double getLat() {
		return mLat;
	}

	/**
	 * 경도값을 리턴합니다.
	 * 
	 * @return
	 */
	public double getLng() {
		return mLng;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLocationSetting() {
		return isLocationSetting;
	}

	/**
	 * Criteria 객체에 Accuracy를 셋팅한다.
	 * 
	 * @param accuracy
	 */
	public void setAccuracy(int accuracy) {
		mAccuracy = accuracy;
	}

	/**
	 * 위도와 경도값을 기준으로 주소값을 리턴합니다.
	 * 
	 * @param context
	 *            Activity
	 * @param lat
	 *            위도
	 * @param lon
	 *            경도
	 * @return
	 */
	public String getAddress() {
		return LocationHelper.getAddress(mContext, mLat, mLng);
	}

	/**
	 * 지정한 위도와 경도값을 기준으로 주소를 리턴합니다. static 키워드로 설정
	 * 
	 * @param lat
	 *            위도
	 * @param lng
	 *            경도
	 * @return
	 */
	public static String getAddress(Context context, double lat, double lng) {
		String addressString = "No address found";
		Geocoder gc = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(lat, lng, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				addressString = address.getAddressLine(0);
				addressString = addressString.substring(addressString
						.indexOf(" ") + 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addressString;
	}
}