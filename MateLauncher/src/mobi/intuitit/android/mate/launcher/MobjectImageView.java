package mobi.intuitit.android.mate.launcher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MobjectImageView extends ImageView {
	private boolean mBackgroundSizeChanged;
	private Drawable mBackground;

	public MobjectImageView(Context context) {
		super(context);
	}

	public MobjectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MobjectImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initMobjectView(ItemInfo appInfo) {

//		setFocusable(true);
//		mBackground = getBackground();
//		setBackgroundDrawable(null);
//		mBackground.setCallback(this);
		
		this.setImageResource(MImageList.getInstance().getIcon(
				appInfo.mobjectType, appInfo.mobjectIcon));
	}
}
