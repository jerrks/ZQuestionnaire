package com.questionnaire.adapter;

import java.util.List;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Subject;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.utils.QuestManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PaperStatisticAdapter extends AbsListAdapter<SubjectAnswerPairs> {

	private static final String TAG = "PaperStatisticAdapter";
	QuestManager mManager = null;

	public PaperStatisticAdapter(Context context) {
		super(context);
		mManager = QuestManager.getmInstance();
	}

	public PaperStatisticAdapter(Context context, List<SubjectAnswerPairs> list) {
		super(context, list);
		mManager = QuestManager.getmInstance();
	}

	public View getView(int position, View view, ViewGroup parent) {
		HolderItem holder = null;
		if(view ==  null){
			view = LayoutInflater.from(getContext())
					.inflate(R.layout.paper_statistic_item, null);
			holder = new HolderItem();
			
			holder.subject = (TextView) view.findViewById(R.id.subject);
			holder.remark = (TextView) view.findViewById(R.id.remark);
			holder.answer = (TextView) view.findViewById(R.id.answer);
			view.setTag(holder);
		} else {
			holder = (HolderItem) view.getTag();
		}
		final SubjectAnswerPairs pairs = getItem(position);
		if(Conf.DEBUG) Log.d(TAG, "pairs=" + pairs);
		Subject subject = pairs.getSubject();
		holder.subject.setText((subject.getPaperNumber() + 1) + ". "+subject.getTopic());
		String info = mManager.parseAnswersEllipsed(subject, pairs.getAnswers());
		holder.answer.setText(info);
		holder.remark.setText(subject.getTypeString());
		return view;
	}

	class HolderItem{
		TextView subject, answer, remark;
	}
}
