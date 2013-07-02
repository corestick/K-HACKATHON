package mobi.intuitit.android.homepage;
import mobi.intuitit.android.mate.launcher.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Setting extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
	}
}
