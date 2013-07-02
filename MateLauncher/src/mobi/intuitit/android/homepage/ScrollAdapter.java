package mobi.intuitit.android.homepage;

import java.util.ArrayList;

import mobi.intuitit.android.mate.launcher.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScrollAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private ArrayList<String> mRowList;
	private int resourceLayoutId;
	
	public ScrollAdapter(Context context, int textViewResourceId, ArrayList<String> rowList)
	{
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceLayoutId = textViewResourceId;
		mRowList = rowList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = mInflater.inflate(resourceLayoutId, parent, false);
		}			
		
		TextView tvTextLabel = (TextView) convertView.findViewById(R.id.guestMsg);
		tvTextLabel.setText(mRowList.get(position));
		
		
		
		return convertView;
	}

	@Override
	public int getCount()
	{
		return mRowList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mRowList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}