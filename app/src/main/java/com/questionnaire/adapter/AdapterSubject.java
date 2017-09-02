package com.questionnaire.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.listener.OnModifySubjectListener;

public class AdapterSubject extends AbsListAdapter<Subject> {

    private final int ITEM_COUNT = 2;
    private final int ITEM_CHOICES = 0;
    private final int ITEM_ANSWER = 1;
    private int mItemType = ITEM_CHOICES;

    private final static int TAG_EDIT = 0;
    private final static int TAG_DELETE = 1;

    private boolean mEditable = false;
    private OnModifySubjectListener mModifyListener;

    Drawable mChoiceDrawable,mMutilChoiceDrawable;
    private final String[] mLabels;

    public AdapterSubject(Context context, OnModifySubjectListener l) {
        super(context);
        mModifyListener = l;
        mLabels = context.getResources()
                .getStringArray(R.array.subject_label);
        mChoiceDrawable = ContextCompat.getDrawable(context,R.drawable.ic_choice_unselect);
        mMutilChoiceDrawable = ContextCompat.getDrawable(context,R.drawable.ic_mutil_choice_normal);
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_COUNT;
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
    }

    public void update(long paperId) {
        if (mEditable) {
            List<Subject> list = Dao.getDaoSubject().getAll(paperId);
            updateDataSet(list);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Subject data = getItem(position);
        if (data != null) {
            switch (data.getType()) {
                case Subject.TYPE_CHOICE_SINGLE:
                case Subject.TYPE_CHOICE_MUTILPE:
                case Subject.TYPE_SORT:
                    mItemType = ITEM_CHOICES;
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
        convertView = createConvertView(convertView,parent);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        fillItemViewData(position, holder, convertView);
        return convertView;
    }

    View createConvertView(View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            switch (mItemType) {
                case ITEM_CHOICES:
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.adapter_subject_item_choice_view, null);
                    holder.options = convertView.findViewById(R.id.choice_options_container);
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

            View editLayout = v.findViewById(R.id.choice_edit_layout);
            if (mEditable) {
                editLayout.setVisibility(View.VISIBLE);
                holder.edit = v.findViewById(R.id.choice_edit);
                holder.delete = v.findViewById(R.id.choice_delete);
                addCallBack(holder.edit, TAG_EDIT);
                addCallBack(holder.delete, TAG_DELETE);
            } else {
                editLayout.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        }

        return convertView;
    }

    private void fillItemViewData(int position, ViewHolder holder, View view) {

        Subject data = getItem(position);
        if (mEditable) {
            if (holder.delete != null) {
                holder.delete.setTag(data);
                holder.delete.setTag(R.string.key_tag_index, position);
            }
            if (holder.edit != null) {
                holder.edit.setTag(data);
                holder.edit.setTag(R.string.key_tag_index, position);
            }
        }
        if (data == null) return;

        int index = position + 1;
        String value = String.format(
                getContext().getString(R.string.subject_label_name),
                index,
                data.getTopic());
        holder.title.setText(value);

        if(mItemType==ITEM_CHOICES){
            String[] options = data.getOptions();
            ViewGroup container = (ViewGroup) holder.options;
            View child;
            int c;

            TextView tv;
            ImageView iv;

            for (int i = 0; i < options.length; i++) {
                c = container.getChildCount();
                if(c>i){
                    child = container.getChildAt(i);
                }else{
                    child = LayoutInflater.from(container.getContext()).inflate(R.layout.subject_option_item_view,null,false);
                    container.addView(child);
                }
                if (TextUtils.isEmpty(options[i])){
                    child.setVisibility(View.GONE);
                    continue;
                }

                iv = (ImageView) child.findViewById(R.id.subject_option_icon);
                iv.setImageDrawable(data.getType()==Subject.TYPE_CHOICE_SINGLE ? mChoiceDrawable : mMutilChoiceDrawable);

                tv = (TextView) child.findViewById(R.id.subject_option_value);
                value = String.format(
                        getContext().getString(R.string.subject_value_fromat),
                        mLabels[i],
                        options[i]);
                tv.setText(value);
                child.setVisibility(View.VISIBLE);

            }
        }
    }

    private void addCallBack(View v, int tag) {
        if (v == null || !mEditable || mModifyListener == null)
            return;

        v.setTag(R.string.key_tag, tag);

        v.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (v.getTag(R.string.key_tag) == null)
                    return;
                try {
                    int iTag = Integer.valueOf(
                            v.getTag(R.string.key_tag).toString());
                    Object obj = v.getTag();
                    Subject data = null;
                    if (obj instanceof Subject)
                        data = (Subject) obj;
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

    class ViewHolder {
        TextView title;
        View options;
        View edit;
        View delete;
    }
}
