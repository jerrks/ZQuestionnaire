package com.questionnaire.adapter;

import java.util.List;

import com.questionnaire.R;
import com.questionnaire.db.Answer;
import com.questionnaire.utils.QuestManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdapterQuestionAnswer extends AbsListAdapter<Answer>{

	public AdapterQuestionAnswer(Context context,List<Answer> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if(view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_question_answer, null);
			holder.testerInfo = (TextView) view.findViewById(R.id.tester_info);
			holder.content = (TextView) view.findViewById(R.id.content);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Answer answer = getItem(position);
		if(answer != null) {
			holder.content.setText(answer.getAnswer());
			holder.testerInfo.setText(QuestManager.getmInstance().getStatTesterInfo(answer.getTesterId()));
		}

		return view;
	}
	
	class ViewHolder{
		TextView testerInfo, content;
	}
}
