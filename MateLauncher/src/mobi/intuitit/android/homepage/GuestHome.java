package mobi.intuitit.android.homepage;

import java.util.ArrayList;
import java.util.Date;

import mobi.intuitit.android.mate.launcher.LauncherProvider;
import mobi.intuitit.android.mate.launcher.R;
import mobi.intuitit.android.mate.launcher.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class GuestHome extends Activity implements OnScrollListener,
		OnClickListener {

	private int[] mImagePath1 = { R.drawable.screen0, R.drawable.screen1,
			R.drawable.screen2 };
	private int[] mImagePath2 = { R.drawable.screen3, R.drawable.screen4,
			R.drawable.screen5 };
	private int[] mImagePath3 = { R.drawable.screen6, R.drawable.screen7,
			R.drawable.screen8 };
	private int[] mImagePath4 = { R.drawable.screen9, R.drawable.screen10,
			R.drawable.screen11 };

	public Intent mIntent = new Intent();

	final String serverUrl = "http://kimsunghyuntest2.appspot.com/simpleservletapp";

	private static final int TEXT_DIALOG = 0;

	private String sdcard = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	private ScrollAdapter mAdapter;
	private ListView mListView;
	private LayoutInflater mInflater;
	private ArrayList<String> mRowList;
	private boolean mLockListView;
	public int msgCnt = 20;

	// 남길말, 사진 전달받는 변수
	public String m_state;
	public int m_profile;

	// 사진, 남길말, 이름
	public TextView tv_State;
	public TextView tv_Name;
	public ImageView tv_Profile;
	// private ImageView btnRecommend;
	// private ImageView btnDown;
	// private ImageView btnWrite;

	private TextView tv_Recommend;
	private TextView tv_Download;
	private TextView tv_Comment;

	public int count_Recommend = 0;
	public int count_Download = 0;
	public int count_Comment = 4;

	public Button downButton;
	public Button likeButton;
	public Button commentButton;

	int friend_position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_home);

		mIntent = getIntent();
		m_state = mIntent.getStringExtra("state");
		m_profile = mIntent.getIntExtra("profile", 0);
		friend_position = mIntent.getIntExtra("position", 0);

		downButton = (Button) findViewById(R.id.guest_download);
		likeButton = (Button) findViewById(R.id.guest_like_it);
		commentButton = (Button) findViewById(R.id.guest_comment);
		downButton.setOnClickListener(this);
		likeButton.setOnClickListener(this);
		commentButton.setOnClickListener(this);

		// 추천, 다운, 방문 텍스트뷰
		tv_State = (TextView) findViewById(R.id.guest_state);
		tv_Profile = (ImageView) findViewById(R.id.guest_profile);
		tv_Recommend = (TextView) findViewById(R.id.guest_recommend);
		tv_Download = (TextView) findViewById(R.id.guest_down);
		tv_Comment = (TextView) findViewById(R.id.guest_visit);
		// 남길말, 사진
		tv_State = setState(m_state);
		tv_Profile = setProfile(m_profile);
		// 추천, 다운, 방문 셋
		tv_Recommend = setRecommend(count_Recommend);
		tv_Download = setDownload(count_Download);
		tv_Comment = setVisit(count_Comment);

		Gallery gallery = (Gallery) findViewById(R.id.gallery_guest);
		gallery.setAdapter(new GuestImageAdapter(this, friend_position));
		gallery.setSelection(1);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Guest_Dialog dialog = new Guest_Dialog(GuestHome.this, position);
				dialog.setCancelable(true);
				android.view.WindowManager.LayoutParams params = dialog
						.getWindow().getAttributes();
				params.width = LayoutParams.MATCH_PARENT;
				params.height = LayoutParams.MATCH_PARENT;
				dialog.getWindow().setAttributes(params);
				dialog.show();
			}
		});
	}

	class Guest_Dialog extends Dialog implements
			android.view.View.OnClickListener {

		ImageView iv;

		public Guest_Dialog(Context context, int position) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_screen);
			iv = (ImageView) findViewById(R.id.dialog_imageview);
			if (friend_position == 0) {
				iv.setImageResource(mImagePath1[position]);
			} else if (friend_position == 1) {
				iv.setImageResource(mImagePath2[position]);
			} else if (friend_position == 2) {
				iv.setImageResource(mImagePath3[position]);
			} else if (friend_position == 3) {
				iv.setImageResource(mImagePath4[position]);
			}
			// imageView.setImageURI(uri);

			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setOnClickListener(this);
		}

		public void onClick(View view) {
			if (view == iv) {
				dismiss();
			}
		}
	}

	public TextView setRecommend(int n) {
		tv_Recommend.setText("" + n);
		return tv_Recommend;
	}

	public TextView setDownload(int n) {
		tv_Download.setText("" + n);
		return tv_Download;
	}

	public TextView setVisit(int n) {
		tv_Comment.setText("" + n);
		return tv_Comment;
	}

	// 남길말, 이름, 사진 설정
	public TextView setState(String state) {
		tv_State.setText(state);
		return tv_State;
	}

	public ImageView setProfile(int profile) {
		tv_Profile.setImageResource(profile);
		return tv_Profile;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
		// 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
		int count = totalItemCount - visibleItemCount;

		if (msgCnt != 0) {
			if (firstVisibleItem >= count && totalItemCount != 0
					&& mLockListView == false) {
				// addItems(5);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	private void addItems(final int size) {
		// 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
		mLockListView = true;

		Runnable run = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < size; i++) {
					mRowList.add("안녕 " + msgCnt);
					msgCnt--;
				}

				// 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
				// 리스트뷰의 락을 해제합니다.
				mAdapter.notifyDataSetChanged();
				if (msgCnt != 0)
					mLockListView = false;
				else
					mLockListView = true;
			}
		};

		// 속도의 딜레이를 구현하기 위한 꼼수
		Handler handler = new Handler();
		handler.postDelayed(run, 5000);
	}

	public String[] getJSONString(String str) {
		int bracketCnt = 0;
		int arrCnt = 0;
		int tmpCnt = 0;
		String[] strArr = new String[100];
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '{') {
				bracketCnt++;
			}
			if (str.charAt(i) == '}') {
				bracketCnt--;
				if (bracketCnt == 0) {
					strArr[arrCnt] = str.substring(tmpCnt, i + 1);
					arrCnt++;
					tmpCnt = i + 1;
				}
			}
		}
		return strArr;
	}

	// DB 삭제와 화면지우기
	public void remove_DB() {
		LauncherProvider lp = new LauncherProvider();
		lp.delete_table();
	}

	public void insert_DB() {
		String AUTHORITY = "mobi.intuitit.android.mate.launcher.settings";
		String TABLE_FAVORITES = "favorites";
		String PARAMETER_NOTIFY = "notify";
		final Uri CONTENT_URI_NO_NOTIFICATION = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_FAVORITES + "?" + PARAMETER_NOTIFY
				+ "=false");
		final ContentValues values = new ContentValues();
		final ContentResolver cr = getContentResolver();

		String str = JSONfunctions.getJSONfromURL(serverUrl,
				String.valueOf(friend_position));

		String[] jsonArr = getJSONString(str);

		try {
			for (int i = 0; i < jsonArr.length; i++) {
				if (jsonArr[i] == null) {
					break;
				}
				JSONObject json = new JSONObject(jsonArr[i]);
				if (json.getString("wall").equals("null")) {
					values.put("container", json.getString("container"));
					values.put("screen", json.getString("screen"));
					values.put("cellX", json.getString("cellX"));
					values.put("cellY", json.getString("cellY"));
					values.put("mobjectType", json.getString("MobjectType"));
					values.put("mobjectIcon", json.getString("MobjectIcon"));
					values.put("itemType", json.getString("itemType"));
					if (json.getString("intent").equals("null") == false)
						values.put("intent", json.getString("intent"));
					else {
						String intent = null;
						values.put("intent", intent);
					}

					String contacts = null;
					values.put("contacts", contacts);

					cr.insert(CONTENT_URI_NO_NOTIFICATION, values);
				} else {
					String wall = json.getString("wall");
					String floor = json.getString("floor");
					String[] wall_arry = wall.split("-");
					String[] floor_arry = floor.split("-");
					SharedPreference.putSharedPreference(this,
							Integer.parseInt(wall_arry[0]) + "|w",
							Integer.parseInt(wall_arry[1]));
					SharedPreference.putSharedPreference(this,
							Integer.parseInt(floor_arry[0]) + "|f",
							Integer.parseInt(floor_arry[0]));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 추천, 다운로드, 코멘트 클릭 리스너
	@Override
	public void onClick(View v) {
		if (v.equals(downButton)) {
			remove_DB(); // DB 지우고, 화면 View 삭제
			downThreadAndDialog();
		} else if (v.equals(likeButton)) {
			count_Recommend++;
			tv_Recommend = setRecommend(count_Recommend);

		} else if (v.equals(commentButton)) {
			CommentDialog commentdialog = new CommentDialog(GuestHome.this);
			commentdialog.setCancelable(true);
			android.view.WindowManager.LayoutParams params = commentdialog
					.getWindow().getAttributes();
			params.width = LayoutParams.WRAP_CONTENT;
			params.height = LayoutParams.WRAP_CONTENT;
			commentdialog.getWindow().setAttributes(params);
			commentdialog.show();

		}
	}

	// 코멘트 다이어로그

	public class CommentDialog extends Dialog implements
			android.view.View.OnClickListener {
		ImageButton write;
		ListView listview;
		DataAdapter adapter;
		ArrayList<CData> alist;
		EditText et;

		public CommentDialog(Context context) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.comment_dialog);

			et = (EditText) findViewById(R.id.edit_comment);
			write = (ImageButton) findViewById(R.id.btnwrite);
			write.setOnClickListener(this);
			listview = (ListView) findViewById(R.id.comment_listview);
			alist = new ArrayList<CData>();
			adapter = new DataAdapter(this.getContext(), alist);
			listview.setAdapter(adapter);
			add("와 이쁘네요~", "김성현", R.drawable.hyun);
			add("이쁘다~", "김권섭", R.drawable.kwon);
			add("추천박고가요~", "나동규", R.drawable.na);
			add("퍼가요~", "류종원", R.drawable.ryu);
		}

		public void onClick(View view) {
			if (view == write) {
				add(et.getText(), "김권섭", R.drawable.kwon);
				et.setText("");

				count_Comment++;
				tv_Comment = setVisit(count_Comment);
			}
		}

		public void add(Editable text, String name, int profile) {
			adapter.add(new CData(getApplicationContext(), "" + text, name,
					profile));
		}

		public void add(String message, String name, int profile) {
			adapter.add(new CData(getApplicationContext(), message, name,
					profile));
		}
	}

	private class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;
			Date date = new Date();
			if (v == null) {
				view = mInflater.inflate(R.layout.comment_list, null);
			} else {
				view = v;
			}
			final CData data = this.getItem(position);
			if (data != null) {
				TextView m_name = (TextView) view
						.findViewById(R.id.comment_name);
				TextView m_comment = (TextView) view
						.findViewById(R.id.comment_message);
				ImageView m_profile = (ImageView) view
						.findViewById(R.id.comment_profile);
				TextView day = (TextView) view.findViewById(R.id.comment_day);

				String today = date.getYear() + 1900 + "."
						+ (date.getMonth() + 1) + "." + date.getDate();

				m_name.setText(data.getName());
				m_comment.setText(data.getComment());
				m_profile.setImageResource(data.getProfile());
				day.setText(today);
			}
			return view;
		}
	}

	private ProgressDialog loagindDialog; // Loading Dialog

	void downThreadAndDialog() {
		/* ProgressDialog */
		loagindDialog = ProgressDialog.show(this, "downLoading",
				"Please wait...", true, false);

		Thread thread = new Thread(new Runnable() {
			public void run() {
				insert_DB();
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loagindDialog.dismiss(); // 다이얼로그 삭제
			count_Download++;
			tv_Download = setDownload(count_Download);
		}
	};

	class CData {

		private String m_comment;
		private String m_name;
		private int m_profile;

		public CData(Context context, String p_comment, String p_name,
				int p_profile) {

			m_comment = p_comment;
			m_name = p_name;
			m_profile = p_profile;
		}

		public String getComment() {
			return m_comment;
		}

		public String getName() {
			return m_name;
		}

		public int getProfile() {
			return m_profile;
		}
	}

}