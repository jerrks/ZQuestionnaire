package com.questionnaire.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.listener.OnModifySubjectListener;

public class AdapterSubject extends AbsListAdapter<Subject>{
	
	private final int ITEM_COUNT = 3;
	private final int ITEM_CHOICE_SINGLE = 0;
	private final int ITEM_CHOICE_MULTIPLE = 1;
	private final int ITEM_ANSWER = 2;
	private int mItemType = ITEM_CHOICE_SINGLE;
	
	private final static int TAG_EDIT = 0;
	private final static int TAG_DELETE = 1;
	
	private boolean mEditable = false;
	private OnModifySubjectListener mModifyListener;
	
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
	
	private final String[] mLabels;
	
	public AdapterSubject(Context context,OnModifySubjectListener l) {
		super(context);
		mModifyListener = l;
		mLabels = context.getResources()
				.getStringArray(R.array.subject_label);
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_COUNT;
	}
	
	public void setEditable(boolean editable){
		mEditable = editable;
	}
	
	public void update(long paperId){
		if(mEditable){
			List<Subject> list = Dao.getDaoSubject().getAll(paperId);
			updateDataSet(list);
		}
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
			
			switch (mItemType) {
			case ITEM_CHOICE_SINGLE:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_choice, null);
				RadioGroup group = (RadioGroup) convertView.findViewById(R.id.choice_sigle_body);
				holder.options = group;
				break;
				
			case ITEM_CHOICE_MULTIPLE:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_choice_multiple, null);
				holder.options = convertView.findViewById(R.id.choice_body);
				break;
				
			case ITEM_ANSWER:
				convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.adapter_item_view_answer_close, null);
				break;
				
			default:
				break;
			}
			View v = convertView.findViewById(R.id.choice_title);
			holder.title = (TextView) v.findViewById(R.id.choice_title_name);
			
			if(mEditable){
				v.findViewById(R.id.choice_edit_layout).setVisibility(View.VISIBLE);
				holder.edit = v.findViewById(R.id.choice_edit);
				holder.delete = v.findViewById(R.id.choice_delete);
				addCallBack(holder.edit,TAG_EDIT);
				addCallBack(holder.delete,TAG_DELETE);
			}else{
				v.findViewById(R.id.choice_edit_layout).setVisibility(View.GONE);
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
		if(mEditable){
			if(holder.delete != null){
				holder.delete.setTag(data);
				holder.delete.setTag(R.string.key_tag_index,position);
			}
			if(holder.edit != null){
				holder.edit.setTag(data);
				holder.edit.setTag(R.string.key_tag_index,position);
			}
		}
		if(data == null)
			return;
		
		int index = position+1;
		switch (mItemType) {
		case ITEM_CHOICE_SINGLE:
			if(data instanceof Subject){
				Subject q = (Subject) data;
				if(q != null){
					String string = String.format(
							getContext().getString(R.string.subject_label_name),
							index,
							q.getTopic());
					holder.title.setText(string);
					
					String[] options = q.getOptions();
					RadioButton radio = null;
					//mLabels 
					for(int i=0; i<mOpt.length; i++){
						if(options[i] == null)
							continue;
						string = String.format(
								getContext().getString(R.string.subject_value_fromat),
								mLabels[i],
								options[i]);
						radio = (RadioButton) holder.options.findViewById(mOpt[i]);
						radio.setText(string);
					}
				}
			}
			break;
			
		case ITEM_CHOICE_MULTIPLE:
			if(data instanceof Subject){
				Subject q = (Subject) data;
				CheckBox chbx = null;
				if(q != null){
					String string = String.format(
							getContext().getString(R.string.subject_label_name),
							index,
							q.getTopic());
					holder.title.setText(string);
					String[] options = q.getOptions();
					for(int i=0; i<mOptions.length; i++){
						chbx = (CheckBox) holder.options.findViewById(mOptions[i]);
						if(options[i] == null)
							continue;
						string = String.format(
								getContext().getString(R.string.subject_value_fromat),
								mLabels[i],
								options[i]);
						chbx.setText(string);
					}
				}
			}
			break;
		case ITEM_ANSWER:
			if(data instanceof Subject){
				Subject a = (Subject)data;
				holder.title.setText(String.format(
						getContext().getString(R.string.subject_label_name),
						index,
						a.getTopic()));
			}
			break;
		default:
			break;
		}
	}
	
	private void addCallBack(View v,int tag){
		if(v == null || !mEditable || mModifyListener == null)
			return;
		
		v.setTag(R.string.key_tag,tag);
		
		v.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(v.getTag(R.string.key_tag) == null)
					return;
				try {
					int iTag = Integer.valueOf(
							v.getTag(R.string.key_tag).toString());
					Object obj = v.getTag();
					Subject data = null;
					if(obj instanceof Subject)
					  data = (Subject)obj;
					else 
						return;
					
					int extra = Integer.valueOf(
							v.getTag(R.string.key_tag_index).toString());
					switch (iTag) {
					case TAG_DELETE:
						mModifyListener.onDeleteSubject(data, extra);
						break;
					case TAG_EDIT:
						mModifyListener.onEditSubject(data, extra);
						break;
					default:
						break;
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	class ViewHolder{
		TextView title;
		View options;
		View edit;
		View delete;
	}
}
