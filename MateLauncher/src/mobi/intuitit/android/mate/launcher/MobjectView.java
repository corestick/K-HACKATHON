package mobi.intuitit.android.mate.launcher;

import java.util.ArrayList;

import android.R.bool;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MobjectView extends GridView implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
		DragSource {

	public int mObjectViewType = MGlobal.MDOCKBAR_MENU_HIDE;

	MobjectAdapter mFurnitureAdapter;
	MobjectAdapter mWallpaperAdapter;
	MobjectAdapter mFlooringAdapter;
	MobjectAdapter mAvatarAdapter;
	MobjectAdapter mWidgetAdapter;
	
	ArrayList<Mobject> mFurnitureList;
	ArrayList<Mobject> mWallpaperList;
	ArrayList<Mobject> mFlooringList;
	ArrayList<Mobject> mAvatarList;
	ArrayList<Mobject> mWidgetList;

	private DragController mDragger;
	private Launcher mLauncher;
	private Bitmap mTexture;
	private Paint mPaint;
	private int mTextureWidth;
	private int mTextureHeight;
	
	public MobjectView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MobjectView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.gridViewStyle);
	}

	public MobjectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.AllAppsGridView, defStyle, 0);
		final int textureId = a.getResourceId(
				R.styleable.AllAppsGridView_texture, 0);
		if (textureId != 0) {
			mTexture = BitmapFactory.decodeResource(getResources(), textureId);
			mTextureWidth = mTexture.getWidth();
			mTextureHeight = mTexture.getHeight();

			mPaint = new Paint();
			mPaint.setDither(false);
		}
		a.recycle();
	}

	public void initAdapter() {
		mFurnitureList = new ArrayList<Mobject>();
		mWallpaperList = new ArrayList<Mobject>();
		mFlooringList = new ArrayList<Mobject>();
		mAvatarList = new ArrayList<Mobject>();
		mWidgetList = new ArrayList<Mobject>();

		for (int i = 0; i < MImageList.getInstance().furnitureList.size(); i++) {
			Mobject mObject = new Mobject();
			mObject.icon = getResources().getDrawable(
					MImageList.getInstance().furnitureList.get(i));
			mObject.mobjectType = MGlobal.MOBJECTTYPE_FURNITURE;
			mObject.mobjectIcon = i;
						
			mFurnitureList.add(mObject);
		}
		mFurnitureAdapter = new MobjectAdapter(mLauncher, mFurnitureList);

		for (int i = 0; i < MImageList.getInstance().wallpaperList.size(); i++) {
			Mobject mObject = new Mobject();
			mObject.icon = getResources().getDrawable(
					MImageList.getInstance().wallpaperList.get(i));
			
			mWallpaperList.add(mObject);
		}
		mWallpaperAdapter = new MobjectAdapter(mLauncher, mWallpaperList);

		for (int i = 0; i < MImageList.getInstance().flooringList.size(); i++) {
			Mobject mObject = new Mobject();
			mObject.icon = getResources().getDrawable(
					MImageList.getInstance().flooringList.get(i));

			mFlooringList.add(mObject);
		}
		mFlooringAdapter = new MobjectAdapter(mLauncher, mFlooringList);

		for (int i = 0; i < MImageList.getInstance().avatarList.size(); i++) {
			Mobject mObject = new Mobject();
			mObject.icon = getResources().getDrawable(
					MImageList.getInstance().avatarList.get(i));
			mObject.mobjectType = MGlobal.MOBJECTTYPE_AVATAR;
			mObject.mobjectIcon = i;
			
			mAvatarList.add(mObject);
		}
		mAvatarAdapter = new MobjectAdapter(mLauncher, mAvatarList);
		
		for (int i = 0; i < MImageList.getInstance().widgetList.size(); i++) {
			Mobject mObject = new Mobject();
			
			mObject.icon = getResources().getDrawable(MImageList.getInstance().widgetList.get(i));
		
			mObject.mobjectType = MGlobal.MOBJECTTYPE_WIDGET;
			mObject.mobjectIcon = i;
						
			mWidgetList.add(mObject);
		}
		
		mWidgetAdapter = new MobjectAdapter(mLauncher, mWidgetList);
	}

	@Override
	public void onDropCompleted(View target, boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onFinishInflate() {
		setOnItemClickListener(this);
		setOnItemLongClickListener(this);
	}

	public boolean isOpaque() {
		return !mTexture.hasAlpha();
	}

	@Override
	public void draw(Canvas canvas) {
		final Bitmap texture = mTexture;
		final Paint paint = mPaint;

		final int width = getWidth();
		final int height = getHeight();

		final int textureWidth = mTextureWidth;
		final int textureHeight = mTextureHeight;

		int x = 0;
		int y;

		while (x < width) {
			y = 0;
			while (y < height) {
				canvas.drawBitmap(texture, x, y, paint);
				y += textureHeight;
			}
			x += textureWidth;
		}

		super.draw(canvas);
	}

	public void setDragger(DragController dragger) {
		mDragger = dragger;
	}

	void setLauncher(Launcher launcher) {
		mLauncher = launcher;
		initAdapter();
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		if (!view.isInTouchMode()) {
			return false;
		}
		if (!isDraggable())
			return false;
			
		Mobject app = (Mobject) parent.getItemAtPosition(position);			
		app = new Mobject(app);
		mDragger.startDrag(view, this, app, DragController.DRAG_ACTION_COPY);
		
		mLauncher.closeObjectView();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		if (mObjectViewType == MGlobal.MDOCKBAR_MENU_WALLPAPER) {
			Workspace mWorkspace = mLauncher.getWorkspace();
			
			MLayout mLayout = (MLayout) mWorkspace.getChildAt(mWorkspace.getCurrentScreen());
			mLayout.setWallpaperResIdx(arg2);			
			SharedPreference.putSharedPreference(mLauncher, mWorkspace.getCurrentScreen() + "|w", arg2);
			
			hideMobjectView();
		}

		if (mObjectViewType == MGlobal.MDOCKBAR_MENU_FLOORING) {
			Workspace mWorkspace = mLauncher.getWorkspace();
			
			MLayout mLayout = (MLayout) mWorkspace.getChildAt(mWorkspace.getCurrentScreen());
			mLayout.setFlooringResIdx(arg2);			
			SharedPreference.putSharedPreference(mLauncher, mWorkspace.getCurrentScreen() + "|f", arg2);
			
			mLauncher.mMDockbar.invalidate(); // µ¶¹Ù Refresh
			hideMobjectView();
		}
	}

	public void hideMobjectView() {
		mObjectViewType = MGlobal.MDOCKBAR_MENU_HIDE;
		this.setVisibility(View.GONE);
	}

	public void showMojbectView(int argType) {
		switch (argType) {
		case MGlobal.MDOCKBAR_MENU_FURNITURE:
			setAdapter(mFurnitureAdapter);
			break;
		case MGlobal.MDOCKBAR_MENU_WALLPAPER:
			setAdapter(mWallpaperAdapter);
			break;
		case MGlobal.MDOCKBAR_MENU_FLOORING:
			setAdapter(mFlooringAdapter);
			break;
		case MGlobal.MDOCKBAR_MENU_AVATAR:
			setAdapter(mAvatarAdapter);
			break;
		case MGlobal.MDOCKBAR_MENU_WIDGET:
			setAdapter(mWidgetAdapter);
			break;
		}
		setVisibility(View.VISIBLE);
		mObjectViewType = argType;
	}

	public boolean isDraggable() {
		switch (mObjectViewType) {
		case MGlobal.MDOCKBAR_MENU_FURNITURE:
		case MGlobal.MDOCKBAR_MENU_AVATAR:
		case MGlobal.MDOCKBAR_MENU_WIDGET:
			return true;
		case MGlobal.MDOCKBAR_MENU_WALLPAPER:
		case MGlobal.MDOCKBAR_MENU_FLOORING:
			return false;
		}
		return false;
	}
}