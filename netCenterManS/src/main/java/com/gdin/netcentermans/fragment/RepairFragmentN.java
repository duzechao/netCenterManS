package com.gdin.netcentermans.fragment;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.MStringRequest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 报修
 * 
 * @version netCenterManS
 * @author DZC
 * @Time 2015-3-1 上午11:41:59
 * 
 * 
 */
public class RepairFragmentN extends Fragment {

	private static RepairFragmentN fragment;

	public static RepairFragmentN getInstance() {
		if (fragment == null) {
			fragment = new RepairFragmentN();
		}
		return fragment;
	}

	private View view;

	// 登录
	private View loginView;
	private EditText etSNum;
	private EditText etPass;
	private TextView tvSubmit;

	
	//进入申请界面
	private View repairView;
	private EditText etReason;
	private EditText etTime;
	private EditText etReasonInfo;
	private TextView tvRepaiSummit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_repair_n, null);
		initView();		
		return view;
	}
	
	private void initView(){
		TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
		title.setText("报修");
		loginView = view.findViewById(R.id.ll_repair_login);
		etSNum = (EditText) view.findViewById(R.id.et_student_num);
		etPass = (EditText) view.findViewById(R.id.et_pass);
		tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
		
		repairView = view.findViewById(R.id.ll_repair_commit);
		etReason = (EditText) view.findViewById(R.id.et_repair_reason);
		etTime = (EditText) view.findViewById(R.id.et_repair_time);
		etReasonInfo = (EditText) view.findViewById(R.id.et_repair_reason_info);
		tvRepaiSummit = (TextView) view.findViewById(R.id.tv_repair_c_submit);
		
		
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sNum = etSNum.getText().toString();
				String pass = etPass.getText().toString();
				if(TextUtils.isEmpty(pass)||TextUtils.isEmpty(sNum)){
					Toast.makeText(getActivity(), "请填写相关信息", Toast.LENGTH_LONG).show();
					return ;
				}
				AVUser.logInInBackground(sNum, pass, new LogInCallback<AVUser>() {
					
					@Override
					public void done(AVUser arg0, AVException e) {
						// TODO Auto-generated method stub
						if(e==null){
							loginView.setVisibility(View.GONE);
							repairView.setVisibility(View.VISIBLE);
						}else{
							Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
		
		tvRepaiSummit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String reason = etReason.getText().toString();
				String time = etTime.getText().toString();
				String reasonInfo = etReasonInfo.getText().toString();
				if(TextUtils.isEmpty(reason)||TextUtils.isEmpty(time)||TextUtils.isEmpty(reasonInfo)){
					return ;
				}
				AVObject repair = new AVObject("repair");
				repair.put("title", reason);
				repair.put("appointTime", time);
				repair.put("reason", reasonInfo);
				repair.put("user", AVUser.getCurrentUser());
				repair.saveInBackground(new SaveCallback() {
					
					@Override
					public void done(AVException e) {
						// TODO Auto-generated method stub
						if(e==null){
							Toast.makeText(getActivity(), "报修成功", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getActivity(), "报修失败", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
		if(AVUser.getCurrentUser()!=null){
			loginView.setVisibility(View.GONE);
			repairView.setVisibility(View.VISIBLE);
		}
	}
}
