package com.questionnaire.activity.create;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterCreatePaperDoc;
import com.questionnaire.db.Paper;
import com.questionnaire.doc.DocumentBuilder;
import com.questionnaire.doc.ICreateDocument;

import java.util.List;

/**
 * Created by discover on 2017/8/2.
 */

public class ActivityCreateDocument extends ActivityBase implements View.OnClickListener, AdapterView.OnItemClickListener {

    boolean isCreateWordDocument;
    AdapterCreatePaperDoc mAdapter;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreateWordDocument = getIntent().getBooleanExtra(Conf.INTENT_DATA,false);
        setContentView(R.layout.activity_create_document);


        View layout = findViewById(R.id.title);
        TextView tv = (TextView) layout.findViewById(R.id.title_center);
        tv.setText(R.string.title_create_doc);

        View v = layout.findViewById(R.id.title_left);
        v.setVisibility(View.VISIBLE);
        v.setOnClickListener(this);

        Button btn = (Button) layout.findViewById(R.id.title_right);
        btn.setVisibility(View.VISIBLE);
        btn.setText("导出");
        btn.setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.list_view);
        mAdapter = new AdapterCreatePaperDoc(this);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);

    }

    void startCreateDocument(List<Paper> list){
        if(progressBar!=null && progressBar.isShowing()) progressBar.dismiss();

        progressBar = new ProgressDialog(this);
        String key = isCreateWordDocument ? " word " : " txt ";
        String format = "正在导出%s文档,请耐心等待...";
        progressBar.setMessage(String.format(format,key));
        progressBar.setCancelable(false);
        progressBar.show();
        new Thread(new CrateDocumentThread(list,isCreateWordDocument)).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:
                startCreateDocument(mAdapter.getSelectedItems());
                break;
            case R.id.title_left:
            default:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
        Paper model = mAdapter.getItem(position);
        if(model!=null) model.setSelected(!model.isSelected());
        mAdapter.notifyDataSetChanged();
    }

    class CrateDocumentThread implements Runnable{
        List<Paper> list;
        ICreateDocument creator;
        String dir;

        CrateDocumentThread(List<Paper> list,boolean isWordDocument){
            this.list = list;
            creator = isWordDocument ? DocumentBuilder.createDocDocumentCreater() : DocumentBuilder.createTextDocumentCreater();
            String path = isWordDocument ? "doc" : "txt";
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                dir = Environment.getExternalStorageDirectory() + "/questionnaire/" + path;
            }else{
                dir = Environment.getDataDirectory() + "/questionnaire/" + path;
            }
        }

        @Override
        public void run() {
            try{
                if(list==null || list.isEmpty()) return;
                for(Paper p : list){
                    if(p==null) continue;
                    creator.onCreateDocument(getBaseContext(),dir,p.getId());
                    Thread.sleep(10);
                }
            }catch (Throwable e){
                e.printStackTrace();
            }finally {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressBar!=null && progressBar.isShowing()) progressBar.dismiss();
                        progressBar = null;
                        Toast.makeText(getBaseContext(),"文档已导出并保存到"+dir,Toast.LENGTH_LONG).show();
                        list.clear();
                        list = null;
                        creator = null;
                    }
                });
            }
        }
    }
}
