package com.gdin.netcentermans.fragment;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.dd.CircularProgressButton;
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
public class UserInforFragmentN extends Fragment{

	private View view;

	private TextView tvSNum;
	private TextView tvNName;
	private EditText etHouse;
	private EditText etHouseTel;
	private EditText etPhone;
	private TextView tvCardNum;
	private TextView tvAddress;
	private TextView tvTime;
	private TextView tvEtime;
	private TextView tvIp;
    private CircularProgressButton tvSave;
    private CircularProgressButton btReturn;
	
	
	private static UserInforFragmentN fragment;
	public static UserInforFragmentN getInstance(){
		if(fragment==null){
			fragment = new UserInforFragmentN();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user_info_n, null);
		initView();
		return view;
	}
	
	private void initView(){

        TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
        title.setText("我的资料");
		tvSNum = (TextView) view.findViewById(R.id.tv_user_info_student_num);
		tvNName = (TextView) view.findViewById(R.id.tv_user_info_student_name);
        etHouse =  (EditText) view.findViewById(R.id.et_user_info_student_house);
		etHouseTel = (EditText) view.findViewById(R.id.et_user_info_student_house_phone);
		etPhone = (EditText) view.findViewById(R.id.et_user_info_student_phone);
		tvCardNum = (TextView) view.findViewById(R.id.tv_user_info_student_card_num);
		tvAddress = (TextView) view.findViewById(R.id.tv_user_info_student_address);
		tvTime = (TextView) view.findViewById(R.id.tv_user_info_apply_time);
		tvEtime = (TextView) view.findViewById(R.id.tv_user_info_expration_time);
		tvIp = (TextView) view.findViewById(R.id.tv_user_info_ip_info);
        tvSave = (CircularProgressButton) view.findViewById(R.id.tv_user_info_save);
        btReturn = (CircularProgressButton) view.findViewById(R.id.tv_user_info_submit);
        btReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AVUser user = AVUser.getCurrentUser();
        if(user==null){
            getActivity().getSupportFragmentManager().popBackStack();
            Fragment fragment1 = new UserLoginFragment();
            Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_main_content, fragment1).commit();
        }else{
            tvSNum.setText(user.getString("sNum"));
            tvNName.setText(user.getString("sName"));
            etHouse.setText(user.getString("house"));
            etHouseTel.setText(user.getString("tel"));
            etPhone.setText(user.getMobilePhoneNumber());
            tvCardNum.setText(user.getString("cardNum"));
            tvAddress.setText(user.getString("netAdress"));
            tvTime.setText(user.getString("time"));
            tvEtime.setText(user.getString("eTime"));
            tvIp.setText(user.getString("ip"));
        }

        TextWatcher textWatcher = new TextWatcher() {
            String str = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSave.setProgress(0);
                tvSave.setVisibility(View.VISIBLE);
            }
        };
        etHouse.addTextChangedListener(textWatcher);
        etHouseTel.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);
        tvSave.setIndeterminateProgressMode(true);
        tvSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvSave.getProgress()==100){
                    return;
                }
                tvSave.setProgress(50);
                AVUser user = AVUser.getCurrentUser();
                String house = etHouse.getText().toString();
                String houseTel = etHouseTel.getText().toString();
                String phone  = etPhone.getText().toString();
                user.put("house",house);
                user.put("tel",houseTel);
                user.put("phone",phone);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            tvSave.setProgress(100);
                        }else{
                            tvSave.setProgress(-1);
                        }
                    }
                });

            }
        });
	}
}
