package mobi.intuitit.android.mate.launcher;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MDockbar extends LinearLayout implements View.OnClickListener {
	
	private final int FURNITURE = 1;
	private final int WALLPAPER = 2;
	private final int FLOORING = 3;
	private final int AVATAR = 4;
	
	private ImageButton mFurniture;
	private ImageButton mWallpaper;
	private ImageButton mFlooring;
	private ImageButton mAvatar;
	private ImageButton mRight;

	Launcher mLauncher;
	Context context;
	
	Bitmap captureView[]; 
	

	public MDockbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.setPadding(2, 2, 2, 2);
		this.setBackgroundResource(R.drawable.dock);
	}

	public void initMDockbar(Launcher launcher) {
		this.mLauncher = launcher;

		mFurniture = new ImageButton(context);
		mFlooring = new ImageButton(context);
		mWallpaper = new ImageButton(context);
		mAvatar = new ImageButton(context);		
		mRight = new ImageButton(context);

		mFurniture.setBackgroundResource(R.drawable.icon_furniture);
		mFlooring.setBackgroundResource(R.drawable.icon_flooring);
		mWallpaper.setBackgroundResource(R.drawable.icon_wallpaper);
		mAvatar.setBackgroundResource(R.drawable.icon_avatar);		
		mRight.setBackgroundResource(R.drawable.icon_right);

		addView(mFurniture);
		LayoutParams params = (LayoutParams) mFurniture.getLayoutParams();
		params.weight = 50;
		mFurniture.setLayoutParams(params);

		addView(mWallpaper);
		params = (LayoutParams) mFurniture.getLayoutParams();
		params.weight = 50;
		mWallpaper.setLayoutParams(params);

		addView(mFlooring);
		params = (LayoutParams) mFlooring.getLayoutParams();
		params.weight = 50;
		mFlooring.setLayoutParams(params);

		addView(mAvatar);
		params = (LayoutParams) mAvatar.getLayoutParams();
		params.weight = 50;
		mAvatar.setLayoutParams(params);

		addView(mRight);
		params = (LayoutParams) mRight.getLayoutParams();
		params.weight = 10;
		mRight.setLayoutParams(params);

		mFurniture.setOnClickListener(this);
		mWallpaper.setOnClickListener(this);
		mFlooring.setOnClickListener(this);
		mAvatar.setOnClickListener(this);	
		mRight.setOnClickListener(this);
	}	

	public void showMDockbar() {
		this.setVisibility(VISIBLE);
	}

	public void hideMDockbar() {
		this.setVisibility(INVISIBLE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubs

		if (v.equals(mFurniture)) {
			if (mLauncher.mObjectView.mObjectViewType == FURNITURE) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView.showMojbectView(FURNITURE);
			}
			return;
		}

		if (v.equals(mWallpaper)) {

			if (mLauncher.mObjectView.mObjectViewType == WALLPAPER) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView.showMojbectView(WALLPAPER);
			}
			return;
		}

		if (v.equals(mFlooring)) {
			if (mLauncher.mObjectView.mObjectViewType == FLOORING) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView.showMojbectView(FLOORING);
			}
			return;
		}

		if (v.equals(mAvatar)) {
			if (mLauncher.mObjectView.mObjectViewType == AVATAR) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView.showMojbectView(AVATAR);
			}
			return;
		}		
			
		if (v.equals(mRight)) {
			mLauncher.mObjectView.hideMobjectView();
			hideMDockbar();
			Launcher.modifyMode = false;
			mLauncher.mDockbar.showDockbar();
			mLauncher.modifyModeOff();
			return;
		}
	}
}
