package mobi.intuitit.android.mate.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MDockbar extends LinearLayout implements View.OnClickListener {

	private ImageButton mFurniture;
	private ImageButton mWallpaper;
	private ImageButton mAvatar;
	private ImageButton mWidget;
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
		mWallpaper = new ImageButton(context);
		mAvatar = new ImageButton(context);
		mWidget = new ImageButton(context);
		mRight = new ImageButton(context);

		mFurniture.setBackgroundResource(R.drawable.icon_furniture);		
		mWallpaper.setBackgroundResource(R.drawable.icon_flooring);
		mAvatar.setBackgroundResource(R.drawable.icon_avatar);
		mWidget.setBackgroundResource(R.drawable.icon_wiget);
		mRight.setBackgroundResource(R.drawable.icon_home);

		addView(mFurniture);
		LayoutParams params = (LayoutParams) mFurniture.getLayoutParams();
		params.weight = 50;
		mFurniture.setLayoutParams(params);

		addView(mWallpaper);
		params = (LayoutParams) mFurniture.getLayoutParams();
		params.weight = 50;
		mWallpaper.setLayoutParams(params);	

		addView(mAvatar);
		params = (LayoutParams) mAvatar.getLayoutParams();
		params.weight = 50;
		mAvatar.setLayoutParams(params);

		addView(mWidget);
		params = (LayoutParams) mWidget.getLayoutParams();
		params.weight = 50;
		mWidget.setLayoutParams(params);

		addView(mRight);
		params = (LayoutParams) mRight.getLayoutParams();
		params.weight = 50;
		mRight.setLayoutParams(params);

		mFurniture.setOnClickListener(this);
		mWallpaper.setOnClickListener(this);		
		mAvatar.setOnClickListener(this);
		mWidget.setOnClickListener(this);
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
			if (mLauncher.mObjectView.mObjectViewType == MGlobal.MDOCKBAR_MENU_FURNITURE) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView
						.showMojbectView(MGlobal.MDOCKBAR_MENU_FURNITURE);
			}
			return;
		}

		if (v.equals(mWallpaper)) {

			if (mLauncher.mObjectView.mObjectViewType == MGlobal.MDOCKBAR_MENU_BACKGROUND) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView
						.showMojbectView(MGlobal.MDOCKBAR_MENU_BACKGROUND);
			}
			return;
		}

		if (v.equals(mAvatar)) {
			if (mLauncher.mObjectView.mObjectViewType == MGlobal.MDOCKBAR_MENU_AVATAR) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView
						.showMojbectView(MGlobal.MDOCKBAR_MENU_AVATAR);
			}
			return;
		}

		if (v.equals(mWidget)) {
			if (mLauncher.mObjectView.mObjectViewType == MGlobal.MDOCKBAR_MENU_WIDGET) {
				mLauncher.mObjectView.hideMobjectView();
			} else {
				mLauncher.mObjectView
						.showMojbectView(MGlobal.MDOCKBAR_MENU_WIDGET);
			}
			return;
		}

		if (v.equals(mRight)) {
			mLauncher.mObjectView.hideMobjectView();
			hideMDockbar();
			Launcher.modifyMode = false;
			mLauncher.mDockbar.showDockbar();
			mLauncher.modifyAnimationStop();
			return;
		}
	}
}
