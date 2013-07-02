package mobi.intuitit.android.mate.launcher;

import java.util.HashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MLayout extends LayoutType {
	private boolean mPortrait;

	private final Rect mRect = new Rect();
	private final CellInfo mCellInfo = new CellInfo();

	int[] mCellXY = new int[2];

	private int mFlooringResIdx;
	private int mWallpaperResIdx;

	private Launcher mLauncher;
	private int mScreenIdx;

	private final int SPEECHBUBBLE_WIDTH = 160;
	private final int SPEECHBUBBLE_HEIGHT = 140;
	private final int SPEECHBUBBLE_BOTTOM_PADDING = 20;

	private final int MAVATARMENU_WIDTH = 300;
	private final int MAVATARMENU_HEIGHT = 60;

	public MLayout(Context context) {
		this(context, null);
	}

	public MLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CellLayout, defStyle, 0);

		a.recycle();
		setAlwaysDrawnWithCacheEnabled(false);
	}

	@Override
	public void cancelLongPress() {
		super.cancelLongPress();

		// Cancel long press for all children
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			child.cancelLongPress();
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		// Generate an id for each view, this assumes we have at most 256x256
		// cells
		// per workspace screen
		super.addView(child, index, params);
	}

	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		if (child != null) {
			Rect r = new Rect();
			child.getDrawingRect(r);
			requestRectangleOnScreen(r);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mCellInfo.screen = ((ViewGroup) getParent()).indexOfChild(this);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final CellInfo cellInfo = mCellInfo;

		if (action == MotionEvent.ACTION_DOWN) {
			final Rect frame = mRect;
			final int x = (int) ev.getX() + getScrollX();
			final int y = (int) ev.getY() + getScrollY();
			final int count = getChildCount();

			boolean found = false;
			for (int i = count - 1; i >= 0; i--) {
				final View child = getChildAt(i);

				if ((child.getVisibility()) == VISIBLE
						|| child.getAnimation() != null) {
					child.getHitRect(frame);
					if (frame.contains(x, y)) {
						final LayoutParams lp = (LayoutParams) child
								.getLayoutParams();
						cellInfo.cell = child;
						cellInfo.cellX = lp.cellX;
						cellInfo.cellY = lp.cellX;
						cellInfo.valid = true;
						found = true;
						break;
					}
				}
			}

			if (!found) {
				int cellXY[] = mCellXY;

				cellInfo.cell = null;
				cellInfo.cellX = cellXY[0];
				cellInfo.cellY = cellXY[1];
				cellInfo.valid = true;
			}
			setTag(cellInfo);
		} else if (action == MotionEvent.ACTION_UP) {
			cellInfo.cell = null;
			cellInfo.cellX = -1;
			cellInfo.cellY = -1;
			cellInfo.valid = false;
			setTag(cellInfo);
		}

		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO: currently ignoring padding

		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode == MeasureSpec.UNSPECIFIED
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			throw new RuntimeException(
					"CellLayout cannot have UNSPECIFIED dimensions");
		}

		mPortrait = heightSpecSize > widthSpecSize;

		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			if (mPortrait) {
				lp.setup(lp.width, lp.height, 0, 0, 0, 0);
			} else {
				lp.setup(lp.width, lp.height, 0, 0, 0, 0);
			}

			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
					MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
					MeasureSpec.EXACTLY);
			child.measure(childWidthMeasureSpec, childheightMeasureSpec);
		}

		setMeasuredDimension(widthSpecSize, heightSpecSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);

			if (child.getVisibility() != GONE) {

				LayoutParams lp = (LayoutParams) child.getLayoutParams();

				int childLeft = lp.cellX;
				int childTop = lp.cellY;
				int childRight;
				int childBottom;

				if (lp.width < 0)
					childRight = childLeft + child.getMeasuredWidth();
				else
					childRight = childLeft + lp.width;

				if (lp.height < 0)
					childBottom = childTop + child.getMeasuredHeight();
				else
					childBottom = childTop + lp.height;

				child.layout(childLeft, childTop, childRight, childBottom);
			
			}
		}

		loadMBackground();
	}

	Bitmap mThumb;
	private Canvas mThumbCanvas;
	private Paint mThumbPaint;

	boolean layoutDrawed = false;

	private void initThumb(int width, int height) {
		if (mThumb == null || mThumb.isRecycled())
			mThumb = Bitmap
					.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Matrix matrix = new Matrix();
		matrix.setScale(0.25f, 0.25f);
		mThumbCanvas = new Canvas(mThumb);
		mThumbCanvas.concat(matrix);

		mThumbPaint = new Paint();
		mThumbPaint.setDither(true);
		mThumbPaint.setAntiAlias(true);
	}

	synchronized void saveThumb() {
		// if (layoutDrawed) 벽지, 바닥 바뀌면 적용을 위해
		// return;

		if (mThumbCanvas == null)
			initThumb(getWidth() >> 2, getHeight() >> 2);

		setDrawingCacheEnabled(true);

		// Get bitmap
		Bitmap bmp = getDrawingCache();
		if (bmp != null) {
			mThumbCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
			mThumbCanvas.drawBitmap(bmp, 0, 0, mThumbPaint);
		}
		// Clean up
		destroyDrawingCache();
		setDrawingCacheEnabled(false);
		layoutDrawed = true;
	}

	public Bitmap getThumb() {
		return mThumb;
	}

	@Override
	protected void setChildrenDrawingCacheEnabled(boolean enabled) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View view = getChildAt(i);
			view.setDrawingCacheEnabled(enabled);

			view.buildDrawingCache(true);
		}
	}

	@Override
	protected void setChildrenDrawnWithCacheEnabled(boolean enabled) {
		super.setChildrenDrawnWithCacheEnabled(enabled);
	}

	@Override
	void onDropChild(View child, int[] targetXY) {
		// TODO Auto-generated method stub

	}

	/**
	 * Drop a child at the specified position
	 * 
	 * @param child
	 *            The child that is being dropped
	 * @param targetXY
	 *            Destination area to move to
	 */
	void onDropChild(View child, int x, int y) {

		if (child != null) {
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.cellX = x;
			lp.cellY = y;
			lp.isDragging = false;
			child.requestLayout();

			child.bringToFront();
			invalidate();
		}
	}

	void onDropAborted(View child) {

		if (child != null) {
			((LayoutParams) child.getLayoutParams()).isDragging = false;
			invalidate();
		}
	}

	/**
	 * Start dragging the specified child
	 * 
	 * @param child
	 *            The child that is being dragged
	 */
	void onDragChild(View child) {

		LayoutParams lp = (LayoutParams) child.getLayoutParams();
		lp.isDragging = true;		
	}

	public int[] rectToCell(int width, int height) {
		return new int[] { 0, 0 };
	}

	void onDragOverChild(View child, int cellX, int cellY) {
		invalidate();
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	int getCountX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	int getCountY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	boolean[] getOccupiedCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	CellInfo findAllVacantCells(boolean[] occupiedCells, View ignoreView) {
		// TODO Auto-generated method stub

		// / 생성 위치 부분 수정 해야 한다.
		CellInfo cellInfo = new CellInfo();

		// /cellInfo.cellX = -1;
		// /cellInfo.cellY = -1;
		cellInfo.screen = mCellInfo.screen;

		cellInfo.valid = true;

		return cellInfo;
	}

	@Override
	CellInfo findAllVacantCellsFromOccupied(boolean[][] occupied, int xCount,
			int yCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	int[] findNearestVacantArea(int pixelX, int pixelY, int spanX, int spanY,
			CellInfo vacantCells, int[] recycle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void pointToCellExact(int x, int y, int[] result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}

	public void initMLayout(Launcher launcher, int screenIdx) {
		this.mLauncher = launcher;
		this.mScreenIdx = screenIdx;
	}

	public void setWallpaperResIdx(int idx) {
		this.mWallpaperResIdx = idx;
		drawMBackground();
	}

	public void setFlooringResIdx(int idx) {
		this.mFlooringResIdx = idx;
		drawMBackground();
	}

	public void loadMBackground() {
		int wIdx = SharedPreference.getIntSharedPreference(mLauncher,
				mScreenIdx + "|w");
		wIdx = wIdx > 0 ? wIdx : 0;
		this.mWallpaperResIdx = wIdx;

		int fIdx = SharedPreference.getIntSharedPreference(mLauncher,
				mScreenIdx + "|f");
		fIdx = fIdx > 0 ? fIdx : 0;
		this.mFlooringResIdx = fIdx;

		drawMBackground();
	}

	public void drawMBackground() {
		if (this.getWidth() > 0 && this.getHeight() > 0) {
			SharedPreference.putSharedPreference(mLauncher, mScreenIdx + "|w",
					mWallpaperResIdx);
			SharedPreference.putSharedPreference(mLauncher, mScreenIdx + "|f",
					mFlooringResIdx);

			Bitmap bitmap = Bitmap.createBitmap(this.getWidth(),
					this.getHeight(), Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmap);

			MBackground mBack = new MBackground(canvas.getWidth(),
					canvas.getHeight());

			// 벽지 그리기
			mBack.setBitmap((BitmapDrawable) getResources().getDrawable(
					MImageList.getInstance().wallpaperList
							.get(mWallpaperResIdx)));
			canvas.drawPath(mBack.getLeftPath(), mBack.getPaint());
			canvas.drawPath(mBack.getRightPath(), mBack.getPaint());

			// 바닥 그리기
			mBack.setBitmap((BitmapDrawable) getResources().getDrawable(
					MImageList.getInstance().flooringList.get(mFlooringResIdx)));
			canvas.drawPath(mBack.getBottomPath(), mBack.getPaint());

			// 테투리 그리기
			canvas.drawPath(mBack.getStrokePath(), mBack.getStrokePaint());

			this.setBackgroundDrawable((Drawable) (new BitmapDrawable(bitmap)));
		}
	}

	public int getWallpaperResIdx() {
		return this.mWallpaperResIdx;
	}

	public int getFlooringResIdx() {
		return this.mFlooringResIdx;
	}
}
