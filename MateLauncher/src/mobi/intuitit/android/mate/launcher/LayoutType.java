package mobi.intuitit.android.mate.launcher;

import java.util.ArrayList;

import mobi.intuitit.android.widget.WidgetCellLayout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

abstract public class LayoutType extends WidgetCellLayout {

	public LayoutType(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LayoutType(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	abstract int getCountX();

	abstract int getCountY();

	abstract boolean[] getOccupiedCells();

	abstract int[] rectToCell(int width, int height);

	abstract void saveThumb();

	abstract Bitmap getThumb();

	abstract CellInfo findAllVacantCells(boolean[] occupiedCells,
			View ignoreView);

	abstract CellInfo findAllVacantCellsFromOccupied(boolean[][] occupied,
			final int xCount, final int yCount);

	@Override
	protected abstract void setChildrenDrawingCacheEnabled(boolean enabled);

	@Override
	protected void setChildrenDrawnWithCacheEnabled(boolean enabled) {
		super.setChildrenDrawnWithCacheEnabled(enabled);
	}

	abstract void onDragChild(View child);

	abstract void onDropChild(View child, int[] targetXY);
	
	abstract void onDropChild(View child, int x, int y);

	abstract int[] findNearestVacantArea(int pixelX, int pixelY, int spanX,
			int spanY, CellInfo vacantCells, int[] recycle);
	
	abstract void onDropAborted(View child);
	
	abstract void pointToCellExact(int x, int y, int[] result);
	
	public static class LayoutParams extends ViewGroup.MarginLayoutParams {
		/**
		 * Horizontal location of the item in the grid.
		 */
		@ViewDebug.ExportedProperty
		public int cellX;

		/**
		 * Vertical location of the item in the grid.
		 */
		@ViewDebug.ExportedProperty
		public int cellY;

		/**
		 * Number of cells spanned horizontally by the item.
		 */
		@ViewDebug.ExportedProperty
		public int cellHSpan;

		/**
		 * Number of cells spanned vertically by the item.
		 */
		@ViewDebug.ExportedProperty
		public int cellVSpan;

		/**
		 * Is this item currently being dragged
		 */
		public boolean isDragging;

		// X coordinate of the view in the layout.
		@ViewDebug.ExportedProperty
		int x;
		// Y coordinate of the view in the layout.
		@ViewDebug.ExportedProperty
		int y;

		boolean regenerateId;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			cellHSpan = 1;
			cellVSpan = 1;
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
			cellHSpan = 1;
			cellVSpan = 1;
		}

		public LayoutParams(int cellX, int cellY, int cellHSpan, int cellVSpan) {
			super(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			this.cellX = cellX;
			this.cellY = cellY;
			this.cellHSpan = cellHSpan;
			this.cellVSpan = cellVSpan;
		}
		
		public LayoutParams(int cellX, int cellY)
		{
			super(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			this.cellX = cellX;
			this.cellY = cellY;
			this.width = LayoutParams.FILL_PARENT;
			this.height = LayoutParams.FILL_PARENT;
		}

		public void setup(int cellWidth, int cellHeight, int widthGap,
				int heightGap, int hStartPadding, int vStartPadding) {

			final int myCellHSpan = cellHSpan;
			final int myCellVSpan = cellVSpan;
			final int myCellX = cellX;
			final int myCellY = cellY;

			width = myCellHSpan * cellWidth + ((myCellHSpan - 1) * widthGap)
					- leftMargin - rightMargin;
			height = myCellVSpan * cellHeight + ((myCellVSpan - 1) * heightGap)
					- topMargin - bottomMargin;

			x = hStartPadding + myCellX * (cellWidth + widthGap) + leftMargin;
			y = vStartPadding + myCellY * (cellHeight + heightGap) + topMargin;
		}
	}

	static final class CellInfo implements ContextMenu.ContextMenuInfo {
		View cell;
		int cellX;
		int cellY;
		int spanX;
		int spanY;
		int screen;
		boolean valid;

		final ArrayList<VacantCell> vacantCells = new ArrayList<VacantCell>(
				VacantCell.POOL_LIMIT);
		int maxVacantSpanX;
		int maxVacantSpanXSpanY;
		int maxVacantSpanY;
		int maxVacantSpanYSpanX;
		final Rect current = new Rect();

		void clearVacantCells() {
			final ArrayList<VacantCell> list = vacantCells;
			final int count = list.size();

			for (int i = 0; i < count; i++)
				list.get(i).release();

			list.clear();
		}

		void findVacantCellsFromOccupied(boolean[] occupied, int xCount,
				int yCount) {
			if (cellX < 0 || cellY < 0) {
				maxVacantSpanX = maxVacantSpanXSpanY = Integer.MIN_VALUE;
				maxVacantSpanY = maxVacantSpanYSpanX = Integer.MIN_VALUE;
				clearVacantCells();
				return;
			}

			final boolean[][] unflattened = new boolean[xCount][yCount];
			for (int y = 0; y < yCount; y++) {
				for (int x = 0; x < xCount; x++) {
					unflattened[x][y] = occupied[y * xCount + x];
				}
			}

			CellLayout.findIntersectingVacantCells(this, cellX, cellY, xCount, yCount, unflattened);
		}

		/**
		 * This method can be called only once! Calling
		 * #findVacantCellsFromOccupied will restore the ability to call this
		 * method.
		 * 
		 * Finds the upper-left coordinate of the first rectangle in the grid
		 * that can hold a cell of the specified dimensions.
		 * 
		 * @param cellXY
		 *            The array that will contain the position of a vacant cell
		 *            if such a cell can be found.
		 * @param spanX
		 *            The horizontal span of the cell we want to find.
		 * @param spanY
		 *            The vertical span of the cell we want to find.
		 * 
		 * @return True if a vacant cell of the specified dimension was found,
		 *         false otherwise.
		 */
		boolean findCellForSpan(int[] cellXY, int spanX, int spanY) {
			return findCellForSpan(cellXY, spanX, spanY, true);
		}

		boolean findCellForSpan(int[] cellXY, int spanX, int spanY,
				boolean clear) {
			final ArrayList<VacantCell> list = vacantCells;
			final int count = list.size();

			boolean found = false;

			if (this.spanX >= spanX && this.spanY >= spanY) {
				cellXY[0] = cellX;
				cellXY[1] = cellY;
				found = true;
			}

			// Look for an exact match first
			for (int i = 0; i < count; i++) {
				VacantCell cell = list.get(i);
				if (cell.spanX == spanX && cell.spanY == spanY) {
					cellXY[0] = cell.cellX;
					cellXY[1] = cell.cellY;
					found = true;
					break;
				}
			}

			// Look for the first cell large enough
			for (int i = 0; i < count; i++) {
				VacantCell cell = list.get(i);
				if (cell.spanX >= spanX && cell.spanY >= spanY) {
					cellXY[0] = cell.cellX;
					cellXY[1] = cell.cellY;
					found = true;
					break;
				}
			}

			if (clear)
				clearVacantCells();
			
			return found;
		}

		@Override
		public String toString() {
			return "Cell[view=" + (cell == null ? "null" : cell.getClass())
					+ ", x=" + cellX + ", y=" + cellY + "]";
		}

		/**
		 * See View.AttachInfo.InvalidateInfo for futher explanations about the
		 * recycling mechanism. In this case, we recycle the vacant cells
		 * instances because up to several hundreds can be instanciated when the
		 * user long presses an empty cell.
		 */
		static final class VacantCell {
			int cellX;
			int cellY;
			int spanX;
			int spanY;
		
			// We can create up to 523 vacant cells on a 4x4 grid, 100 seems
			// like a reasonable compromise given the size of a VacantCell and
			// the fact that the user is not likely to touch an empty 4x4 grid
			// very often
			private static final int POOL_LIMIT = 100;
			private static final Object sLock = new Object();
		
			private static int sAcquiredCount = 0;
			private static VacantCell sRoot;
		
			private VacantCell next;
		
			static VacantCell acquire() {
				synchronized (sLock) {
					if (sRoot == null) {
						return new VacantCell();
					}
		
					VacantCell info = sRoot;
					sRoot = info.next;
					sAcquiredCount--;
		
					return info;
				}
			}
		
			void release() {
				synchronized (sLock) {
					if (sAcquiredCount < POOL_LIMIT) {
						sAcquiredCount++;
						next = sRoot;
						sRoot = this;
					}
				}
			}
		
			@Override
			public String toString() {
				return "VacantCell[x=" + cellX + ", y=" + cellY + ", spanX="
						+ spanX + ", spanY=" + spanY + "]";
			}
		}
	}
}
