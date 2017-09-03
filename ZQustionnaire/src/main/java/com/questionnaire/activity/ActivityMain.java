package com.questionnaire.activity;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.create.ActivityCreate;
import com.questionnaire.activity.create.ActivityHome;
import com.questionnaire.activity.done.ActivityDoing;
import com.questionnaire.activity.more.ActivityMore;
import com.questionnaire.activity.statistic.ActivityStatisticList;
import com.questionnaire.db.Paper;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.interfaces.IPaper;
import com.questionnaire.utils.ToastUtils;

import android.app.ActivityGroup;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ActivityMain extends ActivityGroup 
	implements OnClickListener, OnCheckedChangeListener {

	private final static String ACTIVITY_ID_PRE = "activity_id_";
	private final static int TAB_PAPER_CREATE = 0;
	private final static int TAB_PAPER_DONING = 1;
	private final static int TAB_PAPER_STATISTIC = 2;
	private final static int TAB_PAPER_ABOUT = 3;
	
	private LinearLayout mLayout;
	
	RadioGroup mGroupTab = null;
	
	private int[] mTabIndex = {
			TAB_PAPER_CREATE,
			TAB_PAPER_DONING,
			TAB_PAPER_STATISTIC,
			TAB_PAPER_ABOUT};

	private long exitTime = 0;//两次点击退出应用
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLayout  = (LinearLayout) findViewById(R.id.main_layout);
		
		initView();
	}
	
	void initView() {
		mGroupTab = (RadioGroup) findViewById(R.id.group_tab);
		mGroupTab.setOnCheckedChangeListener(this);
		RadioButton create = (RadioButton) mGroupTab.findViewById(R.id.bt_paper_create);
		create.setChecked(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitApp();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	//3秒内双击退出app
	public void exitApp() {
		if ((System.currentTimeMillis() - exitTime) > 3000) {
			ToastUtils.show(getApplicationContext(), R.string.exit_app_click_again);
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}
	
	public void onClick(View v) {
		startActivityById(v.getId());
	}
	
	protected void onTabClicked(int tab,Intent intent){
		if(tab != -1){
			LocalActivityManager am = getLocalActivityManager();
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			Window window = am.startActivity(ACTIVITY_ID_PRE + mTabIndex[tab], intent);
			if(window != null){
				boolean add = true;
				View root = window.getDecorView();
				for(int i=0;i<mLayout.getChildCount();i++){
					View child = mLayout.getChildAt(i);
					if(root.equals(child)){
						add = false;
					}else{
						child.setVisibility(View.GONE);
					}
				}
				if(add){
					mLayout.addView(root);
				}
				root.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onCheckedChanged(RadioGroup view, int checkedId) {
		startActivityById(checkedId);
	}

	void startActivityById(int checkedId) {
		Intent i = new Intent();
		switch (checkedId) {
		case R.id.bt_paper_create:
			i.setClass(this, ActivityHome.class);
			onTabClicked(TAB_PAPER_CREATE, i);
			break;
		case R.id.bt_paper_doing:
			i.setClass(this, ActivityDoing.class);
			onTabClicked(TAB_PAPER_DONING, i);
			break;
		case R.id.bt_paper_statistic:
			i.setClass(this, ActivityStatisticList.class);
			i.setAction(Conf.ACTION_STATISTIC);
			onTabClicked(TAB_PAPER_STATISTIC, i);
			break;
		case R.id.bt_paper_about:
			i.setClass(this, ActivityMore.class);
			onTabClicked(TAB_PAPER_ABOUT, i);
			break;
		default:
			break;
		}
	}
}
