package com.questionnaire.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.content.MediaInfoItem;

import java.util.List;

public class AdapterMedia extends AbsListAdapter<MediaInfoItem> {

	private static final String TAG = "AdapterAudio";

	public AdapterMedia(Context context, List<MediaInfoItem> list) {
		super(context);
		setDataSet(list);
	}

	public View getView(int position, View view, ViewGroup parent) {
		HolderItem holder = null;
		if(view ==  null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.media_list_adapter, null);
			holder = new HolderItem();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.date = (TextView) view.findViewById(R.id.date);
			holder.author = (TextView) view.findViewById(R.id.author);
			holder.description = (TextView) view.findViewById(R.id.descriprion);
			view.setTag(holder);
		} else {
			holder = (HolderItem) view.getTag();
		}

		MediaInfoItem item = getItem(position);
		if(Conf.DEBUG) Log.d(TAG, "position= " + position + ", " + item);
		if(item != null){
			holder.name.setText(item.getName());
			holder.description.setText(item.getDescription());
			holder.author.setText(formateString(R.string.paper_author, item.getAuthor()));
			holder.date.setText(formateString(R.string.paper_create_time,  Conf.formatTime(item.getDate())));
		}
		return view;
	}
	
	private String formateString(int resId, Object arg){
		return String.format(getContext().getString(resId), arg);
		
	}

	class HolderItem{
		TextView name, author, date, description;
	}

}