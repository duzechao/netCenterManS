package com.gdin.netcentermans;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.SaveCallback;

public class MyApplication extends Application{
	
	public static RequestQueue mQueue;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mQueue = Volley.newRequestQueue(this);
        AVOSCloud.initialize(this,
                "n672vu0h76ujkvbszsoe0yhedsuepp5i9rt9dp2k7t1p42zd",
                "wp5jgq58xhoj4h7mcnr29wxoe8ifxasui717sjs15gyzj06f");
        AVInstallation.getCurrentInstallation().saveInBackground(
                new SaveCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            // 保存成功
                            String installationId = AVInstallation
                                    .getCurrentInstallation()
                                    .getInstallationId();
                            // 关联 installationId 到用户表等操作……
                        } else {
                            // 保存失败，输出错误信息
                        }
                    }
                });
	}

}
