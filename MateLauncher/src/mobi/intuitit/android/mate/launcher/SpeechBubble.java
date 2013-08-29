package mobi.intuitit.android.mate.launcher;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

class SpeechBubble extends LinearLayout {

	private int mVisibleState = INVISIBLE;
	
	TextView txtSpeechBubble;
	ScrollView scrollView;
	
	public SpeechBubble(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SpeechBubble(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void initSpeechBubble(ItemInfo info, MobjectImageView view) {
		setVisible(mVisibleState);
		
		txtSpeechBubble = (TextView) findViewById(R.id.txtSpeechBubble);
//		txtSpeechBubble.setText("You're probably trying to load a huge bitmap or a huge image and your device is running out of memory as seen in the stack trace. You need to be very careful with the size of the images you load and work with.");
		
		scrollView = (ScrollView) findViewById(R.id.scrollView);
	}

	public void setBubbleText(String msg) {
		txtSpeechBubble.setText(msg);
	}
	
	public int getVisible() {
		return mVisibleState;
	}
	
	public void setVisible(int mVisible) {
		mVisibleState = mVisible;
		this.setVisibility(mVisibleState);
	}
}