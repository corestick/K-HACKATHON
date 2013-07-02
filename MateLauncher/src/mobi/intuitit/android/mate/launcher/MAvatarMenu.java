package mobi.intuitit.android.mate.launcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MAvatarMenu extends LinearLayout implements OnClickListener {

	private int mVisibleState = INVISIBLE;

	ImageView imgCall;	

	private Launcher mLauncher;
	MobjectImageView mOjbectImageView;

	public MAvatarMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MAvatarMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void initMAvatarMenu(Launcher launcher,
			MobjectImageView mojbectImageView) {
		setVisible(mVisibleState);

		this.mLauncher = launcher;
		this.mOjbectImageView = mojbectImageView;

		imgCall = (ImageView) findViewById(R.id.imgCall);
		imgCall.setOnClickListener(this);		
	}

	public int getVisible() {
		return mVisibleState;
	}

	public void setVisible(int mVisible) {
		mVisibleState = mVisible;
		this.setVisibility(mVisibleState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(imgCall)) {
			ItemInfo info = (ItemInfo) mOjbectImageView.getTag();
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + info.contacts));
			mLauncher.startActivity(intent);
		} 
	}
}
