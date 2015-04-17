package com.gdin.netcentermans.fragment;

import java.util.List;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.Utils;
import com.gdin.netcentermans.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 信息
 * @version netCenterManS	
 * @author DZC
 * @Time  2015-3-1 上午11:41:59
 *
 *
 */
public class MyRepairFragment extends Fragment{

	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private List<AVObject> data;
	
	private static MyRepairFragment fragment;
	public static MyRepairFragment getInstance(){
		if(fragment==null){
			fragment = new MyRepairFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view  = inflater.inflate(R.layout.my_repair, null);
		initView();
		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private void initView(){
		TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
		title.setText("我的报修记录");
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_my_repair);
		
		if(AVUser.getCurrentUser()==null){
			Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
			return;
		}
		AVQuery.getQuery("repair").whereEqualTo("user", AVUser.getCurrentUser()).addDescendingOrder("createdAt").findInBackground(new FindCallback<AVObject>() {
			
			@Override
			public void done(List<AVObject> avs, AVException e) {
				// TODO Auto-generated method stub
				if(e==null&&avs.size()>0){
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
							if(convertView==null){
								convertView = LayoutInflater.from(getActivity()).inflate(R.layout.my_repair_item, null);
							}
							TextView num = ViewHolder.get(convertView, R.id.tv_my_repair_num);
							TextView title = ViewHolder.get(convertView, R.id.tv_my_repair_title);
							TextView date = ViewHolder.get(convertView, R.id.tv_my_repair_date);
							AVObject repair = data.get(position);
							num.setText((position+1)+"");
							title.setText(repair.getString("title"));
							date.setText(Utils.date2String(repair.getCreatedAt()));
							convertView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									AVObject repair = data.get(position);
									Bundle bundle = new Bundle();
									bundle.putString("id", repair.getObjectId());
									MyRepairInfoFragment fragment = new MyRepairInfoFragment();
									fragment.setArguments(bundle);
									getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("myrepair").replace(R.id.frgment_main_content, fragment).commit();
								
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
							if(data==null){
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
				}else{
					Log.d("",e.toString());
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
