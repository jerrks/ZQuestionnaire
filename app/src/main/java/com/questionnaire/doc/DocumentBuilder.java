package com.questionnaire.doc;

import android.content.Context;

/**
 * Created by discover on 2017/7/24.
 */

public class DocumentBuilder {
    ICreateDocument documentCreater;

    public DocumentBuilder setCreater(ICreateDocument creater){
        documentCreater = creater;
        return this;
    }

    public boolean create(Context context,String docRootDir){
        return documentCreater.onCreateDocument(context,docRootDir);
    }

    public static ICreateDocument createDocDocumentCreater(){
        return new CreateWordDocument();
    }

    public static ICreateDocument createTextDocumentCreater(){
        return new CreateTextDocument();
    }
}
