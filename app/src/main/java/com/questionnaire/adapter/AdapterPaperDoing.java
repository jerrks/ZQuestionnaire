package com.questionnaire.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.db.Answer;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;

public class AdapterPaperDoing extends AbsListAdapter<Subject> 
	implements OnCheckedChangeListener,
		android.widget.CompoundButton.OnCheckedChangeListener{
	
	private final int ITEM_COUNT = 3;
	private final int ITEM_CHOICE_SINGLE = 0;
	private final int ITEM_CHOICE_MULTIPLE = 1;
	private final int ITEM_ANSWER = 2;
	private int mItemType = ITEM_CHOICE_SINGLE;
	
	private final int[] mOptions = {
			R.id.choice_multiple_a,
			R.id.choice_multiple_b,
			R.id.choice_multiple_c,
			R.id.choice_multiple_d,
			R.id.choice_multiple_e,
			R.id.choice_multiple_f,
			R.id.choice_multiple_g};
	
	private final int[] mOpt = {
			R.id.choice_sigle_a,
			R.id.choice_sigle_b,
			R.id.choice_sigle_c,
			R.id.choice_sigle_d};
	
	private final String[] mLabels,mAnswer;
	
	private List<Answer> mAnswerList;
	private long mPaperId,mTesterId;
	
	public AdapterPaperDoing(Context context,long paperId) {
		super(context);
		mLabels = context.getResources()
				.getStringArray(R.array.subject_label);
		mAnswer = context.getResources()
				.getStringArray(R.array.answers);
		mAnswerList = new ArrayList<Answer>();
		mPaperId = paperId;
		setDataSet(Dao.getDaoSubject().getAll(mPaperId));
	}
	
	public void setTesterId(long testerId){
		mTesterId = testerId;
	}

	public boolean onTestComplete(){
		for(Answer a : mAnswerList){
			if(a == null)
				continue;
			if(!a.isDone())
				return false;
		}
		boolean ready = Dao.getDaoAnswer().insertAll(mAnswerList);
		if(ready){
			mAnswerList.clear();
			setDataSet(Dao.getDaoSubject().getAll(mPaperId));
		}
		return ready;
	}
	
	
	@Override
	public int getViewTypeCount() {
		return ITEM_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		Subject data = getItem(position);
		if(data != null){
			switch (data.getType()) {
			case Subject.TYPE_CHOICE_SINGLE:
				mItemType = ITEM_CHOICE_SINGLE;
				break;
				
			case Subject.TYPE_CHOICE_MUTILPE:
			case Subject.TYPE_SORT:
				mItemType = ITEM_CHOICE_MULTIPLE;
				break;
				
			case Subject.TYPE_ANSWER:
			case Subject.TYPE_CLOSE:
				mItemType = ITEM_ANSWER;
				break;
				
			default:
				break;
			}
		}
		return mItemType;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			View v = null;
			switch (mItemType) {
			case ITEM_CHOICE_SINGLE:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_choice, null);
				convertView.findViewById(R.id.choice_edit_layout).setVisibility(View.GONE);
				v = convertView.findViewById(R.id.choice_title);
				holder.title = (TextView) v.findViewById(R.id.choice_title_name);
				RadioGroup group = (RadioGroup) convertView.findViewById(R.id.choice_sigle_body);
				group.setOnCheckedChangeListener(this);
				holder.options = group;
				break;
				
			case ITEM_CHOICE_MULTIPLE:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_choice_multiple, null);
				convertView.findViewById(R.id.choice_edit_layout).setVisibility(View.GONE);
				holder.value = (TextView) convertView.findViewById(R.id.choice_title_value);
				holder.value.setVisibility(View.VISIBLE);
				v = convertView.findViewById(R.id.choice_title);
				holder.title = (TextView) v.findViewById(R.id.choice_title_name);
				v = convertView.findViewById(R.id.choice_body);
				CheckBox chbx = null;
				for(int id : mOptions){
					chbx = (CheckBox) v.findViewById(id);
					chbx.setOnCheckedChangeListener(this);
				}
				holder.options = v;
				break;
				
			case ITEM_ANSWER:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_answer_test, null);
				holder.title = (TextView) convertView.findViewById(R.id.choice_title_name);
				holder.answer = (EditText) convertView.findViewById(R.id.answer_body);
				addEditCallback(holder.answer);
				break;
				
			default:
				break;
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		fillItemViewData(position, holder,convertView);
		
		return convertView;
	}

	private void fillItemViewData(int position,ViewHolder holder,View view){
		
		Subject data = getItem(position);
		if(data == null)
			return;
		Answer answer = null;
		if(mAnswerList.size() <= position){
			answer = new Answer();
			mAnswerList.add(answer);
		}else{
			answer = mAnswerList.get(position);
		}
		
		int index = position+1;
		answer.setSubjectId(data.getId());
		answer.setPaperId(mPaperId);
		answer.setTesterId(mTesterId);
		switch (mItemType) {
		case ITEM_CHOICE_SINGLE:
				if(data != null){
					String string = String.format(
							getContext().getString(R.string.subject_label_name),
							index,
							data.getTopic());
					holder.title.setText(string);
					String[] options = data.getOptions();
					RadioButton radio = null;
					//mLabels 
					for(int i=0; i<mOpt.length; i++){
						radio = (RadioButton) holder.options.findViewById(mOpt[i]);
						if(options[i] == null){
							radio.setVisibility(View.GONE);
							radio.setChecked(false);
						}else{
							radio.setVisibility(View.VISIBLE);
							string = String.format(
									getContext().getString(R.string.subject_value_fromat),
									mLabels[i],
									options[i]);
							radio.setTag(position);
							radio.setTag(R.string.key_tag_index,i);
							radio.setText(string);
							boolean checked = mLabels[i].equals(answer.getAnswer());
							radio.setChecked(checked);
						}
					}
				}
			break;
			
		case ITEM_CHOICE_MULTIPLE:
			if(data != null){
				CheckBox chbx = null;
				String string = String.format(
						getContext().getString(R.string.subject_label_name),
						index,
						data.getTopic());
				if(data.getType() != Subject.TYPE_SORT){
					holder.value.setVisibility(View.GONE);
				}else{
					holder.value.setVisibility(View.VISIBLE);
				}
				holder.title.setText(string);
				
				String[] options = data.getOptions();
				for(int i=0; i<mOptions.length; i++){
					chbx = (CheckBox) holder.options.findViewById(mOptions[i]);
					if(options[i] == null){
						chbx.setVisibility(View.GONE);
						chbx.setChecked(false);
					}else{
						chbx.setVisibility(View.VISIBLE);
						string = String.format(
								getContext().getString(R.string.subject_value_fromat),
								mLabels[i],
								options[i]);
						chbx.setTag(position);
						chbx.setTag(R.string.key_tag_index,i);
						if(data.getType() == Subject.TYPE_SORT){
							chbx.setTag(R.string.key_tag, holder.value);
						}
						chbx.setText(string);
						String str = answer.getAnswer();
						boolean checked = !TextUtils.isEmpty(str);
						if(checked) checked = str.contains(mLabels[i]);
						chbx.setChecked(checked);
					}
				}
			
			}
			break;
		case ITEM_ANSWER:
			if(data != null){
				holder.answer.setTag(R.string.key_tag_index,position);
				holder.title.setText(String.format(
						getContext().getString(R.string.subject_label_name),
						index,
						data.getTopic()));
				String str = answer.getAnswer();
				if(!TextUtils.isEmpty(str))
					holder.answer.setText(str);
			}
			break;
		default:
			break;
		}
	}
	
	class ViewHolder{
		TextView title,value;
		View options;
		EditText answer;
	}

	private void addEditCallback(final EditText edit){
		edit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
				Object obj = edit.getTag(R.string.key_tag_index);
				if(obj != null){
					int id = Integer.valueOf(obj.toString());
					Answer a = mAnswerList.get(id);
					a.setAnswer(s.toString());
					a.setDone(true);
				}
			}
		});
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton radio = (RadioButton) group.findViewById(checkedId);
		if(radio.getTag() != null){
			int id = Integer.valueOf(radio.getTag().toString());
			int index = Integer.valueOf(radio.getTag(R.string.key_tag_index).toString());
			String str = mAnswer[index];
			Answer a = mAnswerList.get(id);
			a.setAnswer(str);
			a.setDone(true);
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = Integer.valueOf(buttonView.getTag().toString());
		int index = Integer.valueOf(buttonView.getTag(R.string.key_tag_index).toString());
		String str = mAnswer[index];
		Answer a = mAnswerList.get(id);
		Subject subj = getItem(id);
		String string = a.getAnswer();
		if(isChecked){ // checked and not contains then add it 
			if(TextUtils.isEmpty(string)){
				a.setAnswer(str);
			}else if(!string.contains(str)){
				a.setAnswer(string+str);
			}
		}else{
			if(string!=null && string.contains(str)){ //not check contains then remove it
				str = string.replace(str, "");
				a.setAnswer(str);
			}
		}
		a.setDone(true);
		if(subj.getType() == Subject.TYPE_SORT){
			Object obj = buttonView.getTag(R.string.key_tag);
			if(obj!=null && obj instanceof TextView){
				TextView tv = (TextView) obj;
				tv.setText(a.getAnswer());
			}
		}
	}
}
