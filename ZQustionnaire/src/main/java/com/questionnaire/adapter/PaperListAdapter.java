package com.questionnaire.adapter;

import java.util.List;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Paper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PaperListAdapter extends AbsListAdapter<Paper> {

	private static final String TAG = "PaperListAdapter";

	public PaperListAdapter(Context context, List<Paper> list) {
		super(context, list);
	}

	public View getView(int position, View view, ViewGroup parent) {
		HolderItem holder = null;
		if(view ==  null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.paper_list_adapter, null);
			holder = new HolderItem();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.date = (TextView) view.findViewById(R.id.date);
			holder.author = (TextView) view.findViewById(R.id.author);
			holder.description = (TextView) view.findViewById(R.id.descriprion);
			view.setTag(holder);
		} else {
			holder = (HolderItem) view.getTag();
		}
		
		Paper paper = getItem(position);
		if(Conf.DEBUG) Log.d(TAG, "paper=" + paper);
		if(paper != null){			
			holder.name.setText(paper.getName());
			holder.date.setText(formateString(R.string.paper_author, 
					paper.getAuthor()));
			holder.author.setText(formateString(R.string.paper_create_time, 
					Conf.formatTime(paper.getDate())));
			holder.description.setText(formateString(R.string.paper_description, 
					paper.getDescription()));
			
		}
		
		return view;
	}
	
	String formateString(int resId, Object arg){
		return String.format(getContext().getString(resId), arg);
		
	}

	class HolderItem{
		TextView name, author, date, description;
	}
}
