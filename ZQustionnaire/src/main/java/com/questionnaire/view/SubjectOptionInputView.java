package com.questionnaire.view;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.R;

/**
 * Created by discover on 2017/9/2.
 */

public class SubjectOptionInputView extends FrameLayout {
    static String[] mSubjectDefOptions;

    TextView mOptionLabelView;
    EditText mOptionInputView;
    Dialog mDialog;

    public SubjectOptionInputView(Context context) {
        super(context);
        initView();
    }

    public SubjectOptionInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SubjectOptionInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.subject_options_input_view,this,true);
        mOptionLabelView = (TextView) findViewById(R.id.option_input_label);
        mOptionInputView = (EditText) findViewById(R.id.option_input_value);
        findViewById(R.id.option_input_choose).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showChooseDefaultOptionsForSubject();
            }
        });

        if(mSubjectDefOptions==null) {
            mSubjectDefOptions = getResources().getStringArray(R.array.subject_default_options);
        }
    }

    public TextView getOptionLabelView(){
        return mOptionLabelView;
    }

    public EditText getOptionInputView(){
        return mOptionInputView;
    }

    void showChooseDefaultOptionsForSubject(){
        if(mDialog==null){
            mDialog = new Dialog(getContext(),R.style.dialog);
            View root = LayoutInflater.from(getContext())
                    .inflate(R.layout.paper_create_subject_choice_typt_dilog, null);
            TextView tv = (TextView) root.findViewById(R.id.dilog_subject_title);
            tv.setText("选择合适的答案");
            ListView list = (ListView) root.findViewById(R.id.subject_list_view);
            list.setAdapter(new ArrayAdapter<String>(getContext(),
                    R.layout.adapter_subject_choice_item_view,
                    R.id.adapter_subject,mSubjectDefOptions));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View v,
                                        int index, long id) {
                    mOptionInputView.setText(mSubjectDefOptions[index]);
                    mDialog.dismiss();
                }
            });
            mDialog.setContentView(root);
        }
        if(!mDialog.isShowing())mDialog.show();
    }
}
