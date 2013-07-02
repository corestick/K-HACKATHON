package mobi.intuitit.android.homepage;
import android.util.Log;

import com.google.gson.Gson;


public class MyGson {
	private Class<?> name = null;
	private Gson gson = new Gson();
	
	public String toJson(Object obj){
		return gson.toJson(obj);
	}
	
	public Object fromJson(String json, String className){
		try {
			name = Class.forName("com.example.gaeclient."+className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("MyG","1"+e);
			e.printStackTrace();
			
		}
		return gson.fromJson(json, name);
	}
}
