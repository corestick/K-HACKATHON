package mobi.intuitit.android.mate.launcher;

import java.util.Vector;

import android.R.integer;

public class MImageList {

	public Vector<Integer> flooringList;
	public Vector<Integer> wallpaperList;
	public Vector<Integer> avatarList;
	public Vector<Integer> furnitureList;
	public Vector<Integer> widgetList;

	private static MImageList mImageList = new MImageList();

	private MImageList() {

		flooringList = new Vector<Integer>();
		wallpaperList = new Vector<Integer>();
		avatarList = new Vector<Integer>();
		furnitureList = new Vector<Integer>();
		widgetList = new Vector<Integer>();

		initFlooring();
		initWallpapaer();
		initAvatar();
		initFurniture();
	}

	public static MImageList getInstance() {
		return mImageList;
	}

	private void initFlooring() {

		flooringList.add(R.drawable.m_flooring_01);
		flooringList.add(R.drawable.m_flooring_02);
	}

	private void initWallpapaer() {

		wallpaperList.add(R.drawable.m_wall_01);
		wallpaperList.add(R.drawable.m_wall_02);
	}

	private void initAvatar() {

		avatarList.add(R.drawable.m_avatar_01);	
		avatarList.add(R.drawable.m_avatar_02);
	}

	private void initFurniture() {
		furnitureList.add(R.drawable.m_furniture_01);
		furnitureList.add(R.drawable.m_furniture_02);
		furnitureList.add(R.drawable.m_furniture_03);
		furnitureList.add(R.drawable.m_furniture_04);
		 furnitureList.add(R.drawable.m_furniture_05);
		 furnitureList.add(R.drawable.m_furniture_06);
		furnitureList.add(R.drawable.m_furniture_07);
		furnitureList.add(R.drawable.m_furniture_08);


	}
	public int getIcon(int resType, int resIdx) {

		switch (resType) {
		case 0: // 가구
			return furnitureList.get(resIdx);
		case 1: // 아바타
			return avatarList.get(resIdx);	
		default:
			return -1;
		}
	}
}
