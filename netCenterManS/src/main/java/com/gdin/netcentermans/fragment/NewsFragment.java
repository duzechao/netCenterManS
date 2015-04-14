package com.gdin.netcentermans.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.ImageLoader;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermans.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
	private ViewFlipper viewFlipper;

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
		AVQuery.getQuery("News").findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(e==null){
					for(AVObject object:list){
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
					}
				}
			}
		});
	}
}
