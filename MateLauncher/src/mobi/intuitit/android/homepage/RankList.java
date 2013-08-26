package mobi.intuitit.android.homepage;

import java.util.ArrayList;

import mobi.intuitit.android.mate.launcher.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RankList extends Activity implements OnItemClickListener{

	private ListView listview;
	// 데이터를 연결할 Adapter
	DataAdapter adapter;
	// 데이터를 담을 자료구조
	ArrayList<CData> alist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranklist);

		listview = (ListView) findViewById(R.id.rank_listview);

		alist = new ArrayList<CData>();

		// 데이터를 받기위해 데이터어댑터 객체 선언
		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		// 리스트뷰 클릭 리스너
		listview.setOnItemClickListener(this);

		// 리스트뷰에 항목 추가

		add("어깨위의 보리", "김성현", R.drawable.hyun);
		add("반갑습니다", "나동규", R.drawable.na);
		add("늑대아이", "류종원", R.drawable.ryu);
		add("그 겨울 바람이 분다", "김권섭", R.drawable.kwon);
	}

	public void add(String state, String name, int profile) {
		adapter.add(new CData(getApplicationContext(), state, name, profile));
	}

	private class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;
			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기
			if (v == null) {
				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.rank_custom, null);
			} else {
				view = v;
			}

			TextView rank = (TextView) view.findViewById(R.id.rank_num);
			int ranknum = position;
			ranknum++;
			rank.setText(ranknum + "등");

			// 자료를 받는다.
			final CData data = this.getItem(position);

			if (data != null) { // 화면 출력

				TextView m_stateMessage = (TextView) view
						.findViewById(R.id.rank_state);
				TextView m_name = (TextView) view.findViewById(R.id.rank_name);
				// 텍스트뷰1에 getStateMessage()을 출력 즉 첫번째 인수값
				m_stateMessage.setText(data.getStateMessage());
				// 텍스트뷰2에 getName()을 출력 즉 두번째 인수값
				m_name.setText(data.getName());

				ImageView m_profile = (ImageView) view
						.findViewById(R.id.rank_photo);
				// 이미지뷰에 뿌려질 해당 이미지값을 연결 즉 세번째 인수값
				m_profile.setImageResource(data.getProfile());
			}
			return view;
		}
	}

	// CData안에 받은 값을 직접 할당
	class CData {

		private String m_state;
		private String m_name;
		private int m_profile;

		public CData(Context context, String p_state, String p_name,
				int p_profile) {

			m_state = p_state;
			m_name = p_name;
			m_profile = p_profile;
		}

		public String getStateMessage() {
			return m_state;
		}

		public String getName() {
			return m_name;
		}

		public int getProfile() {
			return m_profile;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if(position==0){
			Intent intent = new Intent(RankList.this, GuestHome.class);
			intent.putExtra("state", alist.get(position).getStateMessage());
			intent.putExtra("profile", alist.get(position).getProfile());
			intent.putExtra("position", position);
			startActivity(intent);			
		}
		else if(position==1){
			Intent intent = new Intent(RankList.this, GuestHome.class);
			intent.putExtra("state", alist.get(position).getStateMessage());
			intent.putExtra("profile", alist.get(position).getProfile());
			intent.putExtra("position", position);
			startActivity(intent);			
		}
		else if(position==2){
			Intent intent = new Intent(RankList.this, GuestHome.class);
			intent.putExtra("state", alist.get(position).getStateMessage());
			intent.putExtra("profile", alist.get(position).getProfile());
			intent.putExtra("position", position);
			startActivity(intent);			
		}
		else if(position==3){
			Intent intent = new Intent(RankList.this, GuestHome.class);
			intent.putExtra("state", alist.get(position).getStateMessage());
			intent.putExtra("profile", alist.get(position).getProfile());
			intent.putExtra("position", position);
			startActivity(intent);			
		}
	}

}
