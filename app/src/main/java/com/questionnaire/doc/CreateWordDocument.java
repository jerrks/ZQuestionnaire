package com.questionnaire.doc;

import android.content.Context;
import android.text.TextUtils;

import com.questionnaire.R;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.interfaces.IPaper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by discover on 2017/7/24.
 */

public class CreateWordDocument implements ICreateDocument {

    @Override
    public boolean onCreateDocument(Context context,String docRootPath) {
        File temp = null;
        try {
            File dir = new File(docRootPath);
            if(!dir.exists()) dir.mkdirs();
            temp = new File(dir,"temp.doc");

            if(!temp.exists()) temp.createNewFile();

            IPaper<Paper> dao = Dao.getDaoPaper();
            List<Paper> papers = dao.getAll();
            if(papers==null || papers.isEmpty()) return false;

            for(Paper page : papers){
                if(page==null) continue;
                String name = page.getName();

                InputStream is = new FileInputStream(temp);
                HWPFDocument doc = new HWPFDocument(is);
                Range range = doc.getRange();

                writePager(context,range,page);

                OutputStream os = new FileOutputStream(new File(dir,name + ".doc"));
                doc.write(os); //把doc输出到输出流中

                is.close();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            // clear temp doc document
            if(temp!=null && temp.exists()) temp.delete();
        }
        return true;
    }

    void writePager(Context context,Range r,Paper m){
        writeData(r, null,m.getName(),"\n\n"); // pager title
        writeData(r, context.getString(R.string.paper_author,""),m.getAuthor(),"\t\t\t"); // author name
        writeData(r, context.getString(R.string.paper_create_time,""),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(m.getDate())).toString(),"\n\n"); // create date
        writeData(r, context.getString(R.string.paper_description,""),m.getDescription(),"\n\n"); // paper descriptions
        writeData(r, context.getString(R.string.paper_marks,""),m.getMarkes(),"\n"); // markes

        List<Subject> subjects = Dao.getDaoSubject().getAll(m.getId());
        if(subjects==null && subjects.isEmpty()) return;
        int index = 1;
        for(Subject subject : subjects){
            if(subject==null) continue;
            writeSubject(index,subject,r);
            index ++;
        }
    }

    void writeSubject(int index,Subject m,Range r){

        writeData(r,"\n"+index+". ",m.getTopic(),"\n"); // write subject name

        String[] options = m.getOptions();      // write subject options
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
            writeData(r,String.format(label,OPTION_LABELS[index]),op,"\n");
            index ++;
        }
    }

    void writeData(Range r,String pref,String value,String end){
        if(!TextUtils.isEmpty(value)){
            if(!TextUtils.isEmpty(pref)) r.insertAfter(pref);
            r.insertAfter(value);
            if(!TextUtils.isEmpty(end)) r.insertAfter(end);
        }
    }
}
