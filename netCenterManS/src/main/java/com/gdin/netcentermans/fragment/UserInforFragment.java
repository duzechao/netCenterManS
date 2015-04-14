package com.gdin.netcentermans.fragment;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.MStringRequest;
/**
 * 用户信息查询
 * @version netCenterManS	
 * @author DZC
 * @Time  2015-3-1 上午11:41:59
 *
 *
 */
public class UserInforFragment extends Fragment{

	private View view;
	private RequestQueue queue;
	
	private LinearLayout llLogin;
	private EditText etSNum;
	private EditText etPassword;
	private EditText etPhone;
	private EditText etLoginPass;
	private TextView tvSubmit;
	
	private LinearLayout llCommit;
	private TextView tvSNum;
	private TextView tvNName;
	private TextView tvHouse;
	private TextView tvHouseTel;
	private TextView tvPhone;
	private TextView tvCardNum;
	private TextView tvAddress;
	private TextView tvTime;
	private TextView tvEtime;
	private TextView tvIp;
	
	
	private static UserInforFragment fragment;
	public static UserInforFragment getInstance(){
		if(fragment==null){
			fragment = new UserInforFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		queue = MyApplication.mQueue;
		view = inflater.inflate(R.layout.fragment_user_info, null);
		initView();
		return view;
	}
	
	private void initView(){
		llLogin = (LinearLayout) view.findViewById(R.id.ll_user_info_login);
		etLoginPass = (EditText) view.findViewById(R.id.et_user_info_login_password);
		etSNum = (EditText) view.findViewById(R.id.et_user_info_student_num);
		etPassword = (EditText) view.findViewById(R.id.et_user_info_pwd);
		etPhone = (EditText) view.findViewById(R.id.et_user_info_phone);
		tvSubmit = (TextView) view.findViewById(R.id.tv_user_info_login_submit);
		tvHouse = (TextView) view.findViewById(R.id.tv_user_info_student_house);
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String sNum = etSNum.getText().toString();
				final String pass = etPassword.getText().toString();
				final String phone = etPhone.getText().toString();
				final String uPass = etLoginPass.getText().toString();
				if(TextUtils.isEmpty(sNum)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(uPass)){
					Toast.makeText(getActivity(), "请填写有效信息", Toast.LENGTH_LONG).show();
					return ;
				}
				
				MStringRequest request = new MStringRequest(Method.POST,"http://202.192.72.25/netadmin/public/xxrw/editinfo.jsp", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						if(response.length()>6000){
							Document doc = Jsoup.parse(response);
							//System.out.println("学号"+doc.getElementById("xh").val());
							final Elements es = doc.getElementsByTag("table").get(5).getElementsByTag("td");
							AVUser avUser = new AVUser();
							avUser.setUsername(es.get(2).text().replaceAll("\\s*", ""));
							avUser.setPassword(pass);
							avUser.setFetchWhenSave(true);
							avUser.put("sName",es.get(2).text().replaceAll("\\s*", ""));
							avUser.put("sNum", es.get(4).text().replaceAll("\\s*", ""));
							avUser.put("tel", es.get(8).text().replaceAll("\\s*", ""));
							avUser.put("house", es.get(6).text().replaceAll("\\s*", ""));
							avUser.setMobilePhoneNumber(es.get(10).text().replaceAll("\\s*", ""));
							//Toast.makeText(getActivity(), es.get(10).text().replaceAll("\\s*", ""), Toast.LENGTH_LONG).show();
							//avUser.put("uPhone", es.get(10).text().replaceAll("\\s*", ""));
							avUser.put("cardNum", es.get(12).text().replaceAll("\\s*", ""));
							avUser.put("netAddress", es.get(14).text().replaceAll("\\s*", ""));
							avUser.put("time", es.get(16).text().replaceAll("\\s*", ""));
							avUser.put("eTime", es.get(18).text().replaceAll("\\s*", ""));
							avUser.put("ip", es.get(20).text().replaceAll("\\s*", ""));
							avUser.signUpInBackground(new SignUpCallback() {
								
								@Override
								public void done(AVException e) {
									// TODO Auto-generated method stub
									if(e!=null){
										Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
									}else{
										llLogin.setVisibility(View.GONE);
										llCommit.setVisibility(View.VISIBLE);
										tvSNum.setText(es.get(2).text());
										tvNName.setText(es.get(4).text());
										tvHouse.setText(es.get(6).text());
										tvHouseTel.setText(es.get(8).text());
										tvPhone.setText(es.get(10).text());
										tvCardNum.setText(es.get(12).text());
										tvAddress.setText(es.get(14).text());
										tvTime.setText(es.get(16).text());
										tvEtime.setText(es.get(18).text());
										tvIp.setText(Html.fromHtml(es.get(20).text()));
									}
								}
							});
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
				params.put("passWord", pass);
				params.put("handset", phone);
				request.setParams(params);
				queue.add(request);
				
			}
		});
		
		llCommit = (LinearLayout) view.findViewById(R.id.ll_user_info_commit);
		tvSNum = (TextView) view.findViewById(R.id.tv_user_info_student_num);
		tvNName = (TextView) view.findViewById(R.id.tv_user_info_student_name);
		tvHouseTel = (TextView) view.findViewById(R.id.tv_user_info_student_house_phone);
		tvPhone = (TextView) view.findViewById(R.id.tv_user_info_student_phone);
		tvCardNum = (TextView) view.findViewById(R.id.tv_user_info_student_card_num);
		tvAddress = (TextView) view.findViewById(R.id.tv_user_info_student_address);
		tvTime = (TextView) view.findViewById(R.id.tv_user_info_apply_time);
		tvEtime = (TextView) view.findViewById(R.id.tv_user_info_expration_time);
		tvIp = (TextView) view.findViewById(R.id.tv_user_info_ip_info);
	}
}
