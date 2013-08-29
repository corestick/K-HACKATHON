package mobi.intuitit.android.weatherwidget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class CurrentWeatherXmlParser {
    private static final String ns = null;
    
    public class CurrentWeatherXmlData {
    	public final String mStnId;
    	public final String mIcon;
    	public final String mDesc;
        public final String mTemp;
        
        private CurrentWeatherXmlData(String stn_id, String icon, String desc, String temp) {
        	mStnId = stn_id;
        	mIcon = icon;
            mDesc = desc;
            mTemp = temp;
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
    
    private String readTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "temp");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "temp");
    	return title;
    }
    
    private String readDesc(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "desc");
    	String title = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "desc");
    	return title;
    }
    
    //For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    	String result = "";
    	if (parser.next() == XmlPullParser.TEXT) {
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;
    }
   
    //Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    //to their respective "read" methods for processing. Otherwise, skips the tag.
    private CurrentWeatherXmlData readLocal(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, ns, "local");
    	String temp = "0";
    	String icon = "0";
    	String desc = "";
    	String stn_id = "";
    	
    	for(int i=0; i<parser.getAttributeCount(); i++) {
    		if(parser.getAttributeName(i).equals("stn_id"))
    			stn_id = parser.getAttributeValue(i);
    		else if(parser.getAttributeName(i).equals("icon"))
    			icon = parser.getAttributeValue(i);
    		else if(parser.getAttributeName(i).equals("desc"))
    			desc = parser.getAttributeValue(i);
    		else if(parser.getAttributeName(i).equals("ta"))
    			temp = parser.getAttributeValue(i);
    	}
		skip(parser);
		 
    	//logger.log(Level.INFO, "stn_id:" + stn_id + " temp:" + temp + " desc:" + desc);
    	return new CurrentWeatherXmlData(stn_id, icon, desc, temp);
    }
    
    private List<CurrentWeatherXmlData> readWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//logger.log(Level.INFO, "readBody");
    	List<CurrentWeatherXmlData> entries = new ArrayList<CurrentWeatherXmlData>();
        parser.require(XmlPullParser.START_TAG, ns, "weather");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("local")) {
            	entries.add(readLocal(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    
    private List<CurrentWeatherXmlData> readCurrent(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//logger.log(Level.INFO, "readWid");
        List<CurrentWeatherXmlData> list = null;

        parser.require(XmlPullParser.START_TAG, ns, "current");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("weather")) {
            	list = readWeather(parser);
            } else {
                skip(parser);
            }
        }  
        return list;
    }
    
    public List<CurrentWeatherXmlData> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
        	//logger.log(Level.INFO, "parse");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCurrent(parser);
        } finally {
            in.close();
        }
    }
    
    // Given a string representation of a URL, sets up a connection and gets
 	// an input stream.
    public List<CurrentWeatherXmlData> parseFromUrl(String urlString) throws IOException, XmlPullParserException {
		InputStream is;
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		
		// Starts the query
		conn.connect();
		is = conn.getInputStream();
		
		/*
		// for testing dummy data
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>" +
				"<current xmlns=\"current\">" +
				"<weather year=\"2013\" month=\"04\" day=\"21\" hour=\"09\">" +
				"<local stn_id=\"108\" icon=\"06\" desc=\"¸¼À½\" temp=\"100\">Åë¿µ</local></weather></current>";
		
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		return parse(bais);*/
		
		return parse(is);
 	}
}


  
