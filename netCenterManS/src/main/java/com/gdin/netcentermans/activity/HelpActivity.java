package com.gdin.netcentermans.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RefreshCallback;
import com.dd.CircularProgressButton;
import com.easyandroidanimations.library.ScaleInAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.ViewHolder;

import java.util.List;

public class HelpActivity extends ActionBarActivity {

    private ListView lv;
    private List<AVObject> data;
    private TextView tvContent;
    private ImageView imgReturn;
    private boolean isShowContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
    }

    private void initView(){
        lv = (ListView) findViewById(R.id.lv_help);
        tvContent = (TextView) findViewById(R.id.tv_help_content);
        imgReturn = (ImageView) findViewById(R.id.img_help_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowContent){
                    new ScaleOutAnimation(tvContent).animate();
                    new ScaleInAnimation(lv).animate();
                    isShowContent = false;
                }else{
                    finish();
                }
            }
        });
        AVQuery.getQuery("help").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null){
                    data = avObjects;
                    lv.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return data.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            if(convertView==null){
                                convertView = LayoutInflater.from(HelpActivity.this).inflate(R.layout.ll_help_list_item,null);
                            }
                            TextView tvPosition = ViewHolder.get(convertView,R.id.tv_help_list_item_position);
                            TextView tvTitle = ViewHolder.get(convertView,R.id.tv_help_list_item_title);
                            tvPosition.setText((position+1)+"");
                            tvTitle.setText(data.get(position).getString("title"));
                            return convertView;
                        }
                    });
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AVObject object = data.get(position);
                String content = object.getString("content");
                tvContent.setText(content);
                isShowContent = true;
                new ScaleOutAnimation(lv).animate();
                new ScaleInAnimation(tvContent).animate();

            }
        });
    }
}
