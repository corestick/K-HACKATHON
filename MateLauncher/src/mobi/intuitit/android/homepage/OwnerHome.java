package mobi.intuitit.android.homepage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mobi.intuitit.android.mate.launcher.R;
import mobi.intuitit.android.mate.launcher.SharedPreference;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

//import org.omg.CORBA.portable.CustomValue;

public class OwnerHome extends Activity implements OnScrollListener,
		OnClickListener {

	final String serverUrl = "http://kimsunghyuntest2.appspot.com/simpleservletapp";

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	private String sdcard = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	// private static final int TEXT_DIALOG = 0;

	private Uri mImageCaptureUri;
	private ImageView btnProfile;
	private TextView profileMsg;
	private EditText editText;
	private String data;
	private Gallery gallery; // 런처 이미지 보여주기

	private ScrollAdapter mAdapter;
	private ListView mListView;
	private LayoutInflater mInflater;
	private ArrayList<String> mRowList;
	private boolean mLockListView;
	public int msgCnt = 20;

	private TextView tv_Recommend;
	private TextView tv_Download;
	private TextView tv_Comment;

	public int count_Recommend = 0;
	public int count_Download = 0;
	public int count_Comment = 4;

	public Button uploadButton;
	public Button likeButton;
	public Button commentButton;

	public String phoneNum = "2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_owner_home);
		likeButton = (Button) findViewById(R.id.owner_like_it);
		uploadButton = (Button) findViewById(R.id.owner_download);
		commentButton = (Button) findViewById(R.id.owner_comment);

		likeButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		commentButton.setOnClickListener(this);

		btnProfile = (ImageView) findViewById(R.id.owner_profile);
		btnProfile.setImageResource(R.drawable.kwon);
		// btnProfile.setImageURI(Uri.fromFile(new File(sdcard+"screen1.jpg")));
		profileMsg = (TextView) findViewById(R.id.owner_state);

		// 추천, 다운, 방문 텍스트뷰
		tv_Recommend = (TextView) findViewById(R.id.owner_recommend);
		tv_Download = (TextView) findViewById(R.id.owner_down);
		tv_Comment = (TextView) findViewById(R.id.owner_visit);

		// 추천, 다운, 방문 셋
		tv_Recommend = setRecommend(count_Recommend);
		tv_Download = setDownload(count_Download);
		tv_Comment = setVisit(count_Comment);

		// 런처 홈스크린 보여주는 부분
		Gallery gallery = (Gallery) findViewById(R.id.gallery1);
		gallery.setAdapter(new OwnerImageAdapter(this));
		gallery.setSelection(1);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CustomDialog dialog = new CustomDialog(OwnerHome.this, position);
				dialog.setCancelable(true);
				android.view.WindowManager.LayoutParams params = dialog
						.getWindow().getAttributes();
				params.width = LayoutParams.MATCH_PARENT;
				params.height = LayoutParams.MATCH_PARENT;
				dialog.getWindow().setAttributes(params);
				dialog.show();
			}
		});

		btnProfile.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doTakePhotoAction();
					}
				};

				DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doTakeAlbumAction();
					}
				};

				DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				new AlertDialog.Builder(OwnerHome.this).setTitle("업로드할 이미지 선택")
						.setPositiveButton("사진촬영", cameraListener)
						.setNeutralButton("앨범선택", albumListener)
						.setNegativeButton("취소", cancelListener).show();

			}
		});

	}

	// 추천, 다운, 방문 수 셋
	private TextView setRecommend(int n) {
		tv_Recommend.setText("" + n);
		return tv_Recommend;
	}

	private TextView setDownload(int n) {
		tv_Download.setText("" + n);
		return tv_Download;
	}

	private TextView setVisit(int n) {
		tv_Comment.setText("" + n);
		return tv_Comment;
	}

	private void doTakePhotoAction() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String url = "tmp_" + String.valueOf(System.currentTimeMillis())
				+ ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), url));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);

		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	private void doTakeAlbumAction() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case CROP_FROM_CAMERA: {

			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			if (thumbnail != null) {
				ImageView profile = (ImageView) findViewById(R.id.owner_profile);
				profile.setImageBitmap(thumbnail);
			}
			File f = new File(mImageCaptureUri.getPath());
			if (f.exists()) {
				f.delete();
			}

			break;
		}

		case PICK_FROM_ALBUM: {
			mImageCaptureUri = data.getData();
		}

		case PICK_FROM_CAMERA: {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");

			intent.putExtra("outputX", 90);
			intent.putExtra("outputY", 90);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			startActivityForResult(intent, CROP_FROM_CAMERA);

			break;
		}
		}
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
				addItems(5);
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

	public class CustomDialog extends Dialog implements
			android.view.View.OnClickListener {
		ImageView iv;

		public CustomDialog(Context context, int position) {
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_screen);

			iv = (ImageView) findViewById(R.id.dialog_imageview);
			Uri uri = Uri.fromFile(new File(sdcard
					+ "/MateLauncher/Owner/screen" + position + ".jpg"));
			iv.setImageURI(uri);
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setOnClickListener(this);
		}

		public void onClick(View view) {
			if (view == iv) {
				dismiss();
			}
		}
	}

	public void get_DB() {
		// 핸드폰 번호 읽어오기
		// TelephonyManager telManager =
		// (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		// phoneNum = telManager.getLine1Number();

		ContentResolver contentResolver = getContentResolver();
		String AUTHORITY = "mobi.intuitit.android.mate.launcher.settings";
		String TABLE_FAVORITES = "favorites";
		String PARAMETER_NOTIFY = "notify";

		Uri uri = Uri.parse("content://" + AUTHORITY + "/" + TABLE_FAVORITES
				+ "?" + PARAMETER_NOTIFY + "=true");
		final Cursor c = contentResolver.query(uri, null, null, null, null);

		final int intentIndex = c.getColumnIndexOrThrow("intent");
		final int containerIndex = c.getColumnIndexOrThrow("container");
		final int itemTypeIndex = c.getColumnIndexOrThrow("itemType");
		final int screenIndex = c.getColumnIndexOrThrow("screen");
		final int cellXIndex = c.getColumnIndexOrThrow("cellX");
		final int cellYIndex = c.getColumnIndexOrThrow("cellY");
		final int mobjectType = c.getColumnIndexOrThrow("mobjectType");
		final int mobjectIcon = c.getColumnIndex("mobjectIcon");
		final int contactsIndex = c.getColumnIndex("contacts");

		while (c.moveToNext()) {

			String intentDescription = c.getString(intentIndex);
			int container = c.getInt(containerIndex);
			int itemType = c.getInt(itemTypeIndex);
			int screen = c.getInt(screenIndex);
			int cellX = c.getInt(cellXIndex);
			int cellY = c.getInt(cellYIndex);
			int MobjectType = c.getInt(mobjectType);
			int MobjectIcon = c.getInt(mobjectIcon);
			String contacts = c.getString(contactsIndex);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", phoneNum);
			if (intentDescription == null)
				map.put("intent", "null");
			else
				map.put("intent", intentDescription);
//			if (contacts == null)
//				map.put("contacts", "null");
//			else
//				map.put("contacts", contacts);
			map.put("container", container);
			map.put("itemType", itemType);
			map.put("screen", screen);
			map.put("cellX", cellX);
			map.put("cellY", cellY);
			map.put("MobjectType", MobjectType);
			map.put("MobjectIcon", MobjectIcon);

			map.put("wall", "null");

			JSONfunctions.postSONfromURL(serverUrl, map);
		}
		c.close();

		for (int i = 0; i < HomeMain.ChildCount; i++) {
			int wIdx = SharedPreference.getIntSharedPreference(this, i + "|w");
			int fIdx = SharedPreference.getIntSharedPreference(this, i + "|f");
			Log.e("Sh-Wall", String.valueOf(wIdx));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", phoneNum);
			map.put("wall", i + "-" + wIdx);
			map.put("floor", i + "-" + fIdx);
			JSONfunctions.postSONfromURL(serverUrl, map);
		}
	}

	// 추천, 다운로드, 코멘트 클릭 리스너
	@Override
	public void onClick(View v) {
		if (v.equals(uploadButton)) {
			Log.e("upload", "upload");
			upThreadAndDialog();
		} else if (v.equals(likeButton)) {
			count_Recommend++;
			tv_Recommend = setRecommend(count_Recommend);

		} else if (v.equals(commentButton)) {
			CommentDialog commentdialog = new CommentDialog(OwnerHome.this);
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
    void upThreadAndDialog() {
        /* ProgressDialog */
        loagindDialog = ProgressDialog.show(this, "uploading",
                "Please wait...", true, false);
        
        Thread thread = new Thread(new Runnable() {
            public void run() {
            	get_DB();
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            loagindDialog.dismiss(); // 다이얼로그 삭제
            // View갱신
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