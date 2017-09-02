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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by discover on 2017/7/24.
 */

public class CreateWordDocument implements ICreateDocument {

    @Override
    public boolean onCreateDocument(Context context,String dirPath,long id) {
        File temp = null;
        try {
            File dir = new File(dirPath);
            if(!dir.exists()) dir.mkdirs();

            createFileFromAssets(context,dirPath,"temp.doc","temp.doc");

            temp = new File(dir,"temp.doc");

            if(!temp.exists()) temp.createNewFile();

            IPaper<Paper> dao = Dao.getDaoPaper();
            List<Paper> papers;
            if(id<0){
                papers = dao.getAll();
            }else{
                papers = new ArrayList<Paper>(1);
                Paper p = dao.get(id);
                papers.add(p);
            }
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // clear temp doc document
            if(temp!=null && temp.exists()) temp.delete();
        }
        return true;
    }

    void writePager(Context context,Range r,Paper m) throws Exception{
        writeData(r, null,m.getName(),"\r\r"); // pager title
        writeData(r, context.getString(R.string.paper_author,""),m.getAuthor(),"\t\t\t"); // author name
        writeData(r, context.getString(R.string.paper_create_time,""),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(m.getDate())).toString(),"\r\r"); // create date
        writeData(r, context.getString(R.string.paper_description,""),m.getDescription(),"\r\r"); // paper descriptions
        writeData(r, context.getString(R.string.paper_marks,""),m.getMarkes(),"\r"); // markes

        List<Subject> subjects = Dao.getDaoSubject().getAll(m.getId());
        if(subjects==null && subjects.isEmpty()) return;
        int index = 1;
        for(Subject subject : subjects){
            if(subject==null) continue;
            writeSubject(index,subject,r);
            index ++;
        }
    }

    void writeSubject(int index,Subject m,Range r) throws Exception{

        writeData(r,"\r"+index+". ",m.getTopic(),"\r"); // write subject name

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
            writeData(r,String.format(label,OPTION_LABELS[index]),op,"\r");
            index ++;
        }
    }

    void writeData(Range r,String pref,String value,String end) throws Exception{
        if(!TextUtils.isEmpty(value)){
            if(!TextUtils.isEmpty(pref)) r.insertAfter(new String(pref.getBytes(),"utf-8"));
            r.insertAfter(new String(value.getBytes(),"utf-8"));
            if(!TextUtils.isEmpty(end)) r.insertAfter(new String(end.getBytes(),"utf-8"));
        }
    }

    void createFileFromAssets(Context context,String dir, String name,String assetsFileName){
        try{
            InputStream is = context.getAssets().open(assetsFileName);
            File f = new File(dir,name);
            OutputStream os = new FileOutputStream(f);
            byte[] buff = new byte[1024];
            int count;
            while ((count = is.read(buff)) > 0){
                os.write(buff,0,count);
            }
            is.close();
            os.flush();
            os.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
