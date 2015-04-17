package com.gdin.netcentermans.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.easyandroidanimations.library.ScaleInAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.Utils;
import com.gdin.netcentermans.utils.ViewHolder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends ActionBarActivity {
    private ListView lvNews;
    private List<AVObject> data;
    private MyAdapter adapter;

    private ViewFlipper viewFlipper;
    private LinearLayout llMain;
    private LinearLayout llContent;
    private TextView tvContent;
    private TextView tvContentTitle;
    private ImageView tvReturn;
    private ImageView imgLogin;

    private FloatingActionButton rightLowerButton;

    private PopupWindow popupWindow;
    private TextView tvLogout;
    private TextView tvmyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initMenu();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMenuButton();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initView() {
        popupWindow = new PopupWindow(this);
        LinearLayout llPopContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ll_popumenu, null);
        popupWindow.setContentView(llPopContent);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        viewFlipper = (ViewFlipper) findViewById(R.id.vf_news);
        llMain = (LinearLayout) findViewById(R.id.ll_news_main);
        llContent = (LinearLayout) findViewById(R.id.ll_news_content);
        tvContentTitle = (TextView) findViewById(R.id.tv_main_content_title);
        tvReturn = (ImageView) findViewById(R.id.tv_news_content_return);
        imgLogin = (ImageView) findViewById(R.id.img_main_login);
        imgLogin.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                } else {
                    //startActivity(new Intent(Main2Activity.this,UserInfoActivity.class));
                    popupWindow.showAsDropDown(imgLogin);
                }
            }
        });
        tvLogout = (TextView) llPopContent.findViewById(R.id.tv_logout);
        tvmyInfo = (TextView) llPopContent.findViewById(R.id.tv_my_info);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                AVUser.logOut();
                initMenuButton();
            }
        });
        tvmyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(Main2Activity.this, UserInfoActivity.class));
            }
        });
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(llContent).animate();
                new ScaleInAnimation(llMain).animate();
            }
        });
        tvContent = (TextView) findViewById(R.id.tv_news_content);
        lvNews = (ListView) findViewById(R.id.lv_news);
        data = new ArrayList<>();
        adapter = new MyAdapter(data);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AVObject obj = data.get(position);
                tvContent.setText(obj.getString("content"));
                tvContentTitle.setText(obj.getString("title"));
                new ScaleOutAnimation(llMain).animate();
                new ScaleInAnimation(llContent).animate();

            }
        });

        AVQuery.getQuery("News").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (final AVObject object : list) {
                        boolean isImgNews = object.getBoolean("isImgNews");
                        if (isImgNews) {
                            RelativeLayout rl = (RelativeLayout) LayoutInflater.from(Main2Activity.this).inflate(R.layout.rl_main_news_img_item,null);
                            ImageView img = (ImageView) rl.findViewById(R.id.img_news_img);
                            TextView tvTitle = (TextView) rl.findViewById(R.id.tv_news_title);
                            tvTitle.setText(object.getString("title"));
                            rl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tvContent.setText(object.getString("content"));
                                    tvContentTitle.setText(object.getString("title"));
                                    new ScaleOutAnimation(llMain).animate();
                                    new ScaleInAnimation(llContent).animate();

                                }
                            });
                            viewFlipper.addView(rl);
                            ImageView imageView = new ImageView(Main2Activity.this);
                            String url = object.getAVFile("image").getUrl();
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, img, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    ((ImageView) view).setImageBitmap(bitmap);
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        } else {
                            data.add(object);
                        }
                        adapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }


    private void initMenuButton() {
        if (AVUser.getCurrentUser() != null) {
            rightLowerButton.setVisibility(View.VISIBLE);
        } else {
            rightLowerButton.setVisibility(View.GONE);
        }
    }

    private class MyAdapter extends BaseAdapter {

        private List<AVObject> data;

        private MyAdapter(List<AVObject> data) {
            this.data = data;
        }

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
            if (convertView == null) {
                convertView = LayoutInflater.from(Main2Activity.this).inflate(R.layout.rl_main_news_item, null);
            }
            AVObject object = data.get(position);

            TextView tvTitle = ViewHolder.get(convertView, R.id.tv_news_item_title);
            TextView tvDate = ViewHolder.get(convertView, R.id.tv_news_item_date);
            tvTitle.setText(object.getString("title"));
            tvDate.setText(Utils.date2String("yyyy-MM-dd", object.getUpdatedAt()));
            return convertView;
        }
    }


    private void initMenu() {
        // Set up the white button on the lower right corner
        // more or less with default parameter
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);

        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);
        TextView tv3 = new TextView(this);
        TextView tv4 = new TextView(this);
        tv1.setText("报修");
        tv2.setText("记录");

/*        tv1.setBackgroundResource(R.drawable.button_action_touch);
        tv2.setBackgroundResource(R.drawable.button_action_touch);
        tv3.setBackgroundResource(R.drawable.button_action_touch);
        tv4.setBackgroundResource(R.drawable.button_action_touch);*/

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        SubActionButton bt1 = rLSubBuilder.setContentView(tv1).build();
        SubActionButton bt2 = rLSubBuilder.setContentView(tv2).build();
        SubActionButton bt3 = rLSubBuilder.setContentView(tv3).build();
        SubActionButton bt4 = rLSubBuilder.setContentView(tv4).build();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, RepairActivity.class));
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,MyRepairActivity.class));
            }
        });
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(bt1)
                .addSubActionView(bt2)
                .addSubActionView(bt3)
                .addSubActionView(bt4)
                .attachTo(rightLowerButton)
                .build();


        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });
    }

}
