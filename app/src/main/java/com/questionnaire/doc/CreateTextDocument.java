package com.questionnaire.doc;

import android.content.Context;
import android.text.TextUtils;

import com.questionnaire.R;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.interfaces.IPaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by discover on 2017/7/24.
 */

public class CreateTextDocument implements ICreateDocument {

    @Override
    public boolean onCreateDocument(Context context,String docRootPath) {
        try {
            File dir = new File(docRootPath);
            if(!dir.exists()) dir.mkdirs();

            OutputStream os;
            IPaper<Paper> dao = Dao.getDaoPaper();
            List<Paper> papers = dao.getAll();
            if(papers==null || papers.isEmpty()) return false;

            for(Paper page : papers){
                if(page==null) continue;
                os = new FileOutputStream(new File(dir,page.getName() + ".txt"));
                writePaper(context,page,os);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    void writePaper(Context context,Paper m,OutputStream os) throws Exception{
        writeByte(os, null,m.getName(),"\n\n"); // pager title
        writeByte(os, context.getString(R.string.paper_author,""),m.getAuthor(),"\t\t\t"); // author name
        writeByte(os, context.getString(R.string.paper_create_time,""),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(m.getDate())).toString(),"\n\n"); // create date
        writeByte(os, context.getString(R.string.paper_description,""),m.getDescription(),"\n\n"); // paper descriptions
        writeByte(os, context.getString(R.string.paper_marks,""),m.getMarkes(),"\n\n"); // markes

        List<Subject> subjects = Dao.getDaoSubject().getAll(m.getId());
        if(subjects==null && subjects.isEmpty()) return;
        int index = 1;
        for(Subject subject : subjects){
            if(subject==null) continue;
            writeSubject(index,subject,os);
            index ++;
        }
    }

    void writeSubject(int index,Subject m,OutputStream os) throws Exception{

        writeByte(os,"\n"+index+". ",m.getTopic(),"\n"); // write subject name

        String[] options = m.getOptions();  // write subject options
        if(options==null || options.length<1) return;

        index = 0;
        String label;
        switch (m.getType()){
            case Subject.TYPE_CHOICE_SINGLE:
                label = "(%s).";
                break;
            case Subject.TYPE_CHOICE_MUTILPE:
                label = "[%s].";
                break;
            case Subject.TYPE_CLOSE:
            case Subject.TYPE_SORT:
            default:
                label = "%s.";
                break;
        }
        for(String op : options){
            writeByte(os,String.format(label,OPTION_LABELS[index]),op,"\n");
            index ++;
        }
    }

    void writeByte(OutputStream os,String pref,String value,String end) throws Exception{
        if(!TextUtils.isEmpty(value)){
            byte[] data;
            if(!TextUtils.isEmpty(pref)){
                data = pref.getBytes("utf-8");
                os.write(data,0,data.length);
            }
            data = value.getBytes("utf-8");
            os.write(data,0,data.length);
            if(!TextUtils.isEmpty(end)){
                data = end.getBytes("utf-8");
                os.write(data,0,data.length);
            }
        }
    }
}
