package mobi.intuitit.android.weatherwidget;

import java.io.IOException;
import java.util.List;

import mobi.intuitit.android.mate.launcher.R;
import mobi.intuitit.android.weatherwidget.CurrentWeatherXmlParser.CurrentWeatherXmlData;

import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class CurrentWeatherData {
	private static final String TAG = "WWW";
	private CurrentWeatherXmlParser mCurrentParser = new CurrentWeatherXmlParser();
	private List<CurrentWeatherXmlData> mCurrentParsedList = null;
	private String mUrl;
	private int mStnId;

	protected CurrentWeatherData() {
	}

	public void setOption(String url, int stnId) {
		mUrl = url;
		mStnId = stnId;
	}

	// private int determineResourceByIcon(int icon){
	// switch(icon) {
	// case 1: return R.drawable.sunny;
	// case 2: return R.drawable.sunny_cloudy;
	// case 3: return R.drawable.cloudy_sunny;
	// case 4: return R.drawable.cloudy;
	// case 5: return R.drawable.sunny;
	// case 6: return R.drawable.sunny;
	// case 7: return R.drawable.sunny;
	// case 8: return R.drawable.rain;
	// case 9: return R.drawable.rain;
	// case 10: return R.drawable.snow;
	// case 11: return R.drawable.snow;
	// case 15: return R.drawable.foggy;
	// case 17: return R.drawable.foggy;
	// case 18: return R.drawable.foggy;
	// default: return R.drawable.triceratops_color;
	// }
	// }

	protected void update() {
		Log.d(TAG, "URL: " + mUrl + " stnid:" + mStnId);

		try {
			mCurrentParsedList = mCurrentParser.parseFromUrl(mUrl);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	protected int getCurrentTemp() {
		int currentTemp = 0;

		for (int i = 0; i < mCurrentParsedList.size(); i++) {
			if (mCurrentParsedList.get(i).mStnId.equals("" + mStnId)) {
				currentTemp = Math.round(Float.parseFloat(mCurrentParsedList
						.get(i).mTemp));
				break;
			}
		}
		return currentTemp;
	}

	protected String getWeatherString() {
		int icon = getIcon();

		Log.e("RRR", "Icon=" + icon);

		switch (icon) {
		case 1:
			return "¸¼À½";
		case 2:
			return "±¸¸§ Á¶±Ý";
		case 3:
			return "±¸¸§ ¸¹À½";
		case 4:
			return "Èå¸²";
		case 5:
			return "¸¼À½";
		case 6:
			return "¸¼À½";
		case 7:
			return "¸¼À½";
		case 8:
			return "ºñ";
		case 9:
			return "ºñ";
		case 10:
			return "´«";
		case 11:
			return "´«";
		case 14:
			return "ºñ";
		case 15:
			return "¾È°³";
		case 17:
			return "¹Ú¹«";
		case 18:
			return "¾È°³";
		default:
			return "";
		}
	}

	protected int getIcon() {
		int icon = 0;

		if (mCurrentParsedList == null) {
			// ÀÎÅÍ³Ý ¾ÈµÇ¸é ¸¼À½
			return 1;
		} else {

			for (int i = 0; i < mCurrentParsedList.size(); i++) {
				if (mCurrentParsedList.get(i).mStnId.equals("" + mStnId)) {
					try {
						icon = Integer
								.parseInt(mCurrentParsedList.get(i).mIcon);
					} catch (NumberFormatException nfe) {
						icon = 0;
					}
					break;
				}
			}
			return icon;
		}
	}
}
