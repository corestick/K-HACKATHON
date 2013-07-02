package mobi.intuitit.android.mate.launcher;

import java.io.File;
import java.io.FileOutputStream;

import mobi.intuitit.android.homepage.HomeMain;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Dockbar extends LinearLayout implements View.OnClickListener {

	private Launcher mLauncher;
	private ImageView[] mDockButton;
	private Workspace mWorkspace;
	private View mAllAppsGrid;
	private ImageView left;
	
	Bitmap captureView[]; 

	public Dockbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundResource(R.drawable.dock);
	}

	public void setLauncher(Launcher launcher) {
		mLauncher = launcher;
	}

	public void setWorkspace(Workspace workspace) {
		mWorkspace = workspace;
	}

	public void setAllgridView(View allgridview) {
		mAllAppsGrid = allgridview;
	}

	public void CreateDockbar() {

		left = new ImageView(mLauncher);
		addView(left);
		left.setOnClickListener(this);
		LayoutParams param = (LayoutParams) left.getLayoutParams();
		param.weight = 10;
		left.setLayoutParams(param);
		Drawable drawable4 = getResources().getDrawable(R.drawable.icon_left);
		left.setImageDrawable(drawable4);

		mDockButton = new ImageView[5];
		for (int i = 0; i < 5; i++) {
			mDockButton[i] = new ImageView(mLauncher);
			addView(mDockButton[i]);
			mDockButton[i].setOnClickListener(this);
			LayoutParams param1 = (LayoutParams) mDockButton[i]
					.getLayoutParams();
			param1.weight = 50;
			mDockButton[i].setLayoutParams(param1);
		}
		
		mDockButton[0].setImageDrawable(getResources().getDrawable(R.drawable.icon_phone));
		mDockButton[1].setImageDrawable(getResources().getDrawable(R.drawable.icon_contacts));
		mDockButton[2].setImageDrawable(getResources().getDrawable(R.drawable.icon_apps));
		mDockButton[3].setImageDrawable(getResources().getDrawable(R.drawable.icon_homepage));
		mDockButton[4].setImageDrawable(getResources().getDrawable(R.drawable.icon_setting));
	}

	public void showDockbar() {
		this.setVisibility(VISIBLE);
	}

	public void hideDockbar() {
		this.setVisibility(INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mDockButton[0])) {
			Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
			mLauncher.startActivity(intent);
			return;
		} else if (v.equals(mDockButton[1])) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("content://contacts/people/"));
			mLauncher.startActivity(intent);
			return;
		} else if (v.equals(mDockButton[2])) {
			final Rect bounds = mWorkspace.mDrawerBounds;
			mLauncher.offsetBoundsToDragLayer(bounds, mAllAppsGrid);
			mAllAppsGrid.setFocusable(true);
			mAllAppsGrid.setVisibility(View.VISIBLE);
			// mSpeechBubbleview.setVisibility(View.INVISIBLE);
			return;
		} else if (v.equals(mDockButton[3])) {
			//captureView 생성
			int count = mLauncher.getWorkspace().getChildCount();
			
			if (captureView == null || captureView.length != count)
				captureView = new Bitmap[count];
			
			String sdcard = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			
			File cfile = new File(sdcard+ "/MateLauncher/Owner");
			cfile.mkdirs(); // 폴더가 없을 경우 ScreenShotTest 폴더생성
			for (int i = 0; i < count; i++) {
				View tempCapture = mLauncher.getWorkspace().getChildAt(i);
				tempCapture.buildDrawingCache();
				captureView[i] = tempCapture.getDrawingCache();
				
				String path = sdcard + "/MateLauncher/Owner/screen"+i+".jpg";		
				try {
					FileOutputStream fos = new FileOutputStream(path);
					captureView[i].compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(mLauncher,HomeMain.class);
			intent.putExtra("ChildCount", mLauncher.Child_Count());			
			mLauncher.startActivity(intent); 
			return; 
		} else if (v.equals(mDockButton[4])) {
//			Intent intent = mLauncher.getPackageManager()
//			.getLaunchIntentForPackage("com.android.mms");
			
			final Intent intent = new Intent(
					android.provider.Settings.ACTION_SETTINGS);
			mLauncher.startActivity(intent);
			return;
		} else if (v.equals(left)) {
			hideDockbar();
			Launcher.modifyMode = true;
			mLauncher.mMDockbar.showMDockbar();
			mLauncher.modifyMode();
			
			MLayout mLayout = (MLayout) mWorkspace.getChildAt(mWorkspace.getCurrentScreen());
			mLayout.hideAllAvatarView();
		}

	}
}
