package mobi.intuitit.android.homepage;


import mobi.intuitit.android.mate.launcher.R;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TabHost;

public class HomeMain extends TabActivity {	
	
	static int ChildCount ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Intent intent = new Intent(this.getIntent());
		ChildCount = intent.getIntExtra("ChildCount", 0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepagemain);
		TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator("",getResources().getDrawable(R.drawable.home_home))
				.setContent(new Intent(this, OwnerHome.class)));
		tabHost.addTab(tabHost.newTabSpec("tab2")
				.setIndicator("",getResources().getDrawable(R.drawable.home_friends))
				.setContent(new Intent(this, FriendsList.class)));
		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("",getResources().getDrawable(R.drawable.home_rank))
				.setContent(new Intent(this, RankList.class)));
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator("",getResources().getDrawable(R.drawable.home_option))
				.setContent(new Intent(this, Setting.class)));				
//				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		
		tabHost.setCurrentTab(0);
	}
	
}