package com.gdin.netcentermans.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.ImageLoader;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.easyandroidanimations.library.PuffInAnimation;
import com.easyandroidanimations.library.PuffOutAnimation;
import com.easyandroidanimations.library.ScaleInAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.gdin.netcentermans.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻通知
 * @version netCenterManS	
 * @author DZC
 * @Time  2015-3-1 上午11:41:59
 *
 *
 */
public class NewsFragment extends Fragment{

	private View view;
    private ListView lvNews;
    private List<AVObject> data;
    private MyAdapter adapter;

	private ViewFlipper viewFlipper;
    private LinearLayout llMain;
    private LinearLayout llContent;
    private TextView tvContent;
    private TextView tvReturn;

	private static NewsFragment fragment;

    public static NewsFragment getInstance(){
		if(fragment==null){
			fragment = new NewsFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_news,null);
		initView();
		return view;
	}

	private void initView(){
		viewFlipper = (ViewFlipper) view.findViewById(R.id.vf_news);
        llMain = (LinearLayout) view.findViewById(R.id.ll_news_main);
        llContent = (LinearLayout) view.findViewById(R.id.ll_news_content);
        //tvReturn = (TextView) view.findViewById(R.id.tv_news_content_return);
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(llContent).animate();
                new ScaleInAnimation(llMain).animate();
            }
        });
        tvContent = (TextView) view.findViewById(R.id.tv_news_content);
        lvNews = (ListView) view.findViewById(R.id.lv_news);
        data = new ArrayList<>();
        adapter = new MyAdapter(data);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvContent.setText(data.get(position).getString("content"));
                new ScaleOutAnimation(llMain).animate();
                new ScaleInAnimation(llContent).animate();

            }
        });

		AVQuery.getQuery("News").findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(e==null){
					for(AVObject object:list){
                        boolean isImgNews = object.getBoolean("isImgNews");
                        if(isImgNews){
                            ImageView imageView = new ImageView(getActivity());
                            String url = object.getAVFile("image").getUrl();
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    ((ImageView)view).setImageBitmap(bitmap);
                                    viewFlipper.addView(view);
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }else{
                            data.add(object);
                        }
                        adapter.notifyDataSetChanged();


					}
				}
			}
		});
	}

    private class MyAdapter extends BaseAdapter{

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
            TextView tv = new TextView(getActivity());
            tv.setText(data.get(position).getString("title"));
            return tv;
        }
    }
}
