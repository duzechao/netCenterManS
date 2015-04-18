package com.gdin.netcentermans.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.gdin.netcentermans.R;

public class RepairActivity extends ActionBarActivity {

    private EditText etReason;
    private EditText etTime;
    private EditText etReasonInfo;
    private TextView tvRepaiSummit;
    protected View imgCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        initView();
    }

    private void initView(){
        etReason = (EditText) findViewById(R.id.et_repair_reason);
        etTime = (EditText) findViewById(R.id.et_repair_time);
        etReasonInfo = (EditText) findViewById(R.id.et_repair_reason_info);
        tvRepaiSummit = (TextView) findViewById(R.id.tv_repair_c_submit);
        imgCancel = findViewById(R.id.img_repair_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRepaiSummit.setOnClickListener(new View.OnClickListener() {

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
                        if (e == null) {
                            Toast.makeText(RepairActivity.this, "报修成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RepairActivity.this, "报修失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
