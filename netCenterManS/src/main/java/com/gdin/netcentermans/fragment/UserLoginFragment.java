package com.gdin.netcentermans.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RefreshCallback;
import com.gdin.netcentermans.MainActivity;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;
/**
 * 用户信息查询
 * @version netCenterManS	
 * @author DZC
 * @Time  2015-3-1 上午11:41:59
 *
 *
 */
public class UserLoginFragment extends Fragment{

	private View view;
	private RequestQueue queue;
	
	private LinearLayout llLogin;
	private EditText etSNum;
	private EditText etPassword;
	private TextView tvSubmit;
    private TextView tvSign;
	
	
	private static UserLoginFragment fragment;
	public static UserLoginFragment getInstance(){
		if(fragment==null){
			fragment = new UserLoginFragment();
		}
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		queue = MyApplication.mQueue;
		view = inflater.inflate(R.layout.user_login, null);
		initView();
		return view;
	}
	
	private void initView(){
		//llLogin = (LinearLayout) view.findViewById(R.id.ll_user_info_login);
		TextView title  = (TextView) getActivity().findViewById(R.id.tv_main_title);
		title.setText("登录");
		etSNum = (EditText) view.findViewById(R.id.et_user_login_student_num);
		etPassword = (EditText) view.findViewById(R.id.et_user_login_password);
		tvSubmit = (TextView) view.findViewById(R.id.tv_user_login_submit);
        tvSign = (TextView) view.findViewById(R.id.tv_user_login_sign);
        tvSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_main_content,new UserSignFragment()).addToBackStack("login").commit();
            }
        });
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sNum = etSNum.getText().toString();
				String pass = etPassword.getText().toString();
				if(TextUtils.isEmpty(sNum)||TextUtils.isEmpty(pass)){
					Toast.makeText(getActivity(), "请填写有效信息", Toast.LENGTH_LONG).show();
					return;
				}
				AVUser.logInInBackground(sNum, pass, new LogInCallback<AVUser>() {
					
					@SuppressLint("NewApi")
					@Override
					public void done(AVUser user, AVException e) {
						// TODO Auto-generated method stub
						if(e==null){
							user.setFetchWhenSave(true);
							user.put("installationId",MyApplication.installationId);
							user.saveInBackground();

                           /* user.fetchInBackground("building",new GetCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if(e==null){

                                    }
                                }
                            });*/
                            AVUser.getCurrentUser().refreshInBackground("building",new RefreshCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if(e!=null){
                                        Log.d("刷新失败",e.toString());
                                    }else{
                                    }
                                    Log.d("","刷新");
                                }
                            });
							getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgment_main_content, new UserInforFragmentN()).commit();
						}else if(e.getCode() == AVException.USER_DOESNOT_EXIST){
							Toast.makeText(getActivity(), "用户不存在", Toast.LENGTH_LONG).show();
						}else if(e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH){
							Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_LONG).show();
						}else if(e.getCode()==AVException.CONNECTION_FAILED){
							Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_LONG).show();
						}
						if(e!=null)Log.d("",e.toString());
					}
				});
			}
		});
	}
}
