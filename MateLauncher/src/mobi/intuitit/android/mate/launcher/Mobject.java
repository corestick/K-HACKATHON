package mobi.intuitit.android.mate.launcher;

import android.content.ContentValues;

public class Mobject extends ItemInfo {
	
	
	Mobject() {
		itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
	}

	public Mobject(Mobject info) {
		super(info);
	}

	@Override
	void onAddToDatabase(ContentValues values) {
		super.onAddToDatabase(values);
	}

	@Override
	public String toString() {
		return title.toString();
	}
}
