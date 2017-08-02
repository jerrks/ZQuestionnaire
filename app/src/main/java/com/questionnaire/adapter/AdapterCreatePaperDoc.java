package com.questionnaire.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.Paper;

public class AdapterCreatePaperDoc extends AbsListAdapter<Paper> {

    private static final String TAG = "ADAPTER";

    public AdapterCreatePaperDoc(Context context) {
        super(context);
        List<Paper> list = Dao.getDaoPaper().getAll();
        setDataSet(list);
    }

    public List<Paper> getSelectedItems(){
        List<Paper> list = null;
        if(isEmpty()) return list;
        list = new ArrayList<Paper>(10);
        for(Paper p : getDataSet()){
            if(p!=null && p.isSelected()) list.add(p);
        }
        return list;
    }

    public View getView(int position, View view, ViewGroup parent) {
        HolderItem holder = null;
        if(view ==  null){
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_create_paper_doc_view, null);
            holder = new HolderItem();
            holder.icon = view.findViewById(R.id.right_icon);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.author = (TextView) view.findViewById(R.id.author);
            holder.description = (TextView) view.findViewById(R.id.descriprion);
            view.setTag(holder);
        } else {
            holder = (HolderItem) view.getTag();
        }

        Paper paper = getItem(position);
        if(Conf.DEBUG) Log.d(TAG, "paper=" + paper);
        if(paper != null){
            holder.icon.setSelected(paper.isSelected());
            holder.name.setText(paper.getName());
            holder.date.setText(formateString(R.string.paper_author,
                    paper.getAuthor()));
            holder.author.setText(formateString(R.string.paper_create_time,
                    Conf.formatTime(paper.getDate())));
            holder.description.setText(
                    formateString(R.string.paper_description,
                            paper.getDescription()));
        }
        return view;
    }

    private String formateString(int resId, Object arg){
        return String.format(getContext().getString(resId), arg);

    }

    class HolderItem{
        TextView name, author, date, description;
        View icon;
    }

}
