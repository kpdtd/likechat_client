/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.audio.miliao.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A {@link GridView} that supports adding header rows in a very similar way to
 * {@link android.widget.ListView}. See
 * {@link GridViewWithHeaderAndFooter#addHeaderView(View, Object, boolean)} See
 * {@link GridViewWithHeaderAndFooter#addFooterView(View, Object, boolean)}
 */
public class GridViewWithHeaderAndFooter extends GridView
{
	/** 是否处于调试模式 */
	public static boolean ms_bDebug = false;
	
	/** 列表点击监听 */
	private OnItemClickListener     m_onItemClickListener     = null;
	/** 列表长按监听 */
	private OnItemLongClickListener m_onItemLongClickListener = null;

	/**
	 * A class that represents a fixed view in a list, for example a header at
	 * the top or a footer at the bottom.
	 */
	private static class FixedViewInfo
	{
		/**
		 * The view to add to the grid
		 */
		private View   m_vieChild = null;
		/**
		 * The data backing the view. This is returned from
		 * {@link ListAdapter#getItem(int)}.
		 */
		private Object m_objData  = null;
		/**
		 * <code>true</code> if the fixed view should be selectable in the grid
		 */
		private boolean   m_bIsSelectable  = false;
		private ViewGroup m_vieContainer   = null;
		
		/**
		 * return The view to add to the grid
		 * @return
		 */
		public View getView()
		{
			return m_vieChild;
		}
		
		/**
		 * set The view to add to the grid
		 * @param view
		 */
		public void setView(final View view)
		{
			this.m_vieChild = view;
		}
		
		/**
		 * return The data backing the view. This is returned from
		 * @return
		 */
		public Object getData()
		{
			return m_objData;
		}
		
		/**
		 * set The data backing the view. This is returned from
		 * @param data
		 */
		public void setData(final Object data)
		{
			this.m_objData = data;
		}
		
		/**
		 * return if the fixed view should be selectable in the grid
		 * @return
		 */
		public boolean getSelectable()
		{
			return m_bIsSelectable;
		}
		
		/**
		 * set if the fixed view should be selectable in the grid
		 * @param bIsSelectable
		 */
		public void setSelectable(final boolean bIsSelectable)
		{
			this.m_bIsSelectable = bIsSelectable;
		}
		
		/**
		 * return container
		 * @return
		 */
		public ViewGroup getContainer()
		{
			return m_vieContainer;
		}
		
		/**
		 * set container
		 * @param container
		 */
		public void setContainer(final ViewGroup container)
		{
			this.m_vieContainer = container;
		}
	}

	private int m_nNumColumns = AUTO_FIT;
	private int m_nRowHeight  = -1;
	
	private View m_vieForMeasureRowHeight = null;
	/* log tag can be at most 23 characters */
	private static final String LOG_TAG    = "GridViewHeaderAndFooter";

	private ArrayList<FixedViewInfo> m_listHeaderViewInfo = new ArrayList<FixedViewInfo>();
	private ArrayList<FixedViewInfo> m_listFooterViewInfo = new ArrayList<FixedViewInfo>();
	
	private ListAdapter        m_adaOriginal        = null;
	private OnItemClickHandler m_onItemClickHandler = null;

	private void initHeaderGridView()
	{
	}

	public GridViewWithHeaderAndFooter(Context context)
	{
		super(context);
		initHeaderGridView();
	}

	public GridViewWithHeaderAndFooter(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initHeaderGridView();
	}

	public GridViewWithHeaderAndFooter(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initHeaderGridView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		ListAdapter adapter = getAdapter();
		
		if (adapter != null && adapter instanceof HeaderViewGridAdapter)
		{
			((HeaderViewGridAdapter) adapter).setNumColumns(getNumColumnsCompatible());
			((HeaderViewGridAdapter) adapter).setRowHeight(getRowHeight());
		}
	}

	@Override
	public void setClipChildren(boolean clipChildren)
	{
		// Ignore, since the header rows depend on not being clipped
	}

	/**
	 * Do not call this method unless you know how it works.
	 * @param clipChildren
	 */
	public void setClipChildrenSupper(boolean clipChildren)
	{
		super.setClipChildren(false);
	}

	/**
	 * Add a fixed view to appear at the top of the grid. If addHeaderView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * NOTE: Call this before calling setAdapter. This is so HeaderGridView can
	 * wrap the supplied cursor with one that will also account for header
	 * views.
	 * @param v The view to add.
	 */
	public void addHeaderView(View v)
	{
		addHeaderView(v, null, true);
	}

	/**
	 * Add a fixed view to appear at the top of the grid. If addHeaderView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * NOTE: Call this before calling setAdapter. This is so HeaderGridView can
	 * wrap the supplied cursor with one that will also account for header
	 * views.
	 * @param v The view to add.
	 * @param data Data to associate with this view
	 * @param isSelectable whether the item is selectable
	 */
	public void addHeaderView(View v, Object data, boolean isSelectable)
	{
		ListAdapter adapter = getAdapter();
		
		if (adapter != null && !(adapter instanceof HeaderViewGridAdapter))
		{
			throw new IllegalStateException("Cannot add header view to grid -- setAdapter has already been called.");
		}

		ViewGroup.LayoutParams lyp = v.getLayoutParams();

		FixedViewInfo info = new FixedViewInfo();
		FrameLayout fl = new FullWidthFixedViewLayout(getContext());

		if (lyp != null)
		{
			v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
			fl.setLayoutParams(new LayoutParams(lyp.width, lyp.height));
		}
		
		fl.addView(v);
		info.setView(v);
		info.setContainer(fl);
		info.setData(data);
		info.setSelectable(isSelectable);
		m_listHeaderViewInfo.add(info);
		
		// in the case of re-adding a header view, or adding one later on,
		// we need to notify the observer
		if (adapter != null)
		{
			((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
		}
	}

	public void addFooterView(View v)
	{
		addFooterView(v, null, true);
	}

	public void addFooterView(View v, Object data, boolean isSelectable)
	{
		ListAdapter mAdapter = getAdapter();
		
		if (mAdapter != null && !(mAdapter instanceof HeaderViewGridAdapter))
		{
			throw new IllegalStateException("Cannot add header view to grid -- setAdapter has already been called.");
		}

		ViewGroup.LayoutParams lyp = v.getLayoutParams();

		FixedViewInfo info = new FixedViewInfo();
		FrameLayout fl = new FullWidthFixedViewLayout(getContext());

		if (lyp != null)
		{
			v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
			fl.setLayoutParams(new LayoutParams(lyp.width, lyp.height));
		}
		
		fl.addView(v);
		info.setView(v);
		info.setContainer(fl);
		info.setData(data);
		info.setSelectable(isSelectable);
		m_listFooterViewInfo.add(info);

		if (mAdapter != null)
		{
			((HeaderViewGridAdapter) mAdapter).notifyDataSetChanged();
		}
	}

	public int getHeaderViewCount()
	{
		return m_listHeaderViewInfo.size();
	}

	public int getFooterViewCount()
	{
		return m_listFooterViewInfo.size();
	}

	/**
	 * Removes a previously-added header view.
	 * @param v The view to remove
	 * @return true if the view was removed, false if the view was not a header view
	 */
	public boolean removeHeaderView(View v)
	{
		if (m_listHeaderViewInfo.size() > 0)
		{
			boolean result = false;
			ListAdapter adapter = getAdapter();
			
			if (adapter != null && ((HeaderViewGridAdapter) adapter).removeHeader(v))
			{
				result = true;
			}
			
			removeFixedViewInfo(v, m_listHeaderViewInfo);
			return result;
		}
		return false;
	}

	/**
	 * Removes a previously-added footer view.
	 * @param v The view to remove
	 * @return true if the view was removed, false if the view was not a header view
	 */
	public boolean removeFooterView(View v)
	{
		if (m_listFooterViewInfo.size() > 0)
		{
			boolean result = false;
			ListAdapter adapter = getAdapter();
			
			if (adapter != null && ((HeaderViewGridAdapter) adapter).removeFooter(v))
			{
				result = true;
			}
			
			removeFixedViewInfo(v, m_listFooterViewInfo);
			return result;
		}
		return false;
	}

	private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where)
	{
		int len = where.size();
		
		for (int i = 0; i < len; ++i)
		{
			FixedViewInfo info = where.get(i);
			
			if (info.getView() == v)
			{
				where.remove(i);
				break;
			}
		}
	}

	@TargetApi(11)
	private int getNumColumnsCompatible()
	{
		if (Build.VERSION.SDK_INT >= 11)
		{
			return super.getNumColumns();
		}
		else
		{
			try
			{
				Field numColumns = GridView.class.getDeclaredField("mNumColumns");
				numColumns.setAccessible(true);
				return numColumns.getInt(this);
			}
			catch (Exception e)
			{
				if (m_nNumColumns != -1)
				{
					return m_nNumColumns;
				}
				
				throw new RuntimeException("Can not determine the mNumColumns for this API platform, please call setNumColumns to set it.");
			}
		}
	}

	@TargetApi(16)
	private int getColumnWidthCompatible()
	{
		if (Build.VERSION.SDK_INT >= 16)
		{
			return super.getColumnWidth();
		}
		else
		{
			try
			{
				Field numColumns = GridView.class.getDeclaredField("mColumnWidth");
				numColumns.setAccessible(true);
				return numColumns.getInt(this);
			}
			catch (NoSuchFieldException e)
			{
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		m_vieForMeasureRowHeight = null;
	}

	public void invalidateRowHeight()
	{
		m_nRowHeight = -1;
	}

	public int getHeaderHeight(int row)
	{
		if (row >= 0)
		{
			return m_listHeaderViewInfo.get(row).getView().getMeasuredHeight();
		}

		return 0;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public int getVerticalSpacing()
	{
		int value = 0;

		try
		{
			int currentapiVersion = Build.VERSION.SDK_INT;
			
			if (currentapiVersion < Build.VERSION_CODES.JELLY_BEAN)
			{
				Field field = GridView.class.getDeclaredField("mVerticalSpacing");
				field.setAccessible(true);
				value = field.getInt(this);
			}
			else
			{
				value = super.getVerticalSpacing();
			}

		}
		catch (Exception ignore)
		{
		}

		return value;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public int getHorizontalSpacing()
	{
		int value = 0;

		try
		{
			int currentapiVersion = Build.VERSION.SDK_INT;
			
			if (currentapiVersion < Build.VERSION_CODES.JELLY_BEAN)
			{
				Field field = GridView.class.getDeclaredField("mHorizontalSpacing");
				field.setAccessible(true);
				value = field.getInt(this);
			}
			else
			{
				value = super.getHorizontalSpacing();
			}

		}
		catch (Exception ignore)
		{
		}

		return value;
	}

	public int getRowHeight()
	{
		if (m_nRowHeight > 0)
		{
			return m_nRowHeight;
		}
		
		ListAdapter adapter = getAdapter();
		int numColumns = getNumColumnsCompatible();

		// adapter has not been set or has no views in it;
		if (adapter == null || adapter.getCount() <= numColumns * (m_listHeaderViewInfo.size() + m_listFooterViewInfo.size()))
		{
			return -1;
		}
		
		int mColumnWidth = getColumnWidthCompatible();
		View view = getAdapter().getView(numColumns * m_listHeaderViewInfo.size(), m_vieForMeasureRowHeight, this);
		LayoutParams p = (LayoutParams) view.getLayoutParams();
		
		if (p == null)
		{
			p = new LayoutParams(-1, -2, 0);
			view.setLayoutParams(p);
		}
		
		int childHeightSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, p.height);
		int childWidthSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(mColumnWidth, MeasureSpec.EXACTLY), 0, p.width);
		view.measure(childWidthSpec, childHeightSpec);
		m_vieForMeasureRowHeight = view;
		m_nRowHeight = view.getMeasuredHeight();
		return m_nRowHeight;
	}

	@TargetApi(11)
	public void tryToScrollToBottomSmoothly()
	{
		int lastPos = getAdapter().getCount() - 1;
		
		if (Build.VERSION.SDK_INT >= 11)
		{
			smoothScrollToPositionFromTop(lastPos, 0);
		}
		else
		{
			setSelection(lastPos);
		}
	}

	@TargetApi(11)
	public void tryToScrollToBottomSmoothly(int duration)
	{
		int lastPos = getAdapter().getCount() - 1;
		
		if (Build.VERSION.SDK_INT >= 11)
		{
			smoothScrollToPositionFromTop(lastPos, 0, duration);
		}
		else
		{
			setSelection(lastPos);
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		m_adaOriginal = adapter;
		
		if (m_listHeaderViewInfo.size() > 0 || m_listFooterViewInfo.size() > 0)
		{
			HeaderViewGridAdapter headerViewGridAdapter = new HeaderViewGridAdapter(m_listHeaderViewInfo, m_listFooterViewInfo, adapter);
			int numColumns = getNumColumnsCompatible();
			
			if (numColumns > 1)
			{
				headerViewGridAdapter.setNumColumns(numColumns);
			}
			
			headerViewGridAdapter.setRowHeight(getRowHeight());
			super.setAdapter(headerViewGridAdapter);
		}
		else
		{
			super.setAdapter(adapter);
		}
	}

	/**
	 * Return original adapter for convenience.
	 * @return
	 */
	public ListAdapter getOriginalAdapter()
	{
		return m_adaOriginal;
	}

	/**
	 * full width
	 */
	private class FullWidthFixedViewLayout extends FrameLayout
	{

		public FullWidthFixedViewLayout(Context context)
		{
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom)
		{
			int realLeft = GridViewWithHeaderAndFooter.this.getPaddingLeft() + getPaddingLeft();
			
			// Try to make where it should be, from left, full width
			if (realLeft != left)
			{
				offsetLeftAndRight(realLeft - left);
			}
			
			super.onLayout(changed, left, top, right, bottom);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			int targetWidth = GridViewWithHeaderAndFooter.this.getMeasuredWidth() - GridViewWithHeaderAndFooter.this.getPaddingLeft()
					- GridViewWithHeaderAndFooter.this.getPaddingRight();
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth, MeasureSpec.getMode(widthMeasureSpec));
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	public void setNumColumns(int numColumns)
	{
		super.setNumColumns(numColumns);
		m_nNumColumns = numColumns;
		ListAdapter adapter = getAdapter();
		
		if (adapter != null && adapter instanceof HeaderViewGridAdapter)
		{
			((HeaderViewGridAdapter) adapter).setNumColumns(numColumns);
		}
	}

	/**
	 * ListAdapter used when a HeaderGridView has header views. This ListAdapter
	 * wraps another one and also keeps track of the header views and their
	 * associated data objects.
	 * <p>
	 * This is intended as a base class; you will probably not need to use this
	 * class directly in your own code.
	 */
	private static class HeaderViewGridAdapter implements WrapperListAdapter, Filterable
	{
		// This is used to notify the container of updates relating to number of
		// columns
		// or headers changing, which changes the number of placeholders needed
		private ListAdapter m_adapter = null;
		private final DataSetObservable       m_dataSetObservable = new DataSetObservable();
		static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST     = new ArrayList<FixedViewInfo>();

		// This ArrayList is assumed to NOT be null.
		ArrayList<FixedViewInfo> m_listHeaderViewInfo = null;
		ArrayList<FixedViewInfo> m_listFooterViewInfo = null;
		private int m_nNumColumns = 1;
		private int m_nRowHeight  = -1;
		private final boolean m_bIsFilterable;
		boolean m_bAreAllFixedViewsSelectable   = false;
		private boolean m_bCachePlaceHoldView   = true;
		// From Recycle Bin or calling getView, this a question...
		private boolean m_bCacheFirstHeaderView = false;

		public HeaderViewGridAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footViewInfos, ListAdapter adapter)
		{
			m_adapter = adapter;
			m_bIsFilterable = adapter instanceof Filterable;
			if (headerViewInfos == null)
			{
				m_listHeaderViewInfo = EMPTY_INFO_LIST;
			}
			else
			{
				m_listHeaderViewInfo = headerViewInfos;
			}

			if (footViewInfos == null)
			{
				m_listFooterViewInfo = EMPTY_INFO_LIST;
			}
			else
			{
				m_listFooterViewInfo = footViewInfos;
			}
			m_bAreAllFixedViewsSelectable = areAllListInfosSelectable(m_listHeaderViewInfo) && areAllListInfosSelectable(m_listFooterViewInfo);
		}

		public void setNumColumns(int numColumns)
		{
			if (numColumns < 1)
			{
				return;
			}
			if (m_nNumColumns != numColumns)
			{
				m_nNumColumns = numColumns;
				notifyDataSetChanged();
			}
		}

		public void setRowHeight(int height)
		{
			m_nRowHeight = height;
		}

		public int getHeadersCount()
		{
			return m_listHeaderViewInfo.size();
		}

		public int getFootersCount()
		{
			return m_listFooterViewInfo.size();
		}

		/**
		 * @return true if this adapter doesn't contain any data. This is used
		 *         to determine whether the empty view should be displayed. A
		 *         typical implementation will return getCount() == 0 but since
		 *         getCount() includes the headers and footers, specialized
		 *         adapters might want a different behavior.
		 */
		@Override
		public boolean isEmpty()
		{
			return (m_adapter == null || m_adapter.isEmpty());
		}

		private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos)
		{
			if (infos != null)
			{
				for (FixedViewInfo info : infos)
				{
					if (!info.getSelectable())
					{
						return false;
					}
				}
			}
			return true;
		}

		public boolean removeHeader(View v)
		{
			for (int i = 0; i < m_listHeaderViewInfo.size(); i++)
			{
				FixedViewInfo info = m_listHeaderViewInfo.get(i);
				if (info.getView() == v)
				{
					m_listHeaderViewInfo.remove(i);
					m_bAreAllFixedViewsSelectable = areAllListInfosSelectable(m_listHeaderViewInfo) && areAllListInfosSelectable(m_listFooterViewInfo);
					m_dataSetObservable.notifyChanged();
					return true;
				}
			}
			return false;
		}

		public boolean removeFooter(View v)
		{
			for (int i = 0; i < m_listFooterViewInfo.size(); i++)
			{
				FixedViewInfo info = m_listFooterViewInfo.get(i);
				if (info.getView() == v)
				{
					m_listFooterViewInfo.remove(i);
					m_bAreAllFixedViewsSelectable = areAllListInfosSelectable(m_listHeaderViewInfo) && areAllListInfosSelectable(m_listFooterViewInfo);
					m_dataSetObservable.notifyChanged();
					return true;
				}
			}
			return false;
		}

		@Override
		public int getCount()
		{
			if (m_adapter != null)
			{
				return (getFootersCount() + getHeadersCount()) * m_nNumColumns + getAdapterAndPlaceHolderCount();
			}
			else
			{
				return (getFootersCount() + getHeadersCount()) * m_nNumColumns;
			}
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return m_adapter == null || m_bAreAllFixedViewsSelectable && m_adapter.areAllItemsEnabled();
		}

		private int getAdapterAndPlaceHolderCount()
		{
			return (int) (Math.ceil(1f * m_adapter.getCount() / m_nNumColumns) * m_nNumColumns);
		}

		@Override
		public boolean isEnabled(int position)
		{
			// Header (negative positions will throw an
			// IndexOutOfBoundsException)
			int numHeadersAndPlaceholders = getHeadersCount() * m_nNumColumns;
			if (position < numHeadersAndPlaceholders)
			{
				return position % m_nNumColumns == 0 && m_listHeaderViewInfo.get(position / m_nNumColumns).getSelectable();
			}

			// Adapter
			final int adjPosition = position - numHeadersAndPlaceholders;
			int adapterCount = 0;
			if (m_adapter != null)
			{
				adapterCount = getAdapterAndPlaceHolderCount();
				if (adjPosition < adapterCount)
				{
					return adjPosition < m_adapter.getCount() && m_adapter.isEnabled(adjPosition);
				}
			}

			// Footer (off-limits positions will throw an
			// IndexOutOfBoundsException)
			final int footerPosition = adjPosition - adapterCount;
			return footerPosition % m_nNumColumns == 0 && m_listFooterViewInfo.get(footerPosition / m_nNumColumns).getSelectable();
		}

		@Override
		public Object getItem(int position)
		{
			// Header (negative positions will throw an
			// ArrayIndexOutOfBoundsException)
			int numHeadersAndPlaceholders = getHeadersCount() * m_nNumColumns;
			if (position < numHeadersAndPlaceholders)
			{
				if (position % m_nNumColumns == 0)
				{
					return m_listHeaderViewInfo.get(position / m_nNumColumns).getData();
				}
				return null;
			}

			// Adapter
			final int adjPosition = position - numHeadersAndPlaceholders;
			int adapterCount = 0;
			if (m_adapter != null)
			{
				adapterCount = getAdapterAndPlaceHolderCount();
				if (adjPosition < adapterCount)
				{
					if (adjPosition < m_adapter.getCount())
					{
						return m_adapter.getItem(adjPosition);
					}
					else
					{
						return null;
					}
				}
			}

			// Footer (off-limits positions will throw an
			// IndexOutOfBoundsException)
			final int footerPosition = adjPosition - adapterCount;
			if (footerPosition % m_nNumColumns == 0)
			{
				return m_listFooterViewInfo.get(footerPosition).getData();
			}
			else
			{
				return null;
			}
		}

		@Override
		public long getItemId(int position)
		{
			int numHeadersAndPlaceholders = getHeadersCount() * m_nNumColumns;
			if (m_adapter != null && position >= numHeadersAndPlaceholders)
			{
				int adjPosition = position - numHeadersAndPlaceholders;
				int adapterCount = m_adapter.getCount();
				if (adjPosition < adapterCount)
				{
					return m_adapter.getItemId(adjPosition);
				}
			}
			return -1;
		}

		@Override
		public boolean hasStableIds()
		{
			return m_adapter != null && m_adapter.hasStableIds();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (ms_bDebug)
			{
				Log.d(LOG_TAG, String.format("getView: %s, reused: %s", position, convertView == null));
			}
			// Header (negative positions will throw an
			// ArrayIndexOutOfBoundsException)
			int numHeadersAndPlaceholders = getHeadersCount() * m_nNumColumns;
			if (position < numHeadersAndPlaceholders)
			{
				View headerViewContainer = m_listHeaderViewInfo.get(position / m_nNumColumns).getContainer();
				if (position % m_nNumColumns == 0)
				{
					return headerViewContainer;
				}
				else
				{
					if (convertView == null)
					{
						convertView = new View(parent.getContext());
					}
					// We need to do this because GridView uses the height of
					// the last item
					// in a row to determine the height for the entire row.
					convertView.setVisibility(View.INVISIBLE);
					convertView.setMinimumHeight(headerViewContainer.getHeight());
					return convertView;
				}
			}
			// Adapter
			final int adjPosition = position - numHeadersAndPlaceholders;
			int adapterCount = 0;
			if (m_adapter != null)
			{
				adapterCount = getAdapterAndPlaceHolderCount();
				if (adjPosition < adapterCount)
				{
					if (adjPosition < m_adapter.getCount())
					{
						return m_adapter.getView(adjPosition, convertView, parent);
					}
					else
					{
						if (convertView == null)
						{
							convertView = new View(parent.getContext());
						}
						convertView.setVisibility(View.INVISIBLE);
						convertView.setMinimumHeight(m_nRowHeight);
						return convertView;
					}
				}
			}
			// Footer
			final int footerPosition = adjPosition - adapterCount;
			if (footerPosition < getCount())
			{
				View footViewContainer = m_listFooterViewInfo.get(footerPosition / m_nNumColumns).getContainer();
				if (position % m_nNumColumns == 0)
				{
					return footViewContainer;
				}
				else
				{
					if (convertView == null)
					{
						convertView = new View(parent.getContext());
					}
					// We need to do this because GridView uses the height of
					// the last item
					// in a row to determine the height for the entire row.
					convertView.setVisibility(View.INVISIBLE);
					convertView.setMinimumHeight(footViewContainer.getHeight());
					return convertView;
				}
			}
			throw new ArrayIndexOutOfBoundsException(position);
		}

		@Override
		public int getItemViewType(int position)
		{

			final int numHeadersAndPlaceholders = getHeadersCount() * m_nNumColumns;
			final int adapterViewTypeStart = m_adapter == null ? 0 : m_adapter.getViewTypeCount() - 1;
			int type = AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
			if (m_bCachePlaceHoldView)
			{
				// Header
				if (position < numHeadersAndPlaceholders)
				{
					if (position == 0)
					{
						if (m_bCacheFirstHeaderView)
						{
							type = adapterViewTypeStart + m_listHeaderViewInfo.size() + m_listFooterViewInfo.size() + 1 + 1;
						}
					}
					if (position % m_nNumColumns != 0)
					{
						type = adapterViewTypeStart + (position / m_nNumColumns + 1);
					}
				}
			}

			// Adapter
			final int adjPosition = position - numHeadersAndPlaceholders;
			int adapterCount = 0;
			if (m_adapter != null)
			{
				adapterCount = getAdapterAndPlaceHolderCount();
				if (adjPosition >= 0 && adjPosition < adapterCount)
				{
					if (adjPosition < m_adapter.getCount())
					{
						type = m_adapter.getItemViewType(adjPosition);
					}
					else
					{
						if (m_bCachePlaceHoldView)
						{
							type = adapterViewTypeStart + m_listHeaderViewInfo.size() + 1;
						}
					}
				}
			}

			if (m_bCachePlaceHoldView)
			{
				// Footer
				final int footerPosition = adjPosition - adapterCount;
				if (footerPosition >= 0 && footerPosition < getCount() && (footerPosition % m_nNumColumns) != 0)
				{
					type = adapterViewTypeStart + m_listHeaderViewInfo.size() + 1 + (footerPosition / m_nNumColumns + 1);
				}
			}
			if (ms_bDebug)
			{
				Log.d(LOG_TAG, String.format("getItemViewType: pos: %s, result: %s", position, type, m_bCachePlaceHoldView, m_bCacheFirstHeaderView));
			}
			return type;
		}

		/**
		 * content view, content view holder, header[0], header and footer
		 * placeholder(s)
		 * 
		 * @return
		 */
		@Override
		public int getViewTypeCount()
		{
			int count = m_adapter == null ? 1 : m_adapter.getViewTypeCount();
			if (m_bCachePlaceHoldView)
			{
				int offset = m_listHeaderViewInfo.size() + 1 + m_listFooterViewInfo.size();
				if (m_bCacheFirstHeaderView)
				{
					offset += 1;
				}
				count += offset;
			}
			if (ms_bDebug)
			{
				Log.d(LOG_TAG, String.format("getViewTypeCount: %s", count));
			}
			return count;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer)
		{
			m_dataSetObservable.registerObserver(observer);
			if (m_adapter != null)
			{
				m_adapter.registerDataSetObserver(observer);
			}
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer)
		{
			m_dataSetObservable.unregisterObserver(observer);
			if (m_adapter != null)
			{
				m_adapter.unregisterDataSetObserver(observer);
			}
		}

		@Override
		public Filter getFilter()
		{
			if (m_bIsFilterable)
			{
				return ((Filterable) m_adapter).getFilter();
			}
			return null;
		}

		@Override
		public ListAdapter getWrappedAdapter()
		{
			return m_adapter;
		}

		public void notifyDataSetChanged()
		{
			m_dataSetObservable.notifyChanged();
		}
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener l)
	{
		m_onItemClickListener = l;
		super.setOnItemClickListener(getItemClickHandler());
	}

	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener listener)
	{
		m_onItemLongClickListener = listener;
		super.setOnItemLongClickListener(getItemClickHandler());
	}

	private OnItemClickHandler getItemClickHandler()
	{
		if (m_onItemClickHandler == null)
		{
			m_onItemClickHandler = new OnItemClickHandler();
		}
		return m_onItemClickHandler;
	}

	private class OnItemClickHandler implements OnItemClickListener, OnItemLongClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (m_onItemClickListener != null)
			{
				int resPos = position - getHeaderViewCount() * getNumColumnsCompatible();
				if (resPos >= 0)
				{
					m_onItemClickListener.onItemClick(parent, view, resPos, id);
				}
			}
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (m_onItemLongClickListener != null)
			{
				int resPos = position - getHeaderViewCount() * getNumColumnsCompatible();
				
				if (resPos >= 0)
				{
					m_onItemLongClickListener.onItemLongClick(parent, view, resPos, id);
				}
			}
			return true;
		}
	}
}
