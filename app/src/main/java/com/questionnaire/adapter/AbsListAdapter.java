package com.questionnaire.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbsListAdapter<T> extends BaseAdapter {

	private Context mContext = null;
	private List<T> mList = null;
	
	public List<T> getDataSet(){
		return mList;
	}
	
	public void add(T data){
		if(data != null){
			mList.add(data);
			notifyDataSetChanged();
		}
	}
	
	public void update(T data,int index){
		if(index<0 || index>getCount()-1 || data == null)
			return;
		mList.set(index, data);
		notifyDataSetChanged();
	}
	
	public void delete(int index){
		if(index<0 || index>getCount()-1)
			return;
		mList.remove(index);
		notifyDataSetChanged();
	}
	
	public AbsListAdapter(Context context) {
		mContext = context;
		mList = new ArrayList<T>();
	}
	
	public AbsListAdapter(Context context,List<T> list) {
		mContext = context;
		mList = list;
	}
	
	public void setDataSet(List<T> list){
		if(list == null)
			return;
		if(!mList.isEmpty())
			mList.clear();
		mList.addAll(list);
	}
	
	public void updateDataSet(List<T> list){
		if(list == null)
			return;
		setDataSet(list);
		notifyDataSetChanged();
	}
	
	public void addData(List<T> list){
		if(list == null)
			return;
		mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public int getCount() {
		if(mList == null) return 0;
		return mList.size();
		
	}

	public T getItem(int position) {
		if(position < 0 || position >= getCount())
			return null;
		return (mList != null) ? mList.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * @return the mContext
	 */
	public Context getContext() {
		return mContext;
	}

	abstract public View getView(int position, View view, ViewGroup parent);
}
