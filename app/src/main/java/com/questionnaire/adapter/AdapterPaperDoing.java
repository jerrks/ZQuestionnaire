package com.questionnaire.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Answer;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;

public class AdapterPaperDoing extends AbsListAdapter<Subject>
        implements View.OnClickListener {

    private final int ITEM_COUNT = 2;
    private final int ITEM_CHOICES = 0;
    private final int ITEM_ANSWER = 1;
    private int mItemType = ITEM_CHOICES;

    private final String[] mLabels, mAnswer;

    private List<Answer> mAnswerList;
    private long mPaperId, mTesterId;
    ListView mListView;

    Drawable mChoiceDrawable,mChoiceSelectedDrawable,mMutilChoiceDrawable,mMutilChoiceSelectedDrawable;

    public AdapterPaperDoing(ListView listView, long paperId) {
        super(listView.getContext());
        mListView = listView;
        Context context = listView.getContext();
        mLabels = context.getResources()
                .getStringArray(R.array.subject_label);
        mAnswer = context.getResources()
                .getStringArray(R.array.answers);
        mAnswerList = new ArrayList<Answer>();
        mPaperId = paperId;
        setDataSet(Dao.getDaoSubject().getAll(mPaperId));

        mChoiceDrawable = ContextCompat.getDrawable(context,R.drawable.ic_choice_unselect);
        mChoiceSelectedDrawable = ContextCompat.getDrawable(context,R.drawable.ic_choice_selected);
        mMutilChoiceDrawable = ContextCompat.getDrawable(context,R.drawable.ic_mutil_choice_normal);
        mMutilChoiceSelectedDrawable = ContextCompat.getDrawable(context,R.drawable.ic_mutil_choice_checked);
    }

    public void setTesterId(long testerId) {
        mTesterId = testerId;
    }

    public boolean onTestComplete() {
        for (Answer a : mAnswerList) {
            if (a == null)
                continue;
            if (!a.isDone()){
                mListView.smoothScrollToPosition(mAnswerList.indexOf(a));
                Conf.displayToast(getContext(), "问卷调查还未完成，请继续完成问卷！");
                return false;
            }
        }
        boolean ready = Dao.getDaoAnswer().insertAll(mAnswerList);
        if (ready) {
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
        if (data != null) {
            switch (data.getType()) {
                case Subject.TYPE_CHOICE_MUTILPE:
                case Subject.TYPE_SORT:
                case Subject.TYPE_CHOICE_SINGLE:
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (mItemType) {
                case ITEM_CHOICES:
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.adapter_subject_item_choice_view, null);
                    convertView.findViewById(R.id.choice_edit_layout).setVisibility(View.GONE);
                    View v = convertView.findViewById(R.id.choice_title);
                    holder.title = (TextView) v.findViewById(R.id.choice_title_name);
                    holder.options = convertView.findViewById(R.id.choice_options_container);
                    holder.value = (TextView) convertView.findViewById(R.id.subject_option_tips);
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        fillItemViewData(position, holder, convertView);

        return convertView;
    }

    private void fillItemViewData(int position, ViewHolder holder, View view) {

        Subject data = getItem(position);
        if (data == null)return;

        Answer answer;
        if (mAnswerList.size() <= position) {
            answer = new Answer();
            mAnswerList.add(answer);
        } else {
            answer = mAnswerList.get(position);
        }

        int index = position + 1;
        answer.setSubjectId(data.getId());
        answer.setPaperId(mPaperId);
        answer.setTesterId(mTesterId);

        holder.title.setText(String.format(
                getContext().getString(R.string.subject_label_name),
                index,
                data.getTopic()));

        switch (mItemType) {
            case ITEM_CHOICES:
                String[] options = data.getOptions();

                ViewGroup container = (ViewGroup) holder.options;
                View child;
                int c;

                int type = data.getType();
                TextView tv;
                ImageView iv;
                String value;
                String key = answer.getAnswer();

                for (int i = 0; i < options.length; i++) {
                    c = container.getChildCount();
                    if(c>i){
                        child = container.getChildAt(i);
                    }else{
                        child = LayoutInflater.from(container.getContext()).inflate(R.layout.subject_option_item_view,null,false);
                        child.setTag(R.string.key_tag,holder.value);
                        child.setOnClickListener(this);
                        container.addView(child);
                    }
                    if (TextUtils.isEmpty(options[i])){
                        child.setVisibility(View.GONE);
                        continue;
                    }

                    iv = (ImageView) child.findViewById(R.id.subject_option_icon);

                    boolean checked = TextUtils.isEmpty(key) ? false : key.contains(mLabels[i]);
                    Drawable drawable = checked ? (type==Subject.TYPE_CHOICE_SINGLE ? mChoiceSelectedDrawable : mMutilChoiceSelectedDrawable)
                            : (type==Subject.TYPE_CHOICE_SINGLE ? mChoiceDrawable : mMutilChoiceDrawable);
                    child.setSelected(checked);
                    iv.setImageDrawable(drawable);

                    tv = (TextView) child.findViewById(R.id.subject_option_value);
                    child.setTag(position);
                    child.setTag(R.string.key_tag_index, i);
                    value = String.format(
                            getContext().getString(R.string.subject_value_fromat),
                            mLabels[i],
                            options[i]);
                    tv.setText(value);
                    child.setVisibility(View.VISIBLE);

                }

                break;

            case ITEM_ANSWER:
                holder.answer.setTag(R.string.key_tag_index, position);
                String str = answer.getAnswer();
                if (!TextUtils.isEmpty(str)) holder.answer.setText(str);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        boolean isChecked = !v.isSelected();
        v.setSelected(isChecked);

        int id = Integer.valueOf(v.getTag().toString());
        int index = Integer.valueOf(v.getTag(R.string.key_tag_index).toString());
        String str = mAnswer[index];
        Answer a = mAnswerList.get(id);
        Subject subj = getItem(id);
        if(subj.getType()==Subject.TYPE_CHOICE_SINGLE){
            a.setAnswer(null);
        }
        String string = a.getAnswer();
        if (isChecked) { // checked and not contains then add it
            if (TextUtils.isEmpty(string)) {
                a.setAnswer(str);
            } else if (!string.contains(str)) {
                a.setAnswer(string + str);
            }
        } else {
            if (string != null && string.contains(str)) { //not check contains then remove it
                str = string.replace(str, "");
                a.setAnswer(str);
            }
        }
        a.setDone(!TextUtils.isEmpty(a.getAnswer()));
        if (subj.getType() == Subject.TYPE_SORT) {
            Object obj = v.getTag(R.string.key_tag);
            if (obj != null && obj instanceof TextView) {
                TextView tv = (TextView) obj;
                tv.setText(a.getAnswer());
            }
        }
        notifyDataSetChanged();
    }

    private void addEditCallback(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                Object obj = edit.getTag(R.string.key_tag_index);
                if (obj != null) {
                    int id = Integer.valueOf(obj.toString());
                    Answer a = mAnswerList.get(id);
                    String value = s.toString();
                    a.setAnswer(value);
                    a.setDone(!TextUtils.isEmpty(value));
                }
            }
        });
    }

    class ViewHolder {
        TextView title;
        View options;
        EditText answer;
        TextView value;
    }
}
