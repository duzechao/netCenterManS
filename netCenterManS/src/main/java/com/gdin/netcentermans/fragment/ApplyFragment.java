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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 入网申请
 * 
 * @version netCenterManS
 * @author DZC
 * @Time 2015-3-1 上午11:41:59
 * 
 * 
 */
public class ApplyFragment extends Fragment {
	

	private RequestQueue queue;
	private View view;

	// 申请登录
	private View loginView;
	private EditText etSNum;
	private EditText etBirthday;
	private TextView tvSubmit;

	// 进入申请界面
	private View appleView;
	private TextView tvStudentNum;
	private TextView tvStudentName;
	private TextView tvCampus;// 校区
	private TextView tvBuilding;// 楼栋
	private EditText etHouseNum;// 房号
	private EditText etNetNum;// 端口号
	private EditText etHousePhone;// 宿舍电话
	private EditText etPhone;// 学生电话
	private EditText etCardNum;// 上机证号
	private EditText etPass;// 认证密码
	private EditText etAddress;// 网卡地址
	private EditText etClass;// 班别
	private EditText etYs;// 院系
	
	private static ApplyFragment fragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		queue = MyApplication.mQueue;
		view = inflater.inflate(R.layout.fragment_apply, null);
		initView();
		return view;
	}
	
	public static ApplyFragment getInstance(){
		if(fragment==null){
			fragment = new ApplyFragment();
		}
		return fragment;
	}

	private void initView() {
		TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
		title.setText("入网申请");
		loginView = view.findViewById(R.id.ll_apple_login);
		etSNum = (EditText) view.findViewById(R.id.et_student_num_apply);
		etBirthday = (EditText) view.findViewById(R.id.et_birthday);
		tvSubmit = (TextView) view.findViewById(R.id.tv_submit_apply);

		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sNum = etSNum.getText().toString();
				String birthday = etBirthday.getText().toString();
				if (TextUtils.isEmpty(birthday) || TextUtils.isEmpty(sNum)) {
					return;
				}
				MStringRequest request = new MStringRequest(Method.POST,
						"http://202.192.72.25/netadmin/public/xxrw/sqrw2.jsp",
						new Listener<String>() {

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub
								if (response.length() > 10000) {
									Document doc = Jsoup.parse(response);
									// System.out.println("学号"+doc.getElementById("xh").val());
									loginView.setVisibility(View.GONE);
									
									appleView.setVisibility(View.VISIBLE);
									String sNum = doc.getElementById("xh").val();
									String name = doc.getElementById("xm").val();
									String campus = tvCampus.getText().toString();
									String buiding = tvBuilding.getText().toString();
									String houseNum = etHouseNum.getText().toString();
									String netNum = etNetNum.getText().toString();
									String housePhone  = etHousePhone.getText().toString();
									String phone = etPhone.getText().toString();
									String cardNum = etCardNum.getText().toString();
									String pass = etPass.getText().toString();
									String address = etAddress.getText().toString();
									String clas = etClass.getText().toString();
									String ys = etYs.getText().toString();
									HashMap<String, String> params = new HashMap<String, String>();
									params.put("xh", sNum);
									params.put("xm", name);
									params.put("s1", campus);
									params.put("building_id", buiding);
									params.put("sushe_no", houseNum);
									params.put("netport_no", netNum);
									params.put("tel", housePhone);
									params.put("handset", phone);
									params.put("CartNo", cardNum);
									params.put("passWord", pass);
									params.put("Mac", address);
									params.put("class_name", clas);
									params.put("szyx", ys);
									params.put("class_code", doc.getElementById("class_code").val());
									params.put("xi_daima", doc.getElementById("xi_daima").val());
								} else {
									//Log.d("",response);
									Toast.makeText(getActivity(), response.trim(), Toast.LENGTH_SHORT).show();
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								if (error != null) {
									Log.d("收到数据", error.toString());
								}
							}
						});
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("xh", sNum);
				params.put("csrq", birthday);
				request.setParams(params);
				queue.add(request);
			}
		});
		
		appleView = view.findViewById(R.id.ll_apply_commit);

		tvStudentNum = (TextView) view.findViewById(R.id.tv_student_num_apply);
		tvStudentName = (TextView) view.findViewById(R.id.tv_student_name_apply);
		tvCampus = (TextView) view.findViewById(R.id.tv_student_campus_apply);
		tvBuilding = (TextView) view.findViewById(R.id.tv_student_building_apply);
		etHouseNum = (EditText) view.findViewById(R.id.et_student_house_num_apply);
		etNetNum = (EditText) view.findViewById(R.id.et_student_net_num_apply);
		etHousePhone = (EditText) view.findViewById(R.id.et_student_house_phone_apply);
		etPhone = (EditText) view.findViewById(R.id.et_student_phone_apply);
		etCardNum = (EditText) view.findViewById(R.id.et_student_card_num_apply);
		etPass = (EditText) view.findViewById(R.id.et_student_password_apply);
		etAddress = (EditText) view.findViewById(R.id.et_student_address_apply);
		etClass = (EditText) view.findViewById(R.id.et_student_class_apply);
		etYs = (EditText) view.findViewById(R.id.et_student_ys_apply);
	}
}
