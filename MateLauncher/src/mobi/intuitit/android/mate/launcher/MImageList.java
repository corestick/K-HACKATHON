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
		initWidget();
	}

	public static MImageList getInstance() {
		return mImageList;
	}

	private void initFlooring() {

		flooringList.add(R.drawable.m_flooring_01);
		flooringList.add(R.drawable.m_flooring_02);
		flooringList.add(R.drawable.m_flooring_03);
		flooringList.add(R.drawable.m_flooring_04);
		flooringList.add(R.drawable.m_flooring_05);
		flooringList.add(R.drawable.m_flooring_06);
		flooringList.add(R.drawable.m_flooring_07);
		flooringList.add(R.drawable.m_flooring_08);
		// flooringList.add(R.drawable.m_flooring_09);

	}

	private void initWallpapaer() {

		wallpaperList.add(R.drawable.m_wall_01);
		wallpaperList.add(R.drawable.m_wall_02);
		wallpaperList.add(R.drawable.m_wall_03);
		wallpaperList.add(R.drawable.m_wall_04);
		wallpaperList.add(R.drawable.m_wall_05);
		wallpaperList.add(R.drawable.m_wall_06);
		wallpaperList.add(R.drawable.m_wall_07);
		wallpaperList.add(R.drawable.m_wall_08);
		// wallpaperList.add(R.drawable.m_wall_09);
		// wallpaperList.add(R.drawable.m_wall_010);

	}

	private void initAvatar() {

		avatarList.add(R.drawable.m_avatar_01);
		avatarList.add(R.drawable.m_avatar_02);
		avatarList.add(R.drawable.m_avatar_03);
		avatarList.add(R.drawable.m_avatar_04);
		avatarList.add(R.drawable.m_avatar_05);
		avatarList.add(R.drawable.m_avatar_06);
		avatarList.add(R.drawable.m_avatar_07);
		avatarList.add(R.drawable.m_avatar_08);

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
		furnitureList.add(R.drawable.m_furniture_09);
		furnitureList.add(R.drawable.m_furniture_10);
		furnitureList.add(R.drawable.m_furniture_11);
		furnitureList.add(R.drawable.m_furniture_12);
		furnitureList.add(R.drawable.m_furniture_13);
		furnitureList.add(R.drawable.m_furniture_14);
		furnitureList.add(R.drawable.m_furniture_15);
		furnitureList.add(R.drawable.m_furniture_16);
		furnitureList.add(R.drawable.m_furniture_17);
		furnitureList.add(R.drawable.m_furniture_18);
		furnitureList.add(R.drawable.m_furniture_19);
		furnitureList.add(R.drawable.m_furniture_20);
	}

	private void initWidget() {
		widgetList.add(R.drawable.m_widget_01);
		widgetList.add(R.drawable.m_widget_02);
		widgetList.add(R.drawable.m_widget_03);
		widgetList.add(R.drawable.m_widget_04);
	}

	public int getIcon(int resType, int resIdx) {

		switch (resType) {
		case 0: // 가구
			return furnitureList.get(resIdx);
		case 1: // 아바타
			return avatarList.get(resIdx);
		case 2: // 위젯
			return widgetList.get(resIdx);
		default:
			return -1;
		}
	}
}
