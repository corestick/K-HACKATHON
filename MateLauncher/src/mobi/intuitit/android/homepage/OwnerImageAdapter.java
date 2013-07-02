package mobi.intuitit.android.homepage;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class OwnerImageAdapter extends BaseAdapter {
	private Context mContext;
	private String sdcard = Environment.getExternalStorageDirectory()
			.getAbsolutePath(); // sdcard °æ·Î	

	// private int[] mImageID = {
	private String[] mImagePath = {
			sdcard + "/MateLauncher/Owner/screen0.jpg",
			sdcard + "/MateLauncher/Owner/screen1.jpg", 
			sdcard + "/MateLauncher/Owner/screen2.jpg"
	// R.drawable.sample_0,
	// R.drawable.sample_1,
	// R.drawable.sample_2
	};

	public OwnerImageAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mImagePath.length;
		// return mImageID.length;
	}

	@Override
	public Object getItem(int position) {
		return mImagePath[position];
		// return mImageID[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);		
		Uri uri = Uri.fromFile(new File(mImagePath[position]));		
		imageView.setImageURI(uri);

		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setLayoutParams(new Gallery.LayoutParams(150, 400));		

		return imageView;
	}

}