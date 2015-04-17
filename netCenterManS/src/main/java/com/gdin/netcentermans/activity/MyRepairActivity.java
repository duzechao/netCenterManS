package com.gdin.netcentermans.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.fragment.MyRepairInfoFragment;
import com.gdin.netcentermans.utils.Utils;
import com.gdin.netcentermans.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

public class MyRepairActivity extends ActionBarActivity {

    private PullToRefreshListView pullToRefreshListView;
    private List<AVObject> data;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_repair);
        initView();
    }

    private void initView(){
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_my_repair);
        imgReturn = (ImageView) findViewById(R.id.img_myrepair_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(AVUser.getCurrentUser()==null){
            Toast.makeText(MyRepairActivity.this, "请先登录", Toast.LENGTH_LONG).show();
            return;
        }
        AVQuery.getQuery("repair").whereEqualTo("user", AVUser.getCurrentUser()).addDescendingOrder("createdAt").findInBackground(new FindCallback<AVObject>() {

            @Override
            public void done(List<AVObject> avs, AVException e) {
                // TODO Auto-generated method stub
                if (e == null && avs.size() > 0) {
                    data = avs;
                    BaseAdapter adapter = new BaseAdapter() {

                        @Override
                        public void unregisterDataSetObserver(DataSetObserver observer) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void registerDataSetObserver(DataSetObserver observer) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public boolean isEmpty() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean hasStableIds() {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public int getViewTypeCount() {
                            // TODO Auto-generated method stub
                            return 1;
                        }

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            // TODO Auto-generated method stub
                            if (convertView == null) {
                                convertView = LayoutInflater.from(MyRepairActivity.this).inflate(R.layout.my_repair_item, null);
                            }
                            TextView num = ViewHolder.get(convertView, R.id.tv_my_repair_num);
                            TextView title = ViewHolder.get(convertView, R.id.tv_my_repair_title);
                            TextView date = ViewHolder.get(convertView, R.id.tv_my_repair_date);
                            AVObject repair = data.get(position);
                            num.setText((position + 1) + "");
                            title.setText(repair.getString("title"));
                            date.setText(Utils.date2String(repair.getCreatedAt()));
                            convertView.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    AVObject repair = data.get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", repair.getObjectId());
                                    Intent intent = new Intent(MyRepairActivity.this,MyRepairInfoActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            return convertView;
                        }

                        @Override
                        public int getItemViewType(int position) {
                            // TODO Auto-generated method stub
                            return 1;
                        }

                        @Override
                        public long getItemId(int position) {
                            // TODO Auto-generated method stub
                            return 0;
                        }

                        @Override
                        public Object getItem(int position) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public int getCount() {
                            // TODO Auto-generated method stub
                            if (data == null) {
                                return 0;
                            }
                            return data.size();
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            // TODO Auto-generated method stub
                            return false;
                        }

                        @Override
                        public boolean areAllItemsEnabled() {
                            // TODO Auto-generated method stub
                            return false;
                        }
                    };
                    pullToRefreshListView.setAdapter(adapter);
                } else {
                    Log.d("", e.toString());
                    Toast.makeText(MyRepairActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
