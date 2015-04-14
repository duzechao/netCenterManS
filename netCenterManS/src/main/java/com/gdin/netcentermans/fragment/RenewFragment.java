package com.gdin.netcentermans.fragment;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.MStringRequest;
/**
 * 续费申请
 * @version netCenterManS	
 * @author DZC
 * @Time  2015-3-1 上午11:41:59
 *
 *
 */
public class RenewFragment extends Fragment{
	
	private static RenewFragment fragment;
	public static RenewFragment getInstance(){
		if(fragment==null){
			fragment = new RenewFragment();
		}
		return fragment;
	}
	
	private View view;
	
	//登录
	private View loginView;
	private EditText etSNum;
	private EditText etCNum;
	private TextView tvSubmit;
	
	private RequestQueue queue;
	
	
	//进入申请界面
	private View repairView;
	private TextView tvStudentNum;
	private TextView tvStudentName;
	private TextView tvCampus;//校区
	private TextView tvBuilding;//楼栋
	private EditText etHouseNum;//房号
	private EditText etNetNum;//端口号
	private EditText etHousePhone;//宿舍电话
	private EditText etPhone;//学生电话
	private TextView tvCardNum;//上机证号
	private EditText etPass;//认证密码
	private EditText etAddress;//网卡地址
	private EditText etClass;//班别
	private EditText etYs;//院系
	private TextView tvRepairInfo;//最后一 次缴费情况
	private TextView tvRepairTime;//最后一次缴费时间
	private TextView tvRepairDeadLine;//缴费到期时间
	private TextView tvIp;//ip地址
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		queue = MyApplication.mQueue;
		view = inflater.inflate(R.layout.fragment_renew, null);
		initView();		
		return view;
	}
	
	@SuppressLint("NewApi")
	private void initView(){
		TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
		title.setText("续费申请");
		loginView = view.findViewById(R.id.ll_renew_login);
		etSNum = (EditText) view.findViewById(R.id.et_student_num);
		etCNum = (EditText) view.findViewById(R.id.et_computer_num);
		tvSubmit = (TextView) view.findViewById(R.id.tv_submit);
		
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sNum = etSNum.getText().toString();
				String cNum = etCNum.getText().toString();
				if(TextUtils.isEmpty(cNum)||TextUtils.isEmpty(sNum)){
					return ;
				}
				MStringRequest request = new MStringRequest(Method.POST,"http://202.192.72.25/netadmin/public/xxrw/sqxf2.jsp", new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						if(response.length()>10000){
							Document doc = Jsoup.parse(response);
							//System.out.println("学号"+doc.getElementById("xh").val());
							loginView.setVisibility(View.GONE);
							repairView.setVisibility(View.VISIBLE);
							tvStudentNum.setText(doc.getElementById("xh").val());
							tvStudentName.setText(doc.getElementById("xm").val());
							tvCampus.setText(doc.getElementById("s1").getElementsByAttribute("selected").text());
							tvBuilding.setText(doc.getElementById("building_id").getElementsByAttribute("selected").text());
							etHouseNum.setText(doc.getElementById("sushe_no").val());
							etNetNum.setText(doc.getElementById("netport_no").val());
							etHousePhone.setText(doc.getElementById("tel").val());
							etPhone.setText(doc.getElementById("handset").val());
							tvCardNum.setText(doc.getElementById("CartNo").val());
							etAddress.setText(doc.getElementById("Mac").val());
							etClass.setText(doc.getElementById("class_name").val());
							etYs.setText(doc.getElementById("szyx").val());
							Elements elements = doc.getElementsByTag("strong");
							tvRepairInfo.setText(elements.get(2).text());
							tvRepairTime.setText(elements.get(4).text());
							tvRepairDeadLine.setText(elements.get(6).text());
							tvIp.setText(elements.get(8).text());
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
		
		repairView = view.findViewById(R.id.ll_renew_commit);

		tvStudentNum = (TextView) view.findViewById(R.id.tv_student_num);
		tvStudentName = (TextView) view.findViewById(R.id.tv_student_name);
		tvCampus = (TextView) view.findViewById(R.id.tv_student_campus);
		tvBuilding = (TextView) view.findViewById(R.id.tv_student_building);
		etHouseNum = (EditText) view.findViewById(R.id.et_student_house_num);
		etNetNum = (EditText) view.findViewById(R.id.et_student_net_num);
		etHousePhone = (EditText) view.findViewById(R.id.et_student_house_phone);
		etPhone = (EditText) view.findViewById(R.id.et_student_phone);
		tvCardNum = (TextView) view.findViewById(R.id.tv_student_card_num);
		etPass = (EditText) view.findViewById(R.id.et_student_password);
		etAddress = (EditText) view.findViewById(R.id.et_student_address);
		etClass = (EditText) view.findViewById(R.id.et_student_class);
		etYs = (EditText) view.findViewById(R.id.et_student_ys);
		tvRepairInfo = (TextView) view.findViewById(R.id.tv_student_repair_infor);
		tvRepairTime = (TextView) view.findViewById(R.id.tv_student_repair_time);
		tvRepairDeadLine = (TextView) view.findViewById(R.id.tv_student_dead_line);
		tvIp = (TextView) view.findViewById(R.id.tv_student_ip);
	}
}
