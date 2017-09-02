package com.questionnaire.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.content.MediaInfoItem;
import com.questionnaire.content.MediaManager;

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
			holder.name = (TextView) view.findViewById(R.id.name_tv);
			holder.date = (TextView) view.findViewById(R.id.date_tv);
			holder.sizeTv = (TextView) view.findViewById(R.id.size_tv);
			holder.description = (TextView) view.findViewById(R.id.description_tv);
			holder.thumbnailView = (ImageView) view.findViewById(R.id.thumbnail_view);
			view.setTag(holder);
		} else {
			holder = (HolderItem) view.getTag();
		}

		MediaInfoItem item = getItem(position);
		if(Conf.DEBUG) Log.d(TAG, "position= " + position + ", " + item);
		if(item != null){
			holder.name.setText(item.getName());
			holder.description.setText(item.getDescription());
			String time = Conf.formatTime(item.getDate());
			holder.date.setText(formateString(R.string.time_context, time));
			holder.sizeTv.setText(item.getFileSise());
			holder.thumbnailView.setImageBitmap(item.getThumbnail());
		}
		return view;
	}
	
	private String formateString(int resId, Object arg){
		return String.format(getContext().getString(resId), arg);
		
	}

	class HolderItem{
		TextView name, date, sizeTv, description;
		ImageView thumbnailView;
	}

}
