package com.gdin.netcentermans.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermans.R;

public class UserInfoActivity extends ActionBarActivity {
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
    private ImageView imgClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_info_n);
        initView();
    }

    private void initView(){
        tvSNum = (TextView) findViewById(R.id.tv_user_info_student_num);
        tvNName = (TextView) findViewById(R.id.tv_user_info_student_name);
        etHouse =  (EditText) findViewById(R.id.et_user_info_student_house);
        etHouseTel = (EditText) findViewById(R.id.et_user_info_student_house_phone);
        etPhone = (EditText) findViewById(R.id.et_user_info_student_phone);
        tvCardNum = (TextView) findViewById(R.id.tv_user_info_student_card_num);
        tvAddress = (TextView) findViewById(R.id.tv_user_info_student_address);
        tvTime = (TextView) findViewById(R.id.tv_user_info_apply_time);
        tvEtime = (TextView) findViewById(R.id.tv_user_info_expration_time);
        tvIp = (TextView) findViewById(R.id.tv_user_info_ip_info);
        tvSave = (CircularProgressButton) findViewById(R.id.tv_user_info_save);
        imgClose = (ImageView) findViewById(R.id.img_user_info_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AVUser user = AVUser.getCurrentUser();
        if(user==null){
            Toast.makeText(UserInfoActivity.this, "账号出错，请退出重新登录", Toast.LENGTH_LONG).show();
            finish();
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
        tvSave.setOnClickListener(new View.OnClickListener() {
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
