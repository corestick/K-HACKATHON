package mobi.intuitit.android.weatherwidget;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.location.Location;
import android.util.Log;

public class AddressMap {
	public class AddressInfo{
		public String 	mCityname;
		public int 		mWeekStnId;
		public int 		mCurStnId;
		public String 	mWeekCityCode;
		public double 	mLatitude; //위도 
		public double 	mLongitude; //경도
		
		public AddressInfo(String cityname, int curStnId, int weekStnId, String weekCitycode, double latitude, double longitude) {
			mCityname = cityname;
			mCurStnId = curStnId;
			mWeekStnId = weekStnId;
			mWeekCityCode = weekCitycode;
			mLatitude = latitude;
			mLongitude = longitude;
		}
	}

	private static final String TAG = "WWW";

	private static AddressMap mAddressMap = null;
	
	private HashMap<String, AddressInfo> mWeekAddressMap = new HashMap<String, AddressInfo>();
	
	private AddressMap() {
		mWeekAddressMap.put("서울", new AddressInfo("서울",  108,  109, "11B10101", 37.566521, 126.977963));
		mWeekAddressMap.put("인천", new AddressInfo("인천",  112,  109, "11B20201", 37.454318, 126.705472 ));
		mWeekAddressMap.put("수원", new AddressInfo("수원",  119,  109, "11B20601", 37.262565, 127.027928));
		mWeekAddressMap.put("춘천", new AddressInfo("춘천",  101,  105, "11D10301", 37.881315, 127.729971 ));
		mWeekAddressMap.put("강릉", new AddressInfo("강릉",  105,  105, "11D20501", 37.751853, 128.876057 ));
		mWeekAddressMap.put("청주", new AddressInfo("청주",  131,  131, "11C10301", 36.642434,127.489032 ));
		mWeekAddressMap.put("대전", new AddressInfo("대전",  133,  133, "11C20401", 36.350412, 127.384548 ));
		mWeekAddressMap.put("서산", new AddressInfo("서산",  129,  133, "11C20101", 36.784499,126.450317 ));
		mWeekAddressMap.put("전주", new AddressInfo("전주",  146,  146, "11F10201", 35.824224,127.147953 ));
		mWeekAddressMap.put("광주", new AddressInfo("광주",  156,  156, "11F20501", 35.159545, 126.852601 ));
		mWeekAddressMap.put("목포", new AddressInfo("목포",  165,  156, "21F20801", 34.811835, 126.392166 ));
		mWeekAddressMap.put("여수", new AddressInfo("여수",  168,  156, "11F20401", 34.760374, 127.662222 ));
		mWeekAddressMap.put("대구", new AddressInfo("대구",  143,  143, "11H10701", 35.871435,128.601445 ));
		mWeekAddressMap.put("안동", new AddressInfo("안동",  136,  143, "11H10501", 36.568354, 128.729357 ));
		mWeekAddressMap.put("부산", new AddressInfo("부산",  159,  159, "11H20201", 35.179554, 129.075642 ));
		mWeekAddressMap.put("울산", new AddressInfo("울산",  152,  159, "11H20101", 35.538377,129.31136 ));
		mWeekAddressMap.put("창원", new AddressInfo("창원",  155,  159, "11H20301", 35.270833,128.663056));
		mWeekAddressMap.put("제주", new AddressInfo("제주",  184,  184, "11G00201", 33.499621,126.531188 ));
		mWeekAddressMap.put("서귀포", new AddressInfo("서귀포", 189, 184, "11G00401", 33.25412, 126.560076 ));
	}
	
	public static AddressMap getInstance() {
		if(mAddressMap == null)
			mAddressMap = new AddressMap();
		
		return mAddressMap;
	}
	
	public int getCurStationId(String cityName){
		AddressInfo value = mWeekAddressMap.get(cityName);
		if(value != null)
			return value.mCurStnId;
		else{
			Log.e(TAG, cityName + " is not found!");
			Exception e = new Exception();
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getWeekStationId(String cityName){
		AddressInfo value = mWeekAddressMap.get(cityName);
		if(value != null)
			return value.mWeekStnId;
		else{
			Log.e(TAG, cityName + " is not found!");
			Exception e = new Exception();
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getWeekCityCode(String cityName){
		AddressInfo value = mWeekAddressMap.get(cityName);
		if(value != null)
			return value.mWeekCityCode;
		else{
			Log.e(TAG, cityName + " is not found!");
			Exception e = new Exception();
			e.printStackTrace();
			return "";
		}
	}
	
	public AddressInfo queryCloseCity(Location loc){
		Collection<String> coll = mWeekAddressMap.keySet();
		Iterator<String> iter = coll.iterator();
		
		double mindist = 9999;
		String closeCity = "";
		while(iter.hasNext()){
			String cityname = iter.next();
			AddressInfo ai = mWeekAddressMap.get(cityname);
			
			double xdiff = loc.getLongitude() - ai.mLongitude;
			double ydiff = loc.getLatitude() - ai.mLatitude;
			double dist = (xdiff * xdiff) + (ydiff * ydiff);
			if(mindist > dist) {
				mindist = dist;
				closeCity = cityname;
			}
		}
		return mWeekAddressMap.get(closeCity);
	}
}
