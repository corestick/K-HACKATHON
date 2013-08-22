package mobi.intuitit.android.weatherwidget;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.location.Location;
import android.util.Log;

public class Geocoder {
	private static final String TAG = "WWW";

	public static String reverseGeocode(Location loc) {
		String localityName = "";
		HttpURLConnection connection = null;
		URL serverAddress = null;

		try {
			//2013-04-19 the below google geocoding api address is working very good.
			//please be aware of URL being suddenly changed by google
			serverAddress = new URL("http://maps.google.com/maps/api/geocode/xml?latlng=" + Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude()) +
                    				"&sensor=true&language=ko");
			
			Log.d(TAG, "Geocode URL: " + serverAddress.toString());
			
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();
			
			Document geocoderResultDocument = null;
			try {
				// open the connection and get results as InputSource.
				connection.connect();
				InputSource geocoderResultInputSource = new InputSource(connection.getInputStream());

				// read result and parse into XML Document
				geocoderResultDocument = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(geocoderResultInputSource);
			} finally {
				connection.disconnect();
			}
			
			// prepare XPath
			XPath xpath = XPathFactory.newInstance().newXPath();

			// extract the result
			NodeList resultNodeList = null;

			// a) obtain the formatted_address field for every result
			resultNodeList = (NodeList) xpath.evaluate(	"/GeocodeResponse/result/formatted_address", geocoderResultDocument, XPathConstants.NODESET);
			
			//the line below is for showing xml output for formatted_address
			/*for (int i = 0; i < resultNodeList.getLength(); ++i) {
				Log.d(TAG, resultNodeList.item(i).getTextContent());
			}*/
			
			// according to rambar's rules, modify city name to fit into my widget UI. 
			int len = resultNodeList.getLength();
			if(len >= 3) 
				localityName = resultNodeList.item(len - 3).getTextContent();			
			else if(len >= 0)
				localityName = resultNodeList.item(0).getTextContent();
			
			if(localityName.startsWith("대한민국")) {
				localityName = localityName.substring(4).trim();
			}
			
			if(localityName.startsWith("서울특별시")) {
				localityName = "서울시 " + localityName.substring(5).trim();
			}
			
			if(localityName.startsWith("제주특별자치도")) {
				localityName = localityName.substring(7).trim();
			}
			
			if(localityName.length() > 10) {
				StringTokenizer stk = new StringTokenizer(localityName, " ");
				String token;
				localityName = "";
				int i=0;
				for(token = stk.nextToken(); stk.hasMoreTokens(); token = stk.nextToken()) {
					localityName += token + " ";
					if(++i >= 2)break;
				}
				localityName = localityName.trim();
			}
			
			Log.d(TAG, "Geocode result: " + localityName);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return localityName;
	}
}