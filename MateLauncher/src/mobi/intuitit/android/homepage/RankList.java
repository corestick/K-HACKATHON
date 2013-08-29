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
	// �����͸� ������ Adapter
	DataAdapter adapter;
	// �����͸� ���� �ڷᱸ��
	ArrayList<CData> alist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranklist);

		listview = (ListView) findViewById(R.id.rank_listview);

		alist = new ArrayList<CData>();

		// �����͸� �ޱ����� �����;���� ��ü ����
		adapter = new DataAdapter(this, alist);

		// ����Ʈ�信 ����� ����
		listview.setAdapter(adapter);
		// ����Ʈ�� Ŭ�� ������
		listview.setOnItemClickListener(this);

		// ����Ʈ�信 �׸� �߰�

		add("������� ����", "�輺��", R.drawable.hyun);
		add("�ݰ����ϴ�", "������", R.drawable.na);
		add("�������", "������", R.drawable.ryu);
		add("�� �ܿ� �ٶ��� �д�", "��Ǽ�", R.drawable.kwon);
	}

	public void add(String state, String name, int profile) {
		adapter.add(new CData(getApplicationContext(), state, name, profile));
	}

	private class DataAdapter extends ArrayAdapter<CData> {
		// ���̾ƿ� XML�� �о���̱� ���� ��ü
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;
			// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���
			if (v == null) {
				// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
				view = mInflater.inflate(R.layout.rank_custom, null);
			} else {
				view = v;
			}

			TextView rank = (TextView) view.findViewById(R.id.rank_num);
			int ranknum = position;
			ranknum++;
			rank.setText(ranknum + "��");

			// �ڷḦ �޴´�.
			final CData data = this.getItem(position);

			if (data != null) { // ȭ�� ���

				TextView m_stateMessage = (TextView) view
						.findViewById(R.id.rank_state);
				TextView m_name = (TextView) view.findViewById(R.id.rank_name);
				// �ؽ�Ʈ��1�� getStateMessage()�� ��� �� ù��° �μ���
				m_stateMessage.setText(data.getStateMessage());
				// �ؽ�Ʈ��2�� getName()�� ��� �� �ι�° �μ���
				m_name.setText(data.getName());

				ImageView m_profile = (ImageView) view
						.findViewById(R.id.rank_photo);
				// �̹����信 �ѷ��� �ش� �̹������� ���� �� ����° �μ���
				m_profile.setImageResource(data.getProfile());
			}
			return view;
		}
	}

	// CData�ȿ� ���� ���� ���� �Ҵ�
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