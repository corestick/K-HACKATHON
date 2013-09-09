package mobi.intuitit.android.mate.launcher;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author 유종원 패턴 이미지를 받은 페인트 생성 바닥, 벽지(왼쪽/오른쪽) 패스 생성 경계선 패스 생성
 */
public class MBackground {

	public static MBackground mBackground = new MBackground();

	private Vector<MBack> mBackList;

	private Bitmap mBitmap;
	private Paint mPaint;
	private Paint mStrokePaint;
	private Path mPath;
	private int mWidth;
	private int mHeight;

	private float mWallPoint;

	private MBackground() {
		mBackList = new Vector<MBack>();

		MBack mBack = new MBack();
		mBack.resId = R.drawable.m_wall_01;
		mBack.leftRGB = Color.rgb(119, 204, 207);
		mBack.rightRGB = Color.rgb(99, 194, 197);
		mBack.bottomRGB = Color.rgb(139, 224, 227);
		mBackList.add(mBack);

		mBack = new MBack();
		mBack.resId = R.drawable.m_wall_02;
		mBack.leftRGB = Color.rgb(194, 215, 48);
		mBack.rightRGB = Color.rgb(174, 234, 68);
		mBack.bottomRGB = Color.rgb(159, 195, 28);
		mBackList.add(mBack);
		
		mBack = new MBack();
		mBack.resId = R.drawable.m_wall_03;
		mBack.leftRGB = Color.rgb(168, 188, 39);
		mBack.rightRGB = Color.rgb(188, 208, 59);
		mBack.bottomRGB = Color.rgb(148, 168, 19);
		mBackList.add(mBack);

	}
	
	public static MBackground getInstance() {
		return mBackground;
	}
	
	public Drawable getBackground(int argWidth, int argHeight, int argIdx) {
		
		mPath = new Path();

		this.mWidth = argWidth;
		this.mHeight = argHeight;
		mWallPoint = (float) (mHeight / 2 + (mWidth / 2) / 1.7);
		
		Bitmap bitmap = Bitmap.createBitmap(argWidth,
				argHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		
		paint.setColor(mBackList.get(argIdx).leftRGB);
		canvas.drawPath(mBackground.getLeftPath(), paint);
		
		paint.setColor(mBackList.get(argIdx).rightRGB);
		canvas.drawPath(mBackground.getRightPath(), paint);

		paint.setColor(mBackList.get(argIdx).bottomRGB);
		canvas.drawPath(mBackground.getBottomPath(), paint);

		return (Drawable) (new BitmapDrawable(bitmap));
	}

	public void setBitmap(BitmapDrawable bd) {
		mBitmap = bd.getBitmap();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(new BitmapShader(mBitmap, TileMode.REPEAT,
				TileMode.REPEAT));
	}

	public Path getLeftPath() {
		mPath.reset();

		mPath.moveTo(0, 0);
		mPath.lineTo(mWidth / 2, 0);
		mPath.lineTo(mWidth / 2, mHeight / 2);
		mPath.lineTo(0, mWallPoint);
		mPath.lineTo(0, 0);

		return mPath;
	}

	public Path getRightPath() {
		mPath.reset();

		mPath.moveTo(mWidth / 2, 0);
		mPath.lineTo(mWidth / 2, mHeight / 2);
		mPath.lineTo(mWidth, mWallPoint);
		mPath.lineTo(mWidth, 0);
		mPath.lineTo(mWidth / 2, 0);

		return mPath;
	}

	public Path getBottomPath() {
		mPath.reset();

		mPath.moveTo(0, mWallPoint);
		mPath.lineTo(mWidth / 2, mHeight / 2);
		mPath.lineTo(mWidth, mWallPoint);
		mPath.lineTo(mWidth, mHeight);
		mPath.lineTo(0, mHeight);
		mPath.lineTo(0, mWallPoint);

		return mPath;
	}

	class MBack {
		int resId;
		int leftRGB;
		int rightRGB;
		int bottomRGB;
	}
}
