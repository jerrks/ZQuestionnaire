package com.questionnaire.doc;

import android.content.Context;

/**
 * Created by discover on 2017/7/24.
 */

public interface ICreateDocument {
    String[] OPTION_LABELS = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    boolean onCreateDocument(Context context,String docPath);
}
