package mobi.intuitit.android.mate.launcher;

import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MAvatarMenu extends LinearLayout implements OnClickListener {

	private int mVisibleState = INVISIBLE;

	ImageView imgCall;
	ImageView imgSMS;
	ImageView imgKakaoTalk;
	ImageView imgTwitter;
	ImageView imgFacebook;

	private Launcher mLauncher;
	MobjectImageView mOjbectImageView;

	EditText edtMsg;

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
		imgSMS = (ImageView) findViewById(R.id.imgSMS);
		imgKakaoTalk = (ImageView) findViewById(R.id.imgKakaoTalk);
		imgTwitter = (ImageView) findViewById(R.id.imgTwitter);
		imgFacebook = (ImageView) findViewById(R.id.imgFacebook);

		imgCall.setOnClickListener(this);
		imgSMS.setOnClickListener(this);
		imgKakaoTalk.setOnClickListener(this);
		imgTwitter.setOnClickListener(this);
		imgFacebook.setOnClickListener(this);
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
		} else if (v.equals(imgSMS)) {
			showMsgDialog("SMS");
		} else if (v.equals(imgKakaoTalk)) {
			showMsgDialog("Kakao");
		} else if (v.equals(imgTwitter)) {
			Intent intent = mLauncher.getPackageManager()
					.getLaunchIntentForPackage("com.twitter.android");
			mLauncher.startActivity(intent);

			if (intent == null) {
				Toast toast = Toast.makeText(getContext(), "트위터가 없습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		} else if (v.equals(imgFacebook)) {
			Intent intent = mLauncher.getPackageManager()
					.getLaunchIntentForPackage("com.facebook.katana");
			mLauncher.startActivity(intent);

			if (intent == null) {
				Toast toast = Toast.makeText(getContext(), "페이스북이 없습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}
	}

	public void showMsgDialog(final String type) {
		AlertDialog.Builder builder;
		AlertDialog dig;

		View layout = mLauncher.mInflater.inflate(R.layout.mavatar_input,
				(ViewGroup) findViewById(R.id.drag_layer));
		builder = new AlertDialog.Builder(mLauncher);
		builder.setView(layout);

		dig = builder.create();
		dig.setTitle("메시지 입력");
		edtMsg = (EditText) layout.findViewById(R.id.edtAvatarMsg);
		dig.setButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String msg = edtMsg.getText().toString();
				ItemInfo info = (ItemInfo) mOjbectImageView.getTag();

				if (msg.length() > 0) {
					if (type.equals("SMS")) {
						mLauncher.sendtoSMS(info.contacts, msg);
					} else if (type.equals("Kakao")) {
						KakaoLink kakao = new KakaoLink(mLauncher);
						kakao.openKakaoLink(mLauncher, msg,
								mLauncher.getPackageName(), "0.9.2", "UTF-8");
					}
					InputMethodManager imm = (InputMethodManager) mLauncher
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edtMsg.getWindowToken(), 0);
				}
			}
		});
		dig.show();
	}
}
