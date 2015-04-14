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
public class RepairFragment extends Fragment {

	private static RepairFragment fragment;

	public static RepairFragment getInstance() {
		if (fragment == null) {
			fragment = new RepairFragment();
		}
		return fragment;
	}

	private View view;

	// 登录
	private View loginView;
	private EditText etSNum;
	private EditText etCNum;
	private TextView tvSubmit;

	private RequestQueue queue;
	
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
		queue = MyApplication.mQueue;
		view = inflater.inflate(R.layout.fragment_repair, null);
		initView();		
		return view;
	}
	
	private void initView(){
		loginView = view.findViewById(R.id.ll_repair_login);
		etSNum = (EditText) view.findViewById(R.id.et_student_num);
		etCNum = (EditText) view.findViewById(R.id.et_computer_num);
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
				String cNum = etCNum.getText().toString();
				if(TextUtils.isEmpty(cNum)||TextUtils.isEmpty(sNum)){
					return ;
				}
				MStringRequest request = new MStringRequest(Method.POST,"http://202.192.72.25/netadmin/public/gzbx/login.jsp?usertype=student", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.d("",response);
						if(response.length()>10000){
							Document doc = Jsoup.parse(response);
							//System.out.println("学号"+doc.getElementById("xh").val());
							loginView.setVisibility(View.GONE);
							repairView.setVisibility(View.VISIBLE);
							
						}else{
							//Log.d("",response);
							Toast.makeText(getActivity(), Html.fromHtml(response), Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						if(error!=null){
							//Log.d("收到数据", error.toString());
							
						}
					}
				});
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("xh", sNum);
				params.put("CartNo", cNum);
				request.setParams(params);
				queue.add(request);
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
				MStringRequest request = new MStringRequest(Method.POST,"http://http://202.192.72.25/netadmin/public/gzbx//gzbxok.jsp", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.d("",response);
						if(response.length()>10000){
							Document doc = Jsoup.parse(response);
							//System.out.println("学号"+doc.getElementById("xh").val());
						}else{
							//Log.d("",response);
							Toast.makeText(getActivity(), Html.fromHtml(response), Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						if(error!=null){
							//Log.d("收到数据", error.toString());
							
						}
					}
				});
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("title", reason);
				params.put("wanttime", time);
				params.put("textarea", reasonInfo);
				request.setParams(params);
				queue.add(request);
			}
		});
	}
}
