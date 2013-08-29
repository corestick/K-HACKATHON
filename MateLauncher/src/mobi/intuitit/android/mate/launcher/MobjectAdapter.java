package mobi.intuitit.android.mate.launcher;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MobjectAdapter extends ArrayAdapter<Mobject> {
	private final LayoutInflater mInflater;

	public MobjectAdapter(Context context, ArrayList<Mobject> objects) {
		super(context, 0, objects);
		mInflater = LayoutInflater.from(context);

		init();
	}

	public void init() {

	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Mobject info = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.application_boxed, parent, false);
        }
        
        info.icon = Utilities.createIconThumbnail(info.icon, getContext());

        final TextView textView = (TextView) convertView;
        textView.setCompoundDrawablesWithIntrinsicBounds(null, info.icon, null, null);
//        textView.setText(info.title);

        return convertView;
    }
}
