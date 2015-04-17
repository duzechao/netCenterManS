package com.gdin.netcentermans.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RefreshCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermans.MyApplication;
import com.gdin.netcentermans.R;

public class LoginActivity extends ActionBarActivity {
    private LinearLayout llLogin;
    private EditText etSNum;
    private EditText etPassword;
    private CircularProgressButton tvSubmit;
    private CircularProgressButton tvSign;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        initView();
    }

    private void initView(){
        //llLogin = (LinearLayout) view.findViewById(R.id.ll_user_info_login);
        etSNum = (EditText) findViewById(R.id.et_user_login_student_num);
        etPassword = (EditText) findViewById(R.id.et_user_login_password);
        tvSubmit = (CircularProgressButton) findViewById(R.id.tv_user_login_submit);
        tvSign = (CircularProgressButton) findViewById(R.id.tv_user_login_sign);
        tvSubmit.setIndeterminateProgressMode(true);
        imgReturn = (ImageView) findViewById(R.id.img_login_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignActivity.class));
            }
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvSubmit.setProgress(0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    tvSubmit.setProgress(0);
            }
        };
        etSNum.addTextChangedListener(watcher);
        etPassword.addTextChangedListener(watcher);
        tvSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(tvSubmit.getProgress()==-1){
                    return;
                }
                String sNum = etSNum.getText().toString();
                String pass = etPassword.getText().toString();
                if(TextUtils.isEmpty(sNum)||TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "请填写有效信息", Toast.LENGTH_LONG).show();
                    return;
                }
                tvSubmit.setProgress(50);
                AVUser.logInInBackground(sNum, pass, new LogInCallback<AVUser>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void done(AVUser user, AVException e) {
                        // TODO Auto-generated method stub
                        if (e == null) {
                            user.setFetchWhenSave(true);
                            user.put("installationId", MyApplication.installationId);
                            user.saveInBackground();

                           /* user.fetchInBackground("building",new GetCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if(e==null){

                                    }
                                }
                            });*/
                            AVUser.getCurrentUser().refreshInBackground("building", new RefreshCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if (e != null) {
                                        Log.d("刷新失败", e.toString());
                                    } else {
                                    }
                                    Log.d("", "刷新");
                                }
                            });
                            tvSubmit.setProgress(100);
                            finish();
                        } else if (e.getCode() == AVException.USER_DOESNOT_EXIST) {
                            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_LONG).show();
                        } else if (e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH) {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        } else if (e.getCode() == AVException.CONNECTION_FAILED) {
                            Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                        }
                        if (e != null) {
                            tvSubmit.setProgress(-1);
                            Log.d("", e.toString());
                        }
                    }
                });
            }
        });
    }
}
