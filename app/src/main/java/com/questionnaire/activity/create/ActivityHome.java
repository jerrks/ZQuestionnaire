package com.questionnaire.activity.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.activity.media.ActivityAudioList;
import com.questionnaire.activity.media.ActivityImageList;
import com.questionnaire.activity.media.ActivityVideoList;

/**
 * Created by discover on 2017/7/27.
 */

public class ActivityHome extends ActivityBase implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View v = findViewById(R.id.title);
        TextView tv = (TextView) v.findViewById(R.id.title_center);
        tv.setText(R.string.title_home);
        v.findViewById(R.id.title_right).setVisibility(View.GONE);
        v.findViewById(R.id.title_left).setVisibility(View.GONE);

        findViewById(R.id.home_create_paper_for_doc).setOnClickListener(this);
        findViewById(R.id.home_create_paper_for_txt).setOnClickListener(this);

        findViewById(R.id.home_create_paper_page).setOnClickListener(this);
        findViewById(R.id.home_record_for_video).setOnClickListener(this);
        findViewById(R.id.home_record_for_voice).setOnClickListener(this);
        findViewById(R.id.home_record_for_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_create_paper_for_doc:
                createAndLoadPagersForDocument(true);
                break;
            case R.id.home_create_paper_for_txt:
                createAndLoadPagersForDocument(false);
                break;

            case R.id.home_create_paper_page:
                startActivity(new Intent(this,ActivityCreate.class));
                break;
            case R.id.home_record_for_video:
                startActivity(new Intent(this,ActivityVideoList.class));
                break;
            case R.id.home_record_for_voice:
                startActivity(new Intent(this,ActivityAudioList.class));
                break;
            case R.id.home_record_for_photo:
                startActivity(new Intent(this,ActivityImageList.class));
                break;
        }
    }

    void createAndLoadPagersForDocument(boolean isWordFormat){

    }
}
