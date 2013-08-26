package mobi.intuitit.android.mate.launcher;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;


/**
 * 
 * @author 유종원
 * 패턴 이미지를 받은 페인트 생성
 * 바닥, 벽지(왼쪽/오른쪽) 패스 생성
 * 경계선 패스 생성
 */
public class MBackground {

	private Bitmap mBitmap;
	private Paint mPaint;
	private Paint mStrokePaint;
	private Path mPath;
	private int mWidth;
	private int mHeight;
	
	private float mWallPoint;

	public MBackground(int argWidth, int argHeight) {
		mPath = new Path();

		this.mWidth = argWidth;
		this.mHeight = argHeight;
		mWallPoint = (float) (mHeight/2 + (mWidth/2)/1.7);
		
		mStrokePaint = new Paint();
		mStrokePaint.setStrokeWidth(3);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setColor(Color.DKGRAY);
	}
	
	public void setBitmap(BitmapDrawable bd) {
		mBitmap = bd.getBitmap();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);		
		mPaint.setShader(new BitmapShader(mBitmap, TileMode.REPEAT,
				TileMode.MIRROR));
	}
	
	public Path getLeftPath() {
		mPath.reset();
		
		mPath.moveTo(0, 0);
		mPath.lineTo(mWidth/2, 0);
		mPath.lineTo(mWidth/2, mHeight/2);
		mPath.lineTo(0, mWallPoint);
		mPath.lineTo(0, 0);
		
		return mPath;
	}
	
	public Path getRightPath() {
		mPath.reset();
		
		mPath.moveTo(mWidth/2, 0);
		mPath.lineTo(mWidth/2, mHeight/2);
		mPath.lineTo(mWidth, mWallPoint);	
		mPath.lineTo(mWidth, 0);
		mPath.lineTo(mWidth/2, 0);
		
		return mPath;
	}
	
	public Path getBottomPath() {
		mPath.reset();
		
		mPath.moveTo(0, mWallPoint);
		mPath.lineTo(mWidth/2, mHeight/2);
		mPath.lineTo(mWidth, mWallPoint);
		mPath.lineTo(mWidth, mHeight);
		mPath.lineTo(0, mHeight);
		mPath.lineTo(0, mWallPoint);
		
		return mPath;
	}
	
	public Path getStrokePath() {
		mPath.reset();
		
		mPath.moveTo(mWidth/2, 0);
		mPath.lineTo(mWidth/2, mHeight/2);
		mPath.lineTo(0, mWallPoint);
		mPath.moveTo(mWidth/2, mHeight/2);
		mPath.lineTo(mWidth, mWallPoint);
		
		mPath.moveTo(0, 0);
		mPath.lineTo(0, mHeight);
		mPath.moveTo(mWidth, 0);
		mPath.lineTo(mWidth, mHeight);
		
		return mPath;
	}
	
	public Paint getStrokePaint() {
		return mStrokePaint;
	}
	
	public void drawPath(int argX, int argY) {
		mPath.lineTo(argX, argY);
	}

	public Path getPath() {
		return mPath;
	}

	public Paint getPaint() {
		return mPaint;
	}
}
