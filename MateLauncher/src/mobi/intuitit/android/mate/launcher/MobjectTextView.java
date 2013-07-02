package mobi.intuitit.android.mate.launcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class MobjectTextView extends TextView {
	private boolean mBackgroundSizeChanged;
	private Drawable mBackground;

	public MobjectTextView(Context context) {
		super(context);
	}

	public MobjectTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MobjectTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initMobjectView() {

		setFocusable(true);
		mBackground = getBackground();
		setBackgroundDrawable(null);
		mBackground.setCallback(this);

		ItemInfo info = (ItemInfo) getTag();
//		this.setBackgroundResource(MImageList.getInstance().getIcon(
//				info.mobjectType, info.mobjectIcon));
		
		this.setCompoundDrawablesWithIntrinsicBounds(0, MImageList.getInstance().getIcon(
				info.mobjectType, info.mobjectIcon), 0, 0);	
	}

	public void setTitle(boolean isModifyMode) {
		if (isModifyMode) {
			ItemInfo info = (ItemInfo) this.getTag();

			if (info.contact_num != null)
				this.setText(info.contact_name);
			else
				this.setText(info.title);
		} else {
			this.setText("");
		}
	}

	public void startAnimation() {
		Animation anim = new RotateAnimation(-1, 1, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(300);

		this.startAnimation(anim);
////	수정모드에서 타이틀 표시
//		setTitle(true);
	}

	@Override
	protected boolean setFrame(int left, int top, int right, int bottom) {
		if (getLeft() != left || getRight() != right || getTop() != top
				|| getBottom() != bottom) {
			mBackgroundSizeChanged = true;
		}
		return super.setFrame(left, top, right, bottom);
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return who == mBackground || super.verifyDrawable(who);
	}

	@Override
	protected void drawableStateChanged() {
		Drawable d = mBackground;
		if (d != null && d.isStateful()) {
			d.setState(getDrawableState());
		}
		super.drawableStateChanged();
	}

	@Override
	public void draw(Canvas canvas) {
		final Drawable background = mBackground;
		if (background != null) {
			final int scrollX = getScrollX();
			final int scrollY = getScrollY();

			if (mBackgroundSizeChanged) {
				background.setBounds(0, 0, getRight() - getLeft(), getBottom()
						- getTop());
				mBackgroundSizeChanged = false;
			}

			if ((scrollX | scrollY) == 0) {
				background.draw(canvas);
			} else {
				canvas.translate(scrollX, scrollY);
				background.draw(canvas);
				canvas.translate(-scrollX, -scrollY);
			}
		}

		super.draw(canvas);
	}
}
